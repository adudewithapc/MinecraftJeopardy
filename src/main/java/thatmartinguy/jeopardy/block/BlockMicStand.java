package thatmartinguy.jeopardy.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thatmartinguy.jeopardy.tileentity.TileEntityMicStand;

import javax.annotation.Nullable;

public class BlockMicStand extends BlockBase
{
    public BlockMicStand(String name, Material materialIn)
    {
        super(name, materialIn);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityMicStand();
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
}