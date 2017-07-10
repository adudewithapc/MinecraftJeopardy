package thatmartinguy.jeopardy.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.network.AnswerHandlerMessage;
import thatmartinguy.jeopardy.util.ItemNBTHelper;
import thatmartinguy.jeopardy.util.LogHelper;

import java.io.IOException;

public class GuiScreenCard extends GuiScreen
{
    private ItemStack card;
    private EntityPlayer player;

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
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui()
    {
        passButton = this.addButton(new GuiButton(5, this.width / 2 - 100, this.height - 40, "Pass Question"));

        answerA = this.addButton(new GuiButton(0, this.width / 2 - 225, this.height/ 2 - 40, ItemNBTHelper.getString(card, "AnswerA")));
        answerB = this.addButton(new GuiButton(1, this.width / 2 + 10, this.height / 2 - 40, ItemNBTHelper.getString(card, "AnswerB")));
        answerC = this.addButton(new GuiButton(2, this.width / 2 - 225, this.height / 2 + 40, ItemNBTHelper.getString(card, "AnswerC")));
        answerD = this.addButton(new GuiButton(3, this.width / 2 + 10, this.height / 2 + 40, ItemNBTHelper.getString(card, "AnswerD")));

        LogHelper.info("Is boolean question = " + ItemNBTHelper.getBoolean(card, "BooleanQuestion", false));
        if(ItemNBTHelper.getBoolean(card, "BooleanQuestion", false))
        {
            answerC.visible = false;
            answerD.visible = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if(button.id == 5)
        {
            player.sendMessage(new TextComponentString(player.getName() + " passed the question!"));
            this.mc.displayGuiScreen(null);
        }
        else
        {
            /**int answerID = ItemNBTHelper.getInt(card, "AnswerID", -1);
            if(answerID == -1)
            {
                player.sendStatusMessage(new TextComponentString("The button pressed is not a valid button! Discarding card"), false);
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
            }**/
            for(EnumHand hand : EnumHand.values())
            {
                if(player.getHeldItem(hand).isItemEqual(card))
                    Jeopardy.NETWORK.sendToServer(new AnswerHandlerMessage(button.id, hand));
            }
        }
    }
}
