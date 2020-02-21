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

import static net.amoebaman.util.Reflection.getNMSClass;

import java.lang.reflect.Constructor;
import org.bukkit.entity.Player;

public final class TitleUtils {

  private final static int DEFAULT_FADE_IN = 10;
  private final static int DEFAULT_FADE_OUT = 10;
  private final static int DEFAULT_DURATION = 40;

  public static void sendTitle(Player sender, String upper, String lower) {
    sendTitle(sender, upper, lower, DEFAULT_DURATION, DEFAULT_FADE_IN, DEFAULT_FADE_OUT);
  }

  public static void sendTitle(Player sender, String upper, String lower, int duration, int fadeIn,
      int fadeOut) {
    try {
      Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
          .getMethod("a", String.class).invoke(null, "{\"text\": \"" + upper + "\"}");
      Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle")
          .getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
              getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
      Object packet = titleConstructor.newInstance(
          getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null),
          chatTitle, fadeIn, duration, fadeOut);

      Object chatsTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
          .getMethod("a", String.class).invoke(null, "{\"text\": \"" + lower + "\"}");
      Constructor<?> timingTitleConstructor = getNMSClass("PacketPlayOutTitle")
          .getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
              getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
      Object timingPacket = timingTitleConstructor.newInstance(
          getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null),
          chatsTitle, fadeIn, duration, fadeOut);
      sendPacket(sender, packet);
      sendPacket(sender, timingPacket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void sendPacket(Player player, Object packet) {
    try {
      Object handle = player.getClass().getMethod("getHandle").invoke(player);
      Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
      playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"))
          .invoke(playerConnection, packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
