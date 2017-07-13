package thatmartinguy.jeopardy.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy
{
    public IThreadListener getThreadListener(final MessageContext ctx);

    public EntityPlayer getPlayer(final MessageContext ctx);

    public World getWorld(final MessageContext ctx);

    class WrongSideException extends RuntimeException
    {
        public WrongSideException(final String message)
        {
            super(message);
        }

        public WrongSideException(final String message, final Throwable cause)
        {
            super(message, cause);
        }
    }
}
