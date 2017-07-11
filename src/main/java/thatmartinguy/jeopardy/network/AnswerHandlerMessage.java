package thatmartinguy.jeopardy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.init.ModItems;
import thatmartinguy.jeopardy.util.ItemNBTHelper;

public class AnswerHandlerMessage implements IMessage
{
    private int attemptID;
    private EnumHand cardHand;

    public AnswerHandlerMessage() {}

    public AnswerHandlerMessage(int attemptID, EnumHand cardHand)
    {
        this.attemptID = attemptID;
        this.cardHand = cardHand;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        attemptID = buf.readInt();
        cardHand = EnumHand.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(attemptID);
        buf.writeInt(cardHand.ordinal());
    }

    public static class Handler implements IMessageHandler<AnswerHandlerMessage, IMessage>
    {
        @Override
        public IMessage onMessage(AnswerHandlerMessage message, MessageContext ctx)
        {
            Jeopardy.proxy.getThreadListener(ctx).addScheduledTask(() ->
            {
                EntityPlayer player = Jeopardy.proxy.getPlayer(ctx);
                ItemStack heldItem = player.getHeldItem(message.cardHand);

                if(heldItem.getItem() == ModItems.itemQuizCard)
                {
                    if(ItemNBTHelper.getInt(heldItem, "AnswerID", -1) == message.attemptID)
                    {
                        Jeopardy.NETWORK.sendTo(new AnsweredMessage(true), (EntityPlayerMP) player);
                        heldItem.shrink(1);
                        player.addItemStackToInventory(new ItemStack(Items.DIAMOND));
                    }
                    else
                    {
                        Jeopardy.NETWORK.sendTo(new AnsweredMessage(false), (EntityPlayerMP) player);
                        heldItem.shrink(1);
                        player.sendMessage(new TextComponentString("Wrong answer"));
                    }
                }
            });

            return null;
        }
    }
}
