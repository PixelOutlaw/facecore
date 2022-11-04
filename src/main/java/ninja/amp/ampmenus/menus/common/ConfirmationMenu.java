package ninja.amp.ampmenus.menus.common;

import com.tealcube.minecraft.bukkit.facecore.FacecorePlugin;
import com.tealcube.minecraft.bukkit.facecore.utilities.TextUtils;
import io.pixeloutlaw.minecraft.spigot.garbage.StringExtensionsKt;
import io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt;
import java.util.List;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ConfirmationMenu extends ItemMenu {

  public ConfirmationMenu(String menuTitle,
      String confirmTitle, List<String> confirmLore,
      String cancelTitle, List<String> cancelLore,
      boolean closeOnConfirm, boolean closeOnCancel,
      Runnable confirm, Runnable cancel) {
    super(StringExtensionsKt.chatColorize("&f誒&0" + menuTitle), Size.fit(26), FacecorePlugin.getInstance());

    ItemStack confirmStack = new ItemStack(Material.BARRIER);
    ItemStackExtensionsKt.setCustomModelData(confirmStack, 50);
    ItemStackExtensionsKt.setDisplayName(confirmStack, confirmTitle);
    TextUtils.setLore(confirmStack, confirmLore, false);

    ItemStack cancelStack = new ItemStack(Material.BARRIER);
    ItemStackExtensionsKt.setCustomModelData(cancelStack, 50);
    ItemStackExtensionsKt.setDisplayName(cancelStack, cancelTitle);
    TextUtils.setLore(cancelStack, cancelLore, false);

    setItem(10, new ActionIcon(confirmStack, closeOnConfirm, confirm));
    setItem(11, new ActionIcon(confirmStack, closeOnConfirm, confirm));
    setItem(12, new ActionIcon(confirmStack, closeOnConfirm, confirm));

    setItem(14, new ActionIcon(cancelStack, closeOnCancel, cancel));
    setItem(15, new ActionIcon(cancelStack, closeOnCancel, cancel));
    setItem(16, new ActionIcon(cancelStack, closeOnCancel, cancel));
  }
}

/*
00 01 02 03 04 05 06 07 08
09 10 11 12 13 14 15 16 17
18 19 20 21 22 23 24 25 26
27 28 29 30 31 32 33 34 35
36 37 38 39 40 41 42 43 44
45 46 47 48 49 50 51 52 53
*/
