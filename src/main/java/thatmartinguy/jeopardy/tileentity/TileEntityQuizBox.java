package thatmartinguy.jeopardy.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class TileEntityQuizBox extends TileEntityLockableLoot
{
    private final Random RNG = new Random();
    private NonNullList<ItemStack> insertedCards = NonNullList.withSize(27, ItemStack.EMPTY);

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        this.insertedCards = NonNullList.withSize(27, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, insertedCards);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        ItemStackHelper.saveAllItems(compound, insertedCards);

        return compound;
    }

    public void giveRandomCard()
    {
        int currentSlot = -1;
        int nextSlot = 1;

        for(ItemStack itemStack : insertedCards)
        {
            if(!itemStack.isEmpty() && RNG.nextInt(nextSlot) == 0)
            {
                currentSlot = nextSlot;
            }
        }

        InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), insertedCards.get(currentSlot));
        insertedCards.remove(currentSlot);
    }

    public int addCard(ItemStack itemStack)
    {
        for(int i = 0; i < insertedCards.size(); i++)
        {
            if(insertedCards.get(i).isEmpty())
            {
                this.setInventorySlotContents(i, itemStack);
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean isEmpty()
    {
        for(ItemStack itemStack : insertedCards)
        {
            if(!itemStack.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        this.fillWithLoot(playerIn);
        return new ContainerChest(playerInventory, this, playerIn);
    }

    @Override
    public String getGuiID()
    {
        return "Quiz Box";
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public int getSizeInventory()
    {
        return 27;
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return insertedCards;
    }

    public NonNullList<ItemStack> getInsertedCards()
    {
        return getItems();
    }

    @Override
    public void setLootTable(ResourceLocation p_189404_1_, long p_189404_2_) {}

    @Override
    public ResourceLocation getLootTable()
    {
        return new ResourceLocation("");
    }
}