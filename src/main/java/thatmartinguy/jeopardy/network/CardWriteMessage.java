package thatmartinguy.jeopardy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CardWriteMessage implements IMessage
{
    private EntityPlayer player;
    private ItemStack paper;
    private ItemStack card;

    public CardWriteMessage() {}

    public CardWriteMessage(EntityPlayer player, ItemStack paper, ItemStack card)
    {
        this.player = player;
        this.paper = paper;
        this.card = card;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {

    }

    @Override
    public void toBytes(ByteBuf buf)
    {

    }
}
