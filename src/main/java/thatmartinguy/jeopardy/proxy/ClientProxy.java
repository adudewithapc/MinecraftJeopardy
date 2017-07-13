package thatmartinguy.jeopardy.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy implements IProxy
{
    private final Minecraft MINECRAFT = Minecraft.getMinecraft();

    @Override
    public IThreadListener getThreadListener(final MessageContext ctx)
    {
        if(ctx.side.isClient())
            return MINECRAFT;
        else
            return ctx.getServerHandler().player.mcServer;
    }

    @Override
    public EntityPlayer getPlayer(MessageContext ctx)
    {
        if(ctx.side.isClient())
            return MINECRAFT.player;
        else
            return ctx.getServerHandler().player;
    }

    @Override
    public World getWorld(MessageContext ctx)
    {
        if(ctx.side.isClient())
            return MINECRAFT.world;
        else
            return ctx.getServerHandler().player.world;
    }
}
