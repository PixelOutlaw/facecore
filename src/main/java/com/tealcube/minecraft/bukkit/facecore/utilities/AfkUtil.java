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

import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.entity.Player;

public class AfkUtil {

  private final static Map<Player, Long> LAST_SPEAK = new WeakHashMap<>();
  private final static Map<Player, Long> LAST_COMMAND = new WeakHashMap<>();
  private final static Map<Player, Long> LAST_INTERACT = new WeakHashMap<>();

  public static void setLastSpeak(Player player) {
    LAST_SPEAK.put(player, System.currentTimeMillis());
  }

  public static void setLastCommand(Player player) {
    LAST_COMMAND.put(player, System.currentTimeMillis());
  }

  public static void setLastInteract(Player player) {
    LAST_INTERACT.put(player, System.currentTimeMillis());
  }

  public static boolean hasSpoken(Player player, int millis) {
    return System.currentTimeMillis() - LAST_SPEAK.getOrDefault(player, 0L) < millis;
  }

  public static boolean hasUsedCommand(Player player, int millis) {
    return System.currentTimeMillis() - LAST_COMMAND.getOrDefault(player, 0L) < millis;
  }

  public static boolean hasInteracted(Player player, int millis) {
    return System.currentTimeMillis() - LAST_INTERACT.getOrDefault(player, 0L) < millis;
  }

  public static boolean isAfk(Player player, int millis) {
    return !hasSpoken(player, millis) && !hasUsedCommand(player, millis) &&
        !MoveUtil.hasMoved(player, millis) && !hasInteracted(player, millis);
  }
}
