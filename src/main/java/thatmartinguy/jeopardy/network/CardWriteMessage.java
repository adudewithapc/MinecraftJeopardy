package thatmartinguy.jeopardy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thatmartinguy.jeopardy.Jeopardy;

public class CardWriteMessage implements IMessage
{
    private ItemStack paper;
    private ItemStack card;

    public CardWriteMessage() {}

    public CardWriteMessage(ItemStack paper, ItemStack card)
    {
        this.paper = paper;
        this.card = card;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        paper = ByteBufUtils.readItemStack(buf);
        card = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeItemStack(buf, paper);
        ByteBufUtils.writeItemStack(buf, card);
    }

    public static class Handler implements IMessageHandler<CardWriteMessage, IMessage>
    {
        @Override
        public IMessage onMessage(CardWriteMessage message, MessageContext ctx)
        {
            Jeopardy.proxy.getThreadListener(ctx).addScheduledTask(() ->
            {
               EntityPlayer player = Jeopardy.proxy.getPlayer(ctx);

               for(ItemStack itemStack : player.inventoryContainer.inventoryItemStacks)
               {
                   if(itemStack == message.paper)
                       itemStack.shrink(1);
               }
               player.addItemStackToInventory(message.card);
            });
            return null;
        }
    }
}
