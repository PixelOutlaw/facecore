/**
 * The MIT License
 * Copyright (c) 2015 Teal Cube Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.facecore.utilities;

import com.tealcube.minecraft.bukkit.facecore.FacecorePlugin;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.xezard.glow.data.glow.Glow;

public class ItemUtils {

  @Nonnull
  public static Map<Integer, ItemStack> giveOrDrop(@Nonnull Player player, @Nullable ItemStack... items) {
    return giveOrDrop(player, 0, items);
  }

  @Nonnull
  public static Map<Integer, ItemStack> giveOrDrop(@Nonnull Player player, int ticksLived, @Nullable ItemStack... items) {
    if (items != null && items.length != 0) {
      Map<Integer, ItemStack> drop = player.getInventory().addItem(items);
      World world = player.getWorld();
      Location location = player.getLocation();
      drop.values().forEach((item) -> {
        if (item != null && item.getType() != Material.AIR) {
          Item itemEntity = world.dropItemNaturally(location, item);
          if (ticksLived > 0) {
            itemEntity.setTicksLived(ticksLived);
          }
          itemEntity.setOwner(player.getUniqueId());
          Bukkit.getScheduler().runTaskLater(FacecorePlugin.getInstance(), () -> {
            if (itemEntity.isValid()) {
              itemEntity.setOwner(null);
            }
          }, 600L);
        }
      });
      return drop;
    } else {
      return new HashMap<>();
    }
  }

  public static void dropItem(Location loc, ItemStack itemStack, Player looter, int ticksLived,
      Color dropRgb, ChatColor glowColor, boolean extraHeight) {
    Item drop = loc.getWorld().dropItem(loc, itemStack);
    drop.setVelocity(new Vector(
        -0.125 + Math.random() * 0.25,
        extraHeight ? 0.42 + Math.random() * 0.15 : 0.3,
        -0.125 + Math.random() * 0.25
    ));
    if (dropRgb != null) {
      new DropTrail(drop, looter, dropRgb);
    }
    try {
      if (looter != null && glowColor != null) {
        Glow glow = Glow.builder().color(glowColor).name("drop-glow").build();
        glow.addHolders(drop);
        glow.display(looter);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (ticksLived != 0) {
      drop.setTicksLived(ticksLived);
    }
    if (looter != null) {
      applyDropProtection(drop, looter.getUniqueId(), 300);
    }
  }

  public static void applyDropProtection(Item drop, UUID owner, long ticks) {
    drop.setOwner(owner);
    Bukkit.getScheduler().runTaskLater(FacecorePlugin.getInstance(), () -> {
      if (drop != null) {
        drop.setOwner(null);
      }
    }, ticks);
  }

  public static boolean containsLore(ItemStack itemStack, List<String> lore) {
    if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) {
      return false;
    }
    for (String s : TextUtils.getLore(itemStack)) {
      String stripped = ChatColor.stripColor(s);
      if (lore.contains(stripped)) {
        return true;
      }
    }
    return false;
  }
}
