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
    private NonNullList<ItemStack> cards = NonNullList.create();

    public void addCard(ItemStack card)
    {
        cards.add(card);
        card.shrink(1);
        this.markDirty();
    }

    public ItemStack getRandomCard()
    {
        Random random = new Random();

        ItemStack randomedCard = cards.get(random.nextInt(cards.size()));
        cards.remove(randomedCard);

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
        return cards.size();
    }
}
