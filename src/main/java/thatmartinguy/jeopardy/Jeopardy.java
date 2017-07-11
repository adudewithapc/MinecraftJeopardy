package thatmartinguy.jeopardy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import thatmartinguy.jeopardy.init.ModBlocks;
import thatmartinguy.jeopardy.network.AnswerHandlerMessage;
import thatmartinguy.jeopardy.network.AnsweredMessage;
import thatmartinguy.jeopardy.network.CardWriteMessage;
import thatmartinguy.jeopardy.proxy.IProxy;
import thatmartinguy.jeopardy.util.Reference;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class Jeopardy
{
    public static final CreativeTabs TAB_QUIZ = new CreativeTabs("tabJeopardy")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Items.PAPER);
        }
    };

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModBlocks.registerTileEntities();

        int networkID = -1;

        NETWORK.registerMessage(CardWriteMessage.Handler.class, CardWriteMessage.class, networkID++, Side.SERVER);
        NETWORK.registerMessage(AnswerHandlerMessage.Handler.class, AnswerHandlerMessage.class, networkID++, Side.SERVER);
        NETWORK.registerMessage(AnsweredMessage.Handler.class, AnsweredMessage.class, networkID++, Side.CLIENT);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @Instance
    public static Jeopardy instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_LOCATION, serverSide = Reference.SERVER_PROXY_LOCATION)
    public static IProxy proxy;
}
