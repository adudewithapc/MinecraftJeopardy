package thatmartinguy.jeopardy.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy implements IProxy
{
    @Override
    public IThreadListener getThreadListener(MessageContext ctx)
    {
        if(ctx.side.isServer())
            return ctx.getServerHandler().player.mcServer;
        else
            throw new WrongSideException("Tried to get the minecraft client on the server");
    }

    @Override
    public EntityPlayer getPlayer(MessageContext ctx)
    {
        if(ctx.side.isServer())
            return ctx.getServerHandler().player;
        else
            throw new WrongSideException("Tried to get the client player on the server");
    }
}
