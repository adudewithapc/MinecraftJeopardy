package thatmartinguy.jeopardy.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.data.ModWorldData;

public class QuizStartingMessage implements IMessage
{
    private boolean quizStarting;

    public QuizStartingMessage() {}

    public QuizStartingMessage(boolean quizStarting)
    {
        this.quizStarting = quizStarting;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        quizStarting = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(quizStarting);
    }

    public static class Handler implements IMessageHandler<QuizStartingMessage, IMessage>
    {
        @Override
        public IMessage onMessage(QuizStartingMessage message, MessageContext ctx)
        {
            Jeopardy.proxy.getThreadListener(ctx).addScheduledTask(() ->
            {
                ModWorldData worldData = ModWorldData.get(Jeopardy.proxy.getWorld(ctx));
                worldData.setQuizStarting(message.quizStarting);
            });

            return null;
        }
    }
}
