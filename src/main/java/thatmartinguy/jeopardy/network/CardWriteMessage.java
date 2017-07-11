package thatmartinguy.jeopardy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.init.ModItems;
import thatmartinguy.jeopardy.util.ItemNBTHelper;

public class CardWriteMessage implements IMessage
{
    private EnumHand paperHand;
    private int correctAnswer;
    private boolean booleanQuestion;

    private String name;

    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;

    public CardWriteMessage() {}

    public CardWriteMessage(EnumHand paperHand, int correctAnswer, boolean booleanQuestion, String name, String question, String answerA, String answerB, String answerC, String answerD)
    {
        this.paperHand = paperHand;
        this.booleanQuestion = booleanQuestion;
        this.correctAnswer = correctAnswer;
        this.name = name;
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        paperHand = EnumHand.values()[buf.readInt()];
        correctAnswer = buf.readInt();
        booleanQuestion = buf.readBoolean();

        name = ByteBufUtils.readUTF8String(buf);

        question = ByteBufUtils.readUTF8String(buf);

        answerA = ByteBufUtils.readUTF8String(buf);
        answerB = ByteBufUtils.readUTF8String(buf);
        answerC = ByteBufUtils.readUTF8String(buf);
        answerD = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(paperHand.ordinal());
        buf.writeInt(correctAnswer);
        buf.writeBoolean(booleanQuestion);

        ByteBufUtils.writeUTF8String(buf, name);

        ByteBufUtils.writeUTF8String(buf, question);

        ByteBufUtils.writeUTF8String(buf, answerA);
        ByteBufUtils.writeUTF8String(buf, answerB);
        ByteBufUtils.writeUTF8String(buf, answerC);
        ByteBufUtils.writeUTF8String(buf, answerD);
    }

    public static class Handler implements IMessageHandler<CardWriteMessage, IMessage>
    {
        @Override
        public IMessage onMessage(CardWriteMessage message, MessageContext ctx)
        {
            Jeopardy.proxy.getThreadListener(ctx).addScheduledTask(() ->
            {
               EntityPlayer player = Jeopardy.proxy.getPlayer(ctx);
               if(player.getHeldItem(message.paperHand).getItem() == ModItems.itemPaperAndQuill)
               {
                   player.getHeldItem(message.paperHand).shrink(1);
                   ItemStack card = new ItemStack(ModItems.itemQuizCard);

                   ItemNBTHelper.setInt(card, "AnswerID", message.correctAnswer);
                   ItemNBTHelper.setBoolean(card, "BooleanQuestion", message.booleanQuestion);
                   ItemNBTHelper.setString(card, "Author", message.name);

                   ItemNBTHelper.setString(card, "Question", message.question);

                   ItemNBTHelper.setString(card, "AnswerA", message.answerA);
                   ItemNBTHelper.setString(card, "AnswerB", message.answerB);
                   ItemNBTHelper.setString(card, "AnswerC", message.answerC);
                   ItemNBTHelper.setString(card, "AnswerD", message.answerD);

                   player.addItemStackToInventory(card);
               }
            });

            return null;
        }
    }
}
