package thatmartinguy.jeopardy.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.init.ModItems;
import thatmartinguy.jeopardy.tileentity.TileEntityQuizBox;

import javax.annotation.Nullable;

public class BlockQuizBox extends Block
{
    public BlockQuizBox(String name, Material materialIn)
    {
        super(materialIn);
        this.setRegistryName(name);
        this.setUnlocalizedName();
        this.setCreativeTab(Jeopardy.TAB_QUIZ);
    }

    private void setUnlocalizedName()
    {
        super.setUnlocalizedName(getRegistryName().toString());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityQuizBox();
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (worldIn.getTileEntity(pos) instanceof TileEntityQuizBox)
        {
            TileEntityQuizBox entityQuizBox = (TileEntityQuizBox) worldIn.getTileEntity(pos);
            for(ItemStack itemStack : entityQuizBox.getInsertedCards())
            {
                InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemStack);
            }
            worldIn.removeTileEntity(pos);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.getTileEntity(pos) instanceof TileEntityQuizBox)
        {
            TileEntityQuizBox entityQuizBox = (TileEntityQuizBox) worldIn.getTileEntity(pos);
            ItemStack heldItem = playerIn.getHeldItem(hand);

            if(heldItem.getItem() == ModItems.itemQuizCard)
            {
                entityQuizBox.addCard(heldItem);
                heldItem.shrink(1);
            }
            else if(heldItem.isEmpty())
            {

            }

            return true;
        }
        return false;
    }
}
