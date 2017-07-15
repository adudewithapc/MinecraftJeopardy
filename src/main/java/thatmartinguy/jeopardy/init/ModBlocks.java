package thatmartinguy.jeopardy.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thatmartinguy.jeopardy.block.BlockMicStand;
import thatmartinguy.jeopardy.block.BlockQuizBox;
import thatmartinguy.jeopardy.tileentity.TileEntityMicStand;
import thatmartinguy.jeopardy.tileentity.TileEntityQuizBox;

@EventBusSubscriber
public class ModBlocks
{
    public static Block blockQuizBox;
    public static Block blockMicStand;

    public static final Block[] BLOCKS = {
            blockQuizBox = new BlockQuizBox("blockQuizBox", Material.ROCK),
            blockMicStand = new BlockMicStand("blockMicStand", Material.ROCK)
    };

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(BLOCKS);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event)
    {
        for(Block block : BLOCKS)
        {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityQuizBox.class, "tileEntityQuizBox");
        GameRegistry.registerTileEntity(TileEntityMicStand.class, "tileEntityMicStand");
    }
}
