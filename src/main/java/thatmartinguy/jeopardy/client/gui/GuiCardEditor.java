package thatmartinguy.jeopardy.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.init.ModItems;
import thatmartinguy.jeopardy.network.CardWriteMessage;
import thatmartinguy.jeopardy.util.ItemNBTHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiCardEditor extends GuiScreen
{
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("textures/gui/demo_background.png");

    private boolean booleanQuestion = true;
    private int correctAnswerID = -1;

    private EntityPlayer player;
    private ItemStack paper;

    private GuiButton closeButton;
    private GuiButton saveButton;
    private GuiButton booleanButton;

    private GuiTextField question;
    private GuiTextField answerA;
    private GuiTextField answerB;
    private GuiTextField answerC;
    private GuiTextField answerD;

    private List<GuiTextField> answerList = new ArrayList<>();

    private CorrectAnswerButton correctA;
    private CorrectAnswerButton correctB;
    private CorrectAnswerButton correctC;
    private CorrectAnswerButton correctD;

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

        closeButton = this.addButton(new GuiButton(4, this.width / 2 + 50, this.height - 40, "Close"));
        saveButton = this.addButton(new GuiButton(5, this.width / 2 - 225, this.height - 40, "Save"));

        booleanButton = this.addButton(new GuiButton(6, this.width / 2 - 100, this.height / 2, "True/False Question"));

        question = new GuiTextField(7, this.fontRenderer, this.width / 2 - 100, 40, 200, 20);
        answerA = new GuiTextField(8, this.fontRenderer, this.width / 2 - 225, this.height / 2 - 40, 200, 20);
        answerB = new GuiTextField(9, this.fontRenderer, this.width / 2 + 10, this.height / 2 - 40, 200, 20);
        answerC = new GuiTextField(10, this.fontRenderer, this.width / 2 - 225, this.height / 2 + 40, 200, 20);
        answerD = new GuiTextField(11, this.fontRenderer, this.width / 2 + 10, this.height / 2 + 40, 200, 20);

        answerList.add(answerA);
        answerList.add(answerB);
        answerList.add(answerC);
        answerList.add(answerD);

        question.setFocused(true);

        correctA = this.addButton(new CorrectAnswerButton(0, answerA));
        correctB = this.addButton(new CorrectAnswerButton(1, answerB));
        correctC = this.addButton(new CorrectAnswerButton(2, answerC));
        correctD = this.addButton(new CorrectAnswerButton(3, answerD));
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
                saveCard(correctAnswerID);
                this.mc.displayGuiScreen(null);
                break;
            case 6:
                if(booleanQuestion)
                {
                    booleanQuestion = false;
                    booleanButton.displayString = "A-D Question";

                    answerA.setText("True");
                    answerA.setEnabled(false);
                    answerB.setText("False");
                    answerB.setEnabled(false);

                    answerC.setVisible(false);
                    answerC.setText("notempty");
                    answerD.setVisible(false);
                    answerD.setText("notempty");

                    correctC.visible = false;
                    correctD.visible = false;
                }
                else
                {
                    for(GuiTextField answer : answerList)
                    {
                        answer.setVisible(true);
                        answer.setEnabled(true);
                        answer.setText("");
                    }

                    correctC.visible = true;
                    correctD.visible = true;

                    booleanQuestion = true;
                    booleanButton.displayString = "True/False Question";
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

    private void saveCard(int answerID)
    {
        Jeopardy.NETWORK.sendToServer(new CardWriteMessage(paper, answerID, booleanQuestion, player.getName(), question.getText(), answerA.getText(), answerB.getText(), answerC.getText(), answerD.getSelectedText()));
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
        if(booleanQuestion)
        {
            for (GuiTextField textField : answerList)
            {
                if (textField.mouseClicked(mouseX, mouseY, mouseButton))
                {
                    textField.setFocused(true);
                }
                else
                {
                    textField.setFocused(false);
                    question.setFocused(false);
                }
            }
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
