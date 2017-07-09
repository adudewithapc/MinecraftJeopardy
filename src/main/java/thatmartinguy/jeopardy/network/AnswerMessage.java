package thatmartinguy.jeopardy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thatmartinguy.jeopardy.Jeopardy;

@SuppressWarnings("unused")
public class AnswerMessage implements IMessage
{
    private int answerID;
    private ItemStack card;

    public AnswerMessage() {}

    public AnswerMessage(int answerID, ItemStack card)
    {
        this.answerID = answerID;
        this.card = card;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        answerID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(answerID);
    }

    public static class Handler implements IMessageHandler<AnswerMessage, IMessage>
    {
        @Override
        public IMessage onMessage(AnswerMessage message, MessageContext ctx)
        {
            Jeopardy.proxy.getThreadListener(ctx).addScheduledTask(() ->
            {

            });

            return null;
        }
    }
}
