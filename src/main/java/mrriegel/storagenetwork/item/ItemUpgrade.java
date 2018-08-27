package mrriegel.storagenetwork.item;

import java.util.List;
import mrriegel.storagenetwork.CreativeTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item {

  public static final int NUM = 4;
  public static final int SPEED = 0;
  public static final int OPERATION = 1;
  public static final int STACK = 2;
  public static final int STOCK = 3;

  public ItemUpgrade() {
    super();
    this.setCreativeTab(CreativeTab.tab);
    this.setHasSubtypes(true);
    this.setRegistryName("upgrade");
    this.setUnlocalizedName(getRegistryName().toString());
    this.setMaxStackSize(64);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
    if (isInCreativeTab(tab)) {
      for (int i = 0; i < NUM; i++) {
        list.add(new ItemStack(this, 1, i));
      }
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {
    return this.getUnlocalizedName() + "_" + stack.getItemDamage();
  }

  @Override
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
    super.addInformation(stack, playerIn, tooltip, advanced);
    tooltip.add(I18n.format("tooltip.storagenetwork.upgrade_" + stack.getItemDamage()));
  }
}
