package thatmartinguy.jeopardy.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import thatmartinguy.jeopardy.init.ModItems;

import javax.annotation.Nullable;

public class ModGuiHandler implements IGuiHandler
{
    public static final int QUIZ_CARD_EDITOR = 0;
    public static final int QUIZ_CARD = 1;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case QUIZ_CARD_EDITOR:
                if(player.getHeldItemMainhand().getItem() == ModItems.itemPaperAndQuill)
                    return new GuiCardEditor(player, player.getHeldItemMainhand());
                else
                    return new GuiCardEditor(player, player.getHeldItemOffhand());
            case QUIZ_CARD:
                if(player.getHeldItemMainhand().getItem() == ModItems.itemQuizCard)
                    return new GuiScreenCard(player, player.getHeldItemMainhand());
                else
                    return new GuiScreenCard(player, player.getHeldItemOffhand());
        }

        return null;
    }
}
