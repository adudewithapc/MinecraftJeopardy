package thatmartinguy.jeopardy.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import thatmartinguy.jeopardy.client.gui.GuiScreenCard;
import thatmartinguy.jeopardy.data.ModWorldData;
import thatmartinguy.jeopardy.init.ModBlocks;
import thatmartinguy.jeopardy.tileentity.TileEntityQuizBox;
import thatmartinguy.jeopardy.util.ItemNBTHelper;

public class ItemMic extends ItemBase
{
    public ItemMic(String name)
    {
        super(name);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.getBlockState(pos).getBlock() == ModBlocks.blockQuizBox && worldIn.getTileEntity(pos) instanceof TileEntityQuizBox && !worldIn.isRemote)
        {
            TileEntityQuizBox quizBox = (TileEntityQuizBox) worldIn.getTileEntity(pos);
            ItemStack usedItem = player.getHeldItem(hand);

            ItemNBTHelper.setInt(usedItem, "PosX", pos.getX());
            ItemNBTHelper.setInt(usedItem, "PosY", pos.getY());
            ItemNBTHelper.setInt(usedItem, "PosZ", pos.getZ());
            ItemNBTHelper.setBoolean(usedItem, "Bound", true);

            player.sendMessage(new TextComponentString(player.getName() + " used his microphone on " + quizBox.getPlacerName() + "'s quiz box!"));

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack usedItem = playerIn.getHeldItem(handIn);
        ModWorldData worldData = ModWorldData.get(worldIn);
        BlockPos boxPosition = new BlockPos(ItemNBTHelper.getInt(usedItem, "PosX", -1), ItemNBTHelper.getInt(usedItem, "PosY", -1), ItemNBTHelper.getInt(usedItem, "PosZ", -1));


        if (worldIn.getTileEntity(boxPosition) instanceof TileEntityQuizBox)
        {
            TileEntityQuizBox quizBox = (TileEntityQuizBox) worldIn.getTileEntity(boxPosition);

            if(ItemNBTHelper.getBoolean(usedItem, "Bound", false) && !worldData.isQuizStarting())
            {
                ItemNBTHelper.setBoolean(usedItem, "OwnerStarting", true);

                if(worldIn.isRemote)
                {
                    for(EntityPlayer otherPlayer : worldIn.playerEntities)
                    {
                        otherPlayer.sendMessage(new TextComponentString(playerIn.getName() + " is starting a game of Jeopardy! The questions are from " + quizBox.getPlacerName() + "'s quiz box. Use your microphone to join"));
                    }
                    playerIn.sendMessage(new TextComponentString("Use your microphone when you want to begin"));
                }
            }
            else if(worldData.isQuizStarting())
            {

            }
            else if(ItemNBTHelper.getBoolean(usedItem, "OwnerStarting", false))
            {
                ItemStack randomedCard = quizBox.getRandomCard();
                EntityPlayer martin = worldIn.getPlayerEntityByName("That_Martin_Guy");

                FMLClientHandler.instance().displayGuiScreen(martin, new GuiScreenCard(martin, randomedCard));
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, usedItem);
    }
}
