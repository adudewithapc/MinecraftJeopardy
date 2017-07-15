package thatmartinguy.jeopardy.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import thatmartinguy.jeopardy.init.ModBlocks;
import thatmartinguy.jeopardy.tileentity.TileEntityMicStand;
import thatmartinguy.jeopardy.tileentity.TileEntityQuizBox;
import thatmartinguy.jeopardy.util.ItemNBTHelper;

public class ItemMicrophone extends ItemBase
{
    public ItemMicrophone(String name)
    {
        super(name);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack usedMicrophone = player.getHeldItem(hand);

        if(worldIn.getBlockState(pos).getBlock() == ModBlocks.blockQuizBox && worldIn.getTileEntity(pos) instanceof TileEntityQuizBox)
        {
            ItemNBTHelper.setBlockPos(usedMicrophone, "BoxPos", pos);

            if(!worldIn.isRemote)
                player.sendMessage(new TextComponentString("Marking this box as source"));
        }

        if(worldIn.getBlockState(pos).getBlock() == ModBlocks.blockMicStand && worldIn.getTileEntity(pos) instanceof TileEntityMicStand)
        {
            TileEntityMicStand entityMicStand = (TileEntityMicStand) worldIn.getTileEntity(pos);

            if(!ItemNBTHelper.getBlockPos(usedMicrophone, "BoxPos").equals(BlockPos.ORIGIN))
            {
                entityMicStand.setBoxPos(ItemNBTHelper.getBlockPos(usedMicrophone, "BoxPos"));
                if(!worldIn.isRemote)
                    player.sendMessage(new TextComponentString("Stand is now linked to quiz box!"));
            }
            else if(!worldIn.isRemote)
            {
                player.sendMessage(new TextComponentString("You have no source bound to the microphone"));
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
