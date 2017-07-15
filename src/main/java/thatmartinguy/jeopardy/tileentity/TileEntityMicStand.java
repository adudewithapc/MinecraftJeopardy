package thatmartinguy.jeopardy.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityMicStand extends TileEntity
{
    private BlockPos boxPos = BlockPos.ORIGIN;

    public void setBoxPos(BlockPos pos)
    {
        this.boxPos = pos;
        this.markDirty();
    }

    public BlockPos getBoxPos()
    {
        return this.boxPos;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setInteger("BoxPosX", boxPos.getX());
        compound.setInteger("BoxPosY", boxPos.getY());
        compound.setInteger("BoxPosZ", boxPos.getZ());

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        int x = compound.getInteger("BoxPosX");
        int y = compound.getInteger("BoxPosY");
        int z = compound.getInteger("BoxPosZ");

        boxPos = new BlockPos(x, y, z);
    }
}
