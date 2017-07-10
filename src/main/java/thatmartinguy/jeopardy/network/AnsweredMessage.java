package thatmartinguy.jeopardy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thatmartinguy.jeopardy.Jeopardy;

public class AnsweredMessage implements IMessage
{
    private boolean correctAnswer;

    public AnsweredMessage() {}

    public AnsweredMessage(boolean correctAnswer)
    {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        correctAnswer = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(correctAnswer);
    }

    public static class Handler implements IMessageHandler<AnsweredMessage, IMessage>
    {
        @Override
        public IMessage onMessage(AnsweredMessage message, MessageContext ctx)
        {
            Jeopardy.proxy.getThreadListener(ctx).addScheduledTask(() ->
            {
               if(message.correctAnswer)
               {
                   //TODO: Reward player
                   FMLClientHandler.instance().displayGuiScreen(Jeopardy.proxy.getPlayer(ctx), null);
               }
               else
               {
                   Jeopardy.proxy.getPlayer(ctx).sendMessage(new TextComponentString("Wrong answer"));
                   FMLClientHandler.instance().displayGuiScreen(Jeopardy.proxy.getPlayer(ctx), null);
               }
            });

            return null;
        }
    }
}
