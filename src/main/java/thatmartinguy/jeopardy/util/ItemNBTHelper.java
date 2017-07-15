package thatmartinguy.jeopardy.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class ItemNBTHelper
{
    public static NBTTagCompound getNBT(ItemStack itemStack)
    {
        NBTTagCompound compound;

        if(itemStack.hasTagCompound())
        {
            compound = itemStack.getTagCompound();
        }
        else
        {
            compound = new NBTTagCompound();
        }

        itemStack.setTagCompound(compound);
        return compound;
    }

    public static boolean hasKey(ItemStack itemStack, String key)
    {
        return getNBT(itemStack).hasKey(key);
    }

    public static void setString(ItemStack itemStack, String key, String value)
    {
        getNBT(itemStack).setString(key, value);
    }

    public static void setInt(ItemStack itemStack, String key, int value)
    {
        getNBT(itemStack).setInteger(key, value);
    }

    public static void setBoolean(ItemStack itemStack, String key, boolean value)
    {
        getNBT(itemStack).setBoolean(key, value);
    }

    public static void setBlockPos(ItemStack itemStack, String key, BlockPos value)
    {
        setInt(itemStack, key + "X", value.getX());
        setInt(itemStack, key + "Y", value.getY());
        setInt(itemStack, key + "Z", value.getZ());
    }

    public static String getString(ItemStack itemStack, String key)
    {
        return getString(itemStack, key, "");
    }

    public static String getString(ItemStack itemStack, String key, String defaultValue)
    {
        if(hasKey(itemStack, key))
        {
            return getNBT(itemStack).getString(key);
        }
        else
        {
            getNBT(itemStack).setString(key, defaultValue);
            return defaultValue;
        }
    }

    public static int getInt(ItemStack itemStack, String key, int defaultValue)
    {
        if(hasKey(itemStack, key))
        {
            return getNBT(itemStack).getInteger(key);
        }
        else
        {
            getNBT(itemStack).setInteger(key, defaultValue);
            return defaultValue;
        }
    }

    public static boolean getBoolean(ItemStack itemStack, String key, boolean defaultValue)
    {
        if(hasKey(itemStack, key))
        {
            return getNBT(itemStack).getBoolean(key);
        }
        else
        {
            getNBT(itemStack).setBoolean(key, defaultValue);
            return defaultValue;
        }
    }

    public static BlockPos getBlockPos(ItemStack itemStack, String key)
    {
        if(hasKey(itemStack, key + "X"))
        {
            return new BlockPos(getInt(itemStack, key + "X", 0), getInt(itemStack, key + "Y", 0), getInt(itemStack, key + "Z", 0));
        }
        else
        {
            return BlockPos.ORIGIN;
        }
    }
}
