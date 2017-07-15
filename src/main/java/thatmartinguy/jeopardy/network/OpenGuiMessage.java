package thatmartinguy.jeopardy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.util.LogHelper;

public class OpenGuiMessage implements IMessage
{
    private int guiID = -1;

    public OpenGuiMessage() {}

    public OpenGuiMessage(int id)
    {
        this.guiID = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        guiID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(guiID);
    }

    public static class Handler implements IMessageHandler<OpenGuiMessage, IMessage>
    {
        @Override
        public IMessage onMessage(OpenGuiMessage message, MessageContext ctx)
        {
            Jeopardy.proxy.getThreadListener(ctx).addScheduledTask(() ->
            {
               final EntityPlayer player = Jeopardy.proxy.getPlayer(ctx);
               player.openGui(Jeopardy.instance, message.guiID, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
            });

            return null;
        }
    }
}
