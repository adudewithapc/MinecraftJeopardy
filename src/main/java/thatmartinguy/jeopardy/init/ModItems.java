package thatmartinguy.jeopardy.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thatmartinguy.jeopardy.item.ItemMicrophone;
import thatmartinguy.jeopardy.item.ItemPaperAndQuill;
import thatmartinguy.jeopardy.item.ItemQuizCard;

@EventBusSubscriber
public class ModItems
{
    public static Item itemPaperAndQuill;
    public static Item itemQuizCard;
    public static Item itemMicrophone;

    public static final Item[] ITEMS = {
            itemPaperAndQuill = new ItemPaperAndQuill("itemPaperAndQuill"),
            itemQuizCard = new ItemQuizCard("itemQuizCard"),
            itemMicrophone = new ItemMicrophone("itemMic")
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ITEMS);
    }
}
