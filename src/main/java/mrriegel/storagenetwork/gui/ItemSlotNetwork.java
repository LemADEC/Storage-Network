package mrriegel.storagenetwork.gui;

import javax.annotation.Nonnull;
import org.lwjgl.input.Keyboard;
import mrriegel.storagenetwork.util.UtilInventory;
import mrriegel.storagenetwork.util.data.StackWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

/**
 * used as the MAIN grid in the network item display
 * 
 * also as ghost/filter items in the cable filter slots
 * 
 * @author
 *
 */
public class ItemSlotNetwork {

  private int x, y, size, guiLeft, guiTop;
  private int slotIndex;
  private boolean showNumber;
  private Minecraft mc;
  private GuiContainerBase parent;
  private ItemStack stack;

  public ItemSlotNetwork(GuiContainerBase parent, @Nonnull ItemStack stack, int x, int y, int size, int guiLeft, int guiTop, boolean number) {
    this.x = x;
    this.y = y;
    this.setSize(size);
    this.guiLeft = guiLeft;
    this.guiTop = guiTop;
    this.setShowNumber(number);
    this.parent = parent;
    mc = Minecraft.getMinecraft();
    this.setStack(stack);
  }

  public boolean isMouseOverSlot(int mouseX, int mouseY) {
    return parent.isPointInRegion(x - guiLeft, y - guiTop, 16, 16, mouseX, mouseY);
  }

  public void drawSlot(int mx, int my) {
    GlStateManager.pushMatrix();
    //    if (this.slotIndex == 0) {
    //      StorageNetwork.log("draw slot zero " + getStack().getUnlocalizedName());
    //    } 
    if (!getStack().isEmpty()) {

      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemAndEffectIntoGUI(getStack(), x, y);
      String amount;
      //cant sneak in gui
      //default to short form, show full amount if sneak 
      if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        amount = getSize() + "";
      else
        amount = UtilInventory.formatLargeNumber(getSize());
      if (isShowNumber()) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(.5f, .5f, .5f);
        mc.getRenderItem().renderItemOverlayIntoGUI(parent.getFont(), stack, x * 2 + 16, y * 2 + 16, amount);
        GlStateManager.popMatrix();
      }
    }
    if (this.isMouseOverSlot(mx, my)) {
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      int j1 = x;
      int k1 = y;
      GlStateManager.colorMask(true, true, true, false);
      parent.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
      GlStateManager.colorMask(true, true, true, true);
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
    }
    GlStateManager.popMatrix();
  }

  public StackWrapper getAsStackWrapper() {
    StackWrapper wrap = new StackWrapper(stack, size);
    wrap.setShowNumber(isShowNumber());
    return wrap;
  }
  public void drawTooltip(int mx, int my) {
    if (this.isMouseOverSlot(mx, my) && !getStack().isEmpty()) {
      parent.renderToolTip(getStack(), mx, my);
    }
  }

  public ItemStack getStack() {
    return stack;
  }

  public void setStack(ItemStack stack) {
    this.stack = stack;
  }

  public boolean isShowNumber() {
    return showNumber;
  }

  public void setShowNumber(boolean showNumber) {
    this.showNumber = showNumber;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getSlotIndex() {
    return slotIndex;
  }

  public void setSlotIndex(int slotIndex) {
    this.slotIndex = slotIndex;
  }
}
