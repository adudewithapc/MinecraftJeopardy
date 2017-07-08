package thatmartinguy.jeopardy.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import thatmartinguy.jeopardy.util.ItemNBTHelper;

import java.io.IOException;

public class GuiScreenCard extends GuiScreen
{
    private ItemStack card;
    private EntityPlayer player;

    private GuiLabel question;

    private GuiButton passButton;

    private GuiButton answerA;
    private GuiButton answerB;
    private GuiButton answerC;
    private GuiButton answerD;

    public GuiScreenCard(EntityPlayer player, ItemStack card)
    {
        this.player = player;
        this.card = card;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
    }

    @Override
    public void initGui()
    {
        this.labelList.add(question = new GuiLabel(this.fontRenderer, 4, this.width / 2, 50, 200, 200, 0xffffff));
        question.addLine(ItemNBTHelper.getString(card, "Question"));

        answerA = this.addButton(new GuiButton(0, 200, 200, ItemNBTHelper.getString(card, "AnswerA")));
        answerB = this.addButton(new GuiButton(1, this.width - 50, 200, ItemNBTHelper.getString(card, "AnswerB")));
        answerC = this.addButton(new GuiButton(2, 200, this.height / 2 + 100, ItemNBTHelper.getString(card, "AnswerC")));
        answerD = this.addButton(new GuiButton(3, this.width - 50, this.height / 2 + 100, ItemNBTHelper.getString(card, "AnswerD")));

        passButton = this.addButton(new GuiButton(5, this.width / 2, this.height - 100, "Pass Question"));

        if(ItemNBTHelper.getBoolean(card, "BooleanQuestion", false))
        {
           answerA.displayString = "True";
           answerB.displayString = "False";
           answerC.enabled = false;
           answerD.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if(button.id == 5)
        {
            if(!player.world.isRemote)
                player.sendMessage(new TextComponentString(player.getName() + " passed the question!"));
        }
        else
        {
            int answerID = ItemNBTHelper.getInt(card, "AnswerID", -1);
            if(answerID == -1)
            {
                player.sendStatusMessage(new TextComponentString("Incorrect answer button pressed! Discarding card"), false);
                card.shrink(1);
                this.mc.displayGuiScreen(null);
            }
            else if(button.id == answerID)
            {
                //TODO: Reward player...
            }
            else
            {
                player.sendMessage(new TextComponentString("Wrong answer."));
            }
        }
    }
}
