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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public class MoveUtil {

  private final static Map<UUID, Long> LAST_MOVED = new HashMap<>();
  private final static Map<UUID, Long> LAST_GROUNDED = new HashMap<>();
  private final static Map<UUID, Long> SNEAK_START = new HashMap<>();

  public static void setLastMoved(Player player) {
    LAST_MOVED.put(player.getUniqueId(), System.currentTimeMillis());
  }

  public static void setLastGrounded(Player player) {
    LAST_GROUNDED.put(player.getUniqueId(), System.currentTimeMillis());
  }

  public static boolean hasMoved(Player player) {
    return System.currentTimeMillis() - LAST_MOVED.getOrDefault(player.getUniqueId(), 0L) < 100;
  }

  public static long timeOffGround(Player player) {
    return System.currentTimeMillis() - LAST_GROUNDED.getOrDefault(player.getUniqueId(), 1L);
  }

  public static void setSneak(Player player) {
    if (player.isSneaking()) {
      if (!SNEAK_START.containsKey(player.getUniqueId())) {
        SNEAK_START.put(player.getUniqueId(), System.currentTimeMillis());
      }
    } else {
      SNEAK_START.remove(player.getUniqueId());
    }
  }

  public static int getLastSneak(UUID uuid) {
    if (!SNEAK_START.containsKey(uuid)) {
      return -1;
    }
    return Math.toIntExact(System.currentTimeMillis() - SNEAK_START.get(uuid));
  }
}
