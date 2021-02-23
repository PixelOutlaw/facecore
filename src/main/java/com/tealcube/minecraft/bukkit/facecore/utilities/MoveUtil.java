/*
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

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MoveUtil {

  private final static Map<Player, Long> LAST_MOVED = new WeakHashMap<>();
  private final static Map<Player, Long> LAST_GROUNDED = new WeakHashMap<>();
  private final static Map<Player, Long> SNEAK_START = new WeakHashMap<>();
  private final static Map<Player, Vector> LAST_POS = new WeakHashMap<>();
  private final static Map<Player, Vector> VELOCITY = new WeakHashMap<>();

  public static void setLastMoved(Player player) {
    LAST_MOVED.put(player, System.currentTimeMillis());
  }

  public static void setLastGrounded(Player player) {
    LAST_GROUNDED.put(player, System.currentTimeMillis());
  }

  public static boolean hasMoved(Player player) {
    return System.currentTimeMillis() - LAST_MOVED.getOrDefault(player, 0L) < 100;
  }

  public static long timeOffGround(Player player) {
    return System.currentTimeMillis() - LAST_GROUNDED.getOrDefault(player, 1L);
  }

  public static void setVelocity(Player player) {
    VELOCITY.put(player, player.getLocation().toVector()
        .subtract(LAST_POS.getOrDefault(player, player.getLocation().toVector())));
    LAST_POS.put(player, player.getLocation().toVector());
  }

  public static Vector getVelocity(Player player) {
    return VELOCITY.getOrDefault(player, new Vector(0, 0, 0)).clone();
  }

  public static void setSneak(Player player) {
    if (player.isSneaking()) {
      if (!SNEAK_START.containsKey(player)) {
        SNEAK_START.put(player, System.currentTimeMillis());
      }
    } else {
      SNEAK_START.remove(player);
    }
  }

  @Deprecated
  public static int getLastSneak(UUID uuid) {
    for (Player p : SNEAK_START.keySet()) {
      if (p.getUniqueId().equals(uuid)) {
        return Math.toIntExact(System.currentTimeMillis() - SNEAK_START.get(p));
      }
    }
    return -1;
  }

  public static int getLastSneak(Player player) {
    if (SNEAK_START.containsKey(player)) {
      return Math.toIntExact(System.currentTimeMillis() - SNEAK_START.get(player));
    }
    return -1;
  }
}
