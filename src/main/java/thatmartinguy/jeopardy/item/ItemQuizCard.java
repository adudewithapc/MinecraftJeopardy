package thatmartinguy.jeopardy.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.client.gui.GuiScreenCard;
import thatmartinguy.jeopardy.util.ItemNBTHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ItemQuizCard extends Item
{
    public ItemQuizCard(String name)
    {
        this.setRegistryName(name);
        this.setUnlocalizedName();
        this.setCreativeTab(Jeopardy.TAB_QUIZ);
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if(ItemNBTHelper.getString(stack, "Author", "") != "")
        {
            tooltip.add("Created by " + ItemNBTHelper.getString(stack, "Author", ""));
        }
        else
        {
            tooltip.add(TextFormatting.RED + "This card was spawned in and is invalid. Do not use.");
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        FMLClientHandler.instance().displayGuiScreen(playerIn, new GuiScreenCard(playerIn, playerIn.getHeldItem(handIn)));

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if(!ItemNBTHelper.getString(stack, "Question", "").equals(""))
            return ItemNBTHelper.getString(stack, "Question", "");
        else
            return TextFormatting.RED + "ERROR";
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack)
    {
        NBTTagCompound clientNBT = stack.getTagCompound().copy();
        clientNBT.removeTag("AnswerID");

        return clientNBT;
    }

    public Item setUnlocalizedName()
    {
        return super.setUnlocalizedName(this.getRegistryName().toString());
    }
}
