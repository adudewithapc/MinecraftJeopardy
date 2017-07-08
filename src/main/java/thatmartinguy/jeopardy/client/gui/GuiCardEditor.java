package thatmartinguy.jeopardy.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thatmartinguy.jeopardy.init.ModItems;
import thatmartinguy.jeopardy.util.ItemNBTHelper;

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

        closeButton = this.addButton(new GuiButton(4, this.width / 2 + 200, this.height + 200, "Close"));
        saveButton = this.addButton(new GuiButton(5, this.width / 2 - 200, this.height + 200, "Save"));

        booleanButton = this.addButton(new GuiButton(6, this.width / 2, this.height / 2 + 200, "True/False Question"));

        question = new GuiTextField(7, this.fontRenderer, this.width / 2, 50, 200, 20);
        answerA = new GuiTextField(8, this.fontRenderer, this.width / 2 - 200, this.height / 2 - 200, 200, 20);
        answerB = new GuiTextField(9, this.fontRenderer, this.width / 2 + 200, this.height / 2 + 200, 200, 20);
        answerC = new GuiTextField(10, this.fontRenderer, this.width / 2 - 200, this.height / 2 + 200, 200, 20);
        answerD = new GuiTextField(11, this.fontRenderer, this.width / 2 + 200, this.height / 2 + 200, 200, 20);

        trueAnswerButton = this.addButton(new GuiButton(20, answerA.x, answerA.y, "True"));
        falseAnswerButton = this.addButton(new GuiButton(21, answerB.x, answerB.y, "False"));
        trueAnswerButton.enabled = false;
        falseAnswerButton.enabled = true;

        answerList.add(answerA);
        answerList.add(answerB);
        answerList.add(answerC);
        answerList.add(answerD);

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
            case 0:
                this.mc.displayGuiScreen(null);
                break;
            case 1:
                saveCard();
                break;
            case 2:
                if(booleanQuestion)
                {
                    booleanQuestion = false;
                    booleanButton.displayString = "True/False Question";
                    for(GuiTextField textField : answerList)
                    {
                        textField.setVisible(true);
                    }
                    trueAnswerButton.enabled = false;
                    falseAnswerButton.enabled = false;
                }
                else
                {
                    booleanQuestion = true;
                    booleanButton.displayString = "A-D Question";
                    for(GuiTextField textField : answerList)
                    {
                        textField.setVisible(false);
                    }
                    trueAnswerButton.enabled = true;
                    falseAnswerButton.enabled = true;
                }
                break;
        }
        if(button instanceof CorrectAnswerButton)
            correctAnswerID = button.id;
    }

    private void saveCard()
    {
        paper.shrink(1);

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
    }

    @Override
    public void updateScreen()
    {
        for(GuiTextField answer : answerList)
        {
            if(answer.getText() != "" && correctAnswerID > -1 && question.getText() != "")
            {
                saveButton.enabled = true;
            }
            else
            {
                saveButton.enabled = false;
            }
        }
    }

    private static class CorrectAnswerButton extends GuiButton
    {
        public CorrectAnswerButton(int buttonId, GuiTextField answerField)
        {
            super(buttonId, answerField.x + 10, answerField.y, 20, 20, "");
        }
    }
}
