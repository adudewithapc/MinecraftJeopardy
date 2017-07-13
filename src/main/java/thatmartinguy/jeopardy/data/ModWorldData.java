package thatmartinguy.jeopardy.data;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import thatmartinguy.jeopardy.Jeopardy;
import thatmartinguy.jeopardy.network.QuizStartingMessage;
import thatmartinguy.jeopardy.util.Reference;

import java.util.ArrayList;
import java.util.List;

public class ModWorldData extends WorldSavedData
{
    private static final String IDENTIFIER = Reference.MOD_ID;

    private boolean quizStarting;
    private List<String> contestantNames = new ArrayList<>();

    private World world;
    private static ModWorldData instance;

    public ModWorldData()
    {
        super(IDENTIFIER);
    }

    public void setQuizStarting(boolean quizStarting)
    {
        this.quizStarting = quizStarting;

        if(!world.isRemote)
        {
            Jeopardy.NETWORK.sendToAll(new QuizStartingMessage(this.quizStarting));
        }

        this.markDirty();
    }

    public boolean isQuizStarting()
    {
        return quizStarting;
    }

    public void addContestant(String name)
    {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("QuizStarting", quizStarting);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        quizStarting = nbt.getBoolean("QuizStarting");
    }

    public static ModWorldData get(World world)
    {
        ModWorldData worldData;

        if(!world.isRemote && instance != null)
        {
            worldData = instance;
        }
        else
        {
            worldData = (ModWorldData) world.loadData(ModWorldData.class, IDENTIFIER);

            if(worldData == null)
            {
                worldData = new ModWorldData();
            }
            if(worldData.world == null)
            {
                worldData.world = null;
            }
        }

        world.setData(IDENTIFIER, worldData);

        return worldData;
    }

    @EventBusSubscriber
    private static class EventHandler
    {
        private static void sendToPlayer(EntityPlayerMP player)
        {
            final ModWorldData worldData = ModWorldData.get(player.world);
            Jeopardy.NETWORK.sendTo(new QuizStartingMessage(worldData.quizStarting), player);
        }

        @SubscribeEvent
        public static void syncPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
        {
            if (event.player instanceof EntityPlayerMP)
            {
                sendToPlayer((EntityPlayerMP) event.player);
            }
        }

        @SubscribeEvent
        public static void syncPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event)
        {
            if(event.player instanceof EntityPlayerMP)
            {
                sendToPlayer((EntityPlayerMP) event.player);
            }
        }
    }
}