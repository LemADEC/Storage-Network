package mrriegel.storagenetwork.network;

import java.util.ArrayList;
import java.util.List;
import io.netty.buffer.ByteBuf;
import mrriegel.storagenetwork.block.master.TileMaster;
import mrriegel.storagenetwork.gui.ContainerNetworkBase;
import mrriegel.storagenetwork.registry.PacketRegistry;
import mrriegel.storagenetwork.util.data.StackWrapper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.ItemHandlerHelper;

public class ClearRecipeMessage implements IMessage, IMessageHandler<ClearRecipeMessage, IMessage> {

  @Override
  public IMessage onMessage(final ClearRecipeMessage message, final MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().player;
    IThreadListener mainThread = (WorldServer) player.world;
    mainThread.addScheduledTask(new Runnable() {

      @Override
      public void run() {
        if (player.openContainer instanceof ContainerNetworkBase) {
          ContainerNetworkBase container = (ContainerNetworkBase) player.openContainer;
          InventoryCrafting craftMatrix = container.getCraftMatrix();
          TileMaster tileMaster = container.getTileMaster();
          for (int i = 0; i < 9; i++) {
            if (tileMaster == null) {
              break;
            }
            ItemStack stackInSlot = craftMatrix.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
              continue;
            }
            int numBeforeInsert = stackInSlot.getCount();
            int remainingAfter = tileMaster.insertStack(stackInSlot.copy(), null, false);
            if (numBeforeInsert == remainingAfter) {
              continue;
            }
            if (remainingAfter == 0)
              craftMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
            else
              craftMatrix.setInventorySlotContents(i, ItemHandlerHelper.copyStackWithSize(stackInSlot, remainingAfter));
          }
          List<StackWrapper> list = tileMaster.getStacks();
          PacketRegistry.INSTANCE.sendTo(new StackRefreshClientMessage(list, new ArrayList<StackWrapper>()), player);
          container.detectAndSendChanges();
        }
      }
    });
    return null;
  }

  @Override
  public void fromBytes(ByteBuf buf) {}

  @Override
  public void toBytes(ByteBuf buf) {}
}
