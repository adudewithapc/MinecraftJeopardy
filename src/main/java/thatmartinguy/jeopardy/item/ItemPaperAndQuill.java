package thatmartinguy.jeopardy.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.client.gui.ModGuiHandler;
import thatmartinguy.jeopardy.network.OpenGuiMessage;

public class ItemPaperAndQuill extends ItemBase
{
    public ItemPaperAndQuill(String name)
    {
        super(name);
        this.setCreativeTab(Jeopardy.TAB_QUIZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack heldItem = player.getHeldItem(hand);

        if(player instanceof EntityPlayerMP)
        {
            Jeopardy.NETWORK.sendTo(new OpenGuiMessage(ModGuiHandler.QUIZ_CARD_EDITOR), (EntityPlayerMP) player);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
    }

}