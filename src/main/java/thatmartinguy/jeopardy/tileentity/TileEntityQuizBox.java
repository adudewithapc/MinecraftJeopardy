package thatmartinguy.jeopardy.tileentity;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import thatmartinguy.jeopardy.util.LogHelper;

import java.util.Random;

public class TileEntityQuizBox extends TileEntity
{
    private NonNullList<ItemStack> cards = NonNullList.withSize(27, ItemStack.EMPTY);

    public void addCard(ItemStack card)
    {
        for(int i = 0; i < cards.size(); i++)
        {
            if(cards.get(i) == ItemStack.EMPTY)
            {
                cards.set(i, card);
            }
        }
        this.markDirty();
    }

    public ItemStack getRandomCard()
    {
        Random random = new Random();

        int randomedIndex;

        ItemStack randomedCard;

        do
        {
            randomedIndex = random.nextInt(cards.size());
            randomedCard = cards.get(randomedIndex);
        } while(randomedCard == ItemStack.EMPTY);

        cards.set(randomedIndex, ItemStack.EMPTY);

        this.markDirty();
        return randomedCard;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        ItemStackHelper.saveAllItems(compound, cards);
        for(ItemStack card : cards)
            LogHelper.info(card.toString());

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        ItemStackHelper.loadAllItems(compound, cards);
        for(ItemStack card : cards)
            LogHelper.info(card.toString());

    }

    public int getCardAmount()
    {
        int i = 0;
        while(cards.get(i) == ItemStack.EMPTY)
        {
            i++;
        }
        return i;
    }
}
