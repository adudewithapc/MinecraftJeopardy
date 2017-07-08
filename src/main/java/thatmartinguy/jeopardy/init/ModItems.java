package thatmartinguy.jeopardy.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thatmartinguy.jeopardy.item.*;

@EventBusSubscriber
public class ModItems
{
    public static Item itemPaperAndQuill;
    public static Item itemQuizCard;

    public static final Item[] ITEMS = {
            itemPaperAndQuill = new ItemPaperAndQuill("itemPaperAndQuill"),
            itemQuizCard = new ItemQuizCard("itemQuizCard")
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ITEMS);
    }
}
