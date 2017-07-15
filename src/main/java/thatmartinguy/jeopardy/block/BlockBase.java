package thatmartinguy.jeopardy.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import thatmartinguy.jeopardy.Jeopardy;

public class BlockBase extends Block
{
    public BlockBase(String name, Material materialIn)
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
}
