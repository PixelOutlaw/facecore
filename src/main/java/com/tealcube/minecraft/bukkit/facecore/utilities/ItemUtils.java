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
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.util.Vector;

public class ItemUtils {

  public static ItemStack BLANK = buildBlankStack();
  private static final Map<String, ArmorTrim> armorTrimKeys = createTrimKeys();

  public static void applyCustomTrim(ItemStack stack, String faceTrim) {
    if (!armorTrimKeys.containsKey(faceTrim)) {
      Bukkit.getLogger().warning("[FaceCore] Trim key " + faceTrim + " does not exist");
      return;
    }
    ItemMeta meta = stack.getItemMeta();
    if (!(meta instanceof ArmorMeta armorMeta)) {
      return;
    }
    armorMeta.setTrim(armorTrimKeys.get(faceTrim));
    armorMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
    stack.setItemMeta(armorMeta);
  }

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

  @SuppressWarnings("DataFlowIssue")
  private static Map<String, ArmorTrim> createTrimKeys() {
    Map<String, ArmorTrim> keys = new HashMap<>();

    keys.put("bone", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("bone"))));
    keys.put("frog_prince", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("frog_prince"))));
    keys.put("guardsmen", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("guardsmen"))));
    keys.put("hunters", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("hunters"))));
    keys.put("snazzy", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("snazzy"))));
    keys.put("starter", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("starter"))));
    keys.put("misc1", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("misc1"))));
    keys.put("misc2", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("misc2"))));
    keys.put("necro", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("necro"))));
    keys.put("red_knight", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("red_knight"))));
    keys.put("toasty", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("toasty"))));
    keys.put("nether_armor", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("nether_armor"))));

    keys.put("yeti", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("yeti"))));
    keys.put("green_ogre", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("green_ogre"))));
    keys.put("ogre", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("ogre"))));

    keys.put("cloth", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("cloth"))));
    keys.put("cloth_t1", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("cloth_t1"))));
    keys.put("cloth_t2", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("cloth_t2"))));
    keys.put("cloth_t3", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("cloth_t3"))));
    keys.put("cloth_t4", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("cloth_t4"))));
    keys.put("cloth_t5", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("cloth_t5"))));

    keys.put("heavy", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("heavy"))));
    keys.put("heavy_t1", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("heavy_t1"))));
    keys.put("heavy_t2", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("heavy_t2"))));
    keys.put("heavy_t3", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("heavy_t3"))));
    keys.put("heavy_t4", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("heavy_t4"))));
    keys.put("heavy_t5", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("heavy_t5"))));

    keys.put("light", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("light"))));
    keys.put("light_t1", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("light_t1"))));
    keys.put("light_t2", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("light_t2"))));
    keys.put("light_t3", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("light_t3"))));
    keys.put("light_t4", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("light_t4"))));
    keys.put("light_t5", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("light_t5"))));

    keys.put("extra1", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra1"))));
    keys.put("extra2", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra2"))));
    keys.put("extra3", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra3"))));
    keys.put("extra4", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra4"))));
    keys.put("extra5", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra5"))));
    keys.put("extra6", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra6"))));
    keys.put("extra7", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra7"))));
    keys.put("extra8", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra8"))));
    keys.put("extra9", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra9"))));
    keys.put("extra10", new ArmorTrim(TrimMaterial.QUARTZ, Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("extra10"))));

    return keys;
  }
}
