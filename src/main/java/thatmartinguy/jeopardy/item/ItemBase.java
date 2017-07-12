package thatmartinguy.jeopardy.item;

import net.minecraft.item.Item;
import thatmartinguy.jeopardy.Jeopardy;

public class ItemBase extends Item
{
    public ItemBase(String name)
    {
        this.setRegistryName(name);
        this.setCreativeTab(Jeopardy.TAB_QUIZ);
    }

    private void setUnlocalizedName()
    {
        super.setUnlocalizedName(this.getRegistryName().toString());
    }
}
