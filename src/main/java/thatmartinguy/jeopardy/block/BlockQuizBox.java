package thatmartinguy.jeopardy.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import thatmartinguy.jeopardy.init.ModItems;
import thatmartinguy.jeopardy.tileentity.TileEntityQuizBox;
import thatmartinguy.jeopardy.util.ItemNBTHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockQuizBox extends BlockBase
{
    public BlockQuizBox(String name, Material materialIn)
    {
        super(name, materialIn);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (placer instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) placer;
            if (worldIn.getTileEntity(pos) instanceof TileEntityQuizBox)
            {
                TileEntityQuizBox entityQuizBox = (TileEntityQuizBox) worldIn.getTileEntity(pos);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.getTileEntity(pos) instanceof TileEntityQuizBox)
        {
            TileEntityQuizBox entityQuizBox = (TileEntityQuizBox) worldIn.getTileEntity(pos);

            ItemStack usedItem = playerIn.getHeldItem(hand);

            if(usedItem.getItem() == ModItems.itemQuizCard && !ItemNBTHelper.getString(usedItem, "Author").isEmpty())
            {
                entityQuizBox.addCard(usedItem);
                usedItem.shrink(1);

                return true;
            }
            else if(usedItem.isEmpty())
            {
                if(entityQuizBox.getCardAmount() > 0)
                {
                    playerIn.setHeldItem(EnumHand.MAIN_HAND, entityQuizBox.getRandomCard());
                }
                else
                {
                    if(worldIn.isRemote)
                        playerIn.sendMessage(new TextComponentString("The box doesn't contain any cards"));
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (worldIn.getTileEntity(pos) instanceof TileEntityQuizBox)
        {
            TileEntityQuizBox quizBox = (TileEntityQuizBox) worldIn.getTileEntity(pos);
            List<ItemStack> cards = quizBox.getCards();

            for(ItemStack card : cards)
                InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), card);

            worldIn.removeTileEntity(pos);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityQuizBox();
    }

    private void setUnlocalizedName()
    {
        super.setUnlocalizedName(getRegistryName().toString());
    }
}
