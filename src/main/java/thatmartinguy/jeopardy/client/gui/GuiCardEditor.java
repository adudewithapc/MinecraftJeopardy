package thatmartinguy.jeopardy.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.init.ModItems;
import thatmartinguy.jeopardy.network.CardWriteMessage;
import thatmartinguy.jeopardy.util.ItemNBTHelper;
import thatmartinguy.jeopardy.util.LogHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiCardEditor extends GuiScreen
{
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("textures/gui/demo_background.png");

    private boolean booleanQuestion;
    private int correctAnswerID = -1;

    private EntityPlayer player;
    private ItemStack paper;

    private GuiButton closeButton;
    private GuiButton saveButton;
    private GuiButton booleanButton;

    private GuiButton trueAnswerButton;
    private GuiButton falseAnswerButton;

    private GuiTextField question;
    private GuiTextField answerA;
    private GuiTextField answerB;
    private GuiTextField answerC;
    private GuiTextField answerD;

    private List<GuiTextField> answerList = new ArrayList<>();

    public GuiCardEditor(EntityPlayer player, ItemStack paper)
    {
        this.player = player;
        this.paper = paper;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        this.drawTexturedModalRect((this.width - 256) / 2, 2, 0, 0, 256, 256);

        question.drawTextBox();
        answerA.drawTextBox();
        answerB.drawTextBox();
        answerC.drawTextBox();
        answerD.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.answerList.clear();
        Keyboard.enableRepeatEvents(true);

        closeButton = this.addButton(new GuiButton(4, this.width / 2 + 50, this.height - 40, "Close"));
        saveButton = this.addButton(new GuiButton(5, this.width / 2 - 225, this.height - 40, "Save"));

        booleanButton = this.addButton(new GuiButton(6, this.width / 2, this.height / 2 + 200, "True/False Question"));

        question = new GuiTextField(7, this.fontRenderer, this.width / 2 - 100, 40, 200, 20);
        answerA = new GuiTextField(8, this.fontRenderer, this.width / 2 - 225, this.height / 2 - 40, 200, 20);
        answerB = new GuiTextField(9, this.fontRenderer, this.width / 2 + 10, this.height / 2 - 40, 200, 20);
        answerC = new GuiTextField(10, this.fontRenderer, this.width / 2 - 225, this.height / 2 + 40, 200, 20);
        answerD = new GuiTextField(11, this.fontRenderer, this.width / 2 + 10, this.height / 2 + 40, 200, 20);

        trueAnswerButton = this.addButton(new GuiButton(20, answerA.x, answerA.y, "True"));
        falseAnswerButton = this.addButton(new GuiButton(21, answerB.x, answerB.y, "False"));
        trueAnswerButton.visible = false;
        falseAnswerButton.visible = false;

        answerList.add(answerA);
        answerList.add(answerB);
        answerList.add(answerC);
        answerList.add(answerD);

        answerA.setFocused(true);

        this.buttonList.add(new CorrectAnswerButton(0, answerA));
        this.buttonList.add(new CorrectAnswerButton(1, answerB));
        this.buttonList.add(new CorrectAnswerButton(2, answerC));
        this.buttonList.add(new CorrectAnswerButton(3, answerD));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        switch(button.id)
        {
            case 4:
                this.mc.displayGuiScreen(null);
                break;
            case 5:
                saveCard();
                break;
            case 6:
                if(booleanQuestion)
                {
                    booleanQuestion = false;
                    booleanButton.displayString = "True/False Question";
                    for(GuiTextField textField : answerList)
                    {
                        textField.setVisible(true);
                    }
                    trueAnswerButton.enabled = false;
                    trueAnswerButton.visible = false;
                    falseAnswerButton.enabled = false;
                    falseAnswerButton.visible = false;
                }
                else
                {
                    booleanQuestion = true;
                    booleanButton.displayString = "A-D Question";
                    for (GuiTextField textField : answerList)
                    {
                        textField.setVisible(false);
                    }
                    trueAnswerButton.enabled = true;
                    trueAnswerButton.visible = true;
                    falseAnswerButton.enabled = true;
                    falseAnswerButton.visible = true;
                }
                break;

        }
        if(button instanceof CorrectAnswerButton)
            correctAnswerID = button.id;
        if(button.id == 20)
            correctAnswerID = 0;
        if(button.id == 21)
            correctAnswerID = 1;
    }

    private void saveCard()
    {
        ItemStack card = new ItemStack(ModItems.itemQuizCard);

        ItemNBTHelper.setBoolean(card, "BooleanQuestion", booleanQuestion);
        ItemNBTHelper.setString(card, "Question", question.getText());

        ItemNBTHelper.setString(card, "Author", player.getName());

        if(!booleanQuestion)
        {
            ItemNBTHelper.setString(card, "AnswerA", answerA.getText());
            ItemNBTHelper.setString(card, "AnswerB", answerB.getText());
            ItemNBTHelper.setString(card, "AnswerC", answerC.getText());
            ItemNBTHelper.setString(card, "AnswerD", answerD.getText());
        }

        ItemNBTHelper.setInt(card, "AnswerID", correctAnswerID);

        Jeopardy.NETWORK.sendToServer(new CardWriteMessage(paper, card));
        paper.shrink(1);
    }

    @Override
    public void updateScreen()
    {
        for(GuiTextField answer : answerList)
        {
            if(!answer.getText().isEmpty() && correctAnswerID > -1 && !question.getText().isEmpty())
            {
                saveButton.enabled = true;
            }
            else
            {
                saveButton.enabled = false;
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        for(GuiTextField textField : answerList)
        {
            textField.textboxKeyTyped(typedChar, keyCode);
        }
        question.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        for(GuiTextField textField : answerList)
        {
            if(textField.mouseClicked(mouseX, mouseY, mouseButton))
            {
                textField.setFocused(true);
            }
            else
            {
                textField.setFocused(false);
                question.setFocused(false);
            }
            LogHelper.info("Answer focused = " + textField.isFocused());
            LogHelper.info("Question focused = " + question.isFocused());
        }
        if(question.mouseClicked(mouseX, mouseY, mouseButton))
        {
            question.setFocused(true);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    private static class CorrectAnswerButton extends GuiButton
    {
        public CorrectAnswerButton(int buttonId, GuiTextField answerField)
        {
            super(buttonId, answerField.x + 205, answerField.y, 20, 20, "");
        }
    }
}
