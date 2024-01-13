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
import com.tealcube.minecraft.bukkit.facecore.pojo.RandomSound;
import io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.DroppedItemWatcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemUtils {

  public static ItemStack BLANK = buildBlankStack();

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
    dropItem(loc, itemStack, looter, ticksLived, dropRgb, glowColor, null, extraHeight);
  }

  public static void dropItem(Location loc, ItemStack itemStack, Player looter, int ticksLived,
      Color dropRgb, ChatColor glowColor, RandomSound randomSound, boolean extraHeight) {
    Item drop = loc.getWorld().spawn(loc, Item.class, d -> d.setItemStack(itemStack));
    if (looter != null && glowColor != null) {
      try {
        MiscDisguise miscDisguise = new MiscDisguise(DisguiseType.DROPPED_ITEM);
        miscDisguise.setVelocitySent(true);
        DroppedItemWatcher watcher = (DroppedItemWatcher) miscDisguise.getWatcher();
        watcher.setItemStack(itemStack);
        watcher.setNoGravity(false);
        watcher.setGlowing(true);
        watcher.setGlowColor(glowColor);
        DisguiseAPI.disguiseToPlayers(drop, miscDisguise, looter);
      } catch (Exception e) {
        Bukkit.getLogger().warning("[FaceCore] Failed to disguise glow loot...");
        e.printStackTrace();
      }
    }
    drop.setVelocity(new Vector(
        -0.125 + Math.random() * 0.25,
        extraHeight ? 0.42 + Math.random() * 0.15 : 0.3,
        -0.125 + Math.random() * 0.25)
    );
    if (dropRgb != null) {
      new DropTrail(drop, looter, dropRgb, randomSound);
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
      if (drop.isValid()) {
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

  public static int getModelData(@Nonnull ItemStack itemStack) {
    if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasCustomModelData()) {
      return -1;
    }
    return itemStack.getItemMeta().getCustomModelData();
  }

  private static ItemStack buildBlankStack() {
    ItemStack stack = new ItemStack(Material.BARRIER);
    ItemStackExtensionsKt.setCustomModelData(stack, 50);
    ItemStackExtensionsKt.setDisplayName(stack, "");
    return stack;
  }
}
