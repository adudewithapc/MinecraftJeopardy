package thatmartinguy.jeopardy.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.client.gui.GuiCardEditor;

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

        if(world.isRemote)
            FMLClientHandler.instance().displayGuiScreen(player, new GuiCardEditor(player, heldItem));

        return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
    }

    public void setUnlocalizedName()
    {
        super.setUnlocalizedName(this.getRegistryName().toString());
    }
}
