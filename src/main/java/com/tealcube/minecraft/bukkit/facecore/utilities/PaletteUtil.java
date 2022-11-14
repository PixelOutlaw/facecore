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

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class PaletteUtil {

  public static final List<FaceColor> COLORS = List.of(FaceColor.values());

  public static List<String> color(List<String> s) {
    List<String> newLore = new ArrayList<>();
    s.forEach(l -> newLore.add(colorLine(l)));
    return newLore;
  }

  public static List<String> culturallyEnrich(List<String> s) {
    return color(s);
  }

  public static String color(String s) {
    return colorLine(s);
  }

  public static String culturallyEnrich(String s) {
    return color(s);
  }

  public static void sendMessage(CommandSender sender, String message) {
    message = color(message);
    sender.sendMessage(message);
  }

  public static void sendMessage(List<CommandSender> sender, String message) {
    message = color(message);
    for (CommandSender s : sender) {
      s.sendMessage(message);
    }
  }

  private static String colorLine(@NotNull String s) {
    for (FaceColor c : COLORS) {
      s = s.replace(c.getChatCode(), c.s());
    }
    return s;
  }

  public static String loadString(ConfigurationSection cs, String string, String fallback) {
    String result = cs.getString(string, fallback);
    return result == null ? "" : color(cs.getString(string, fallback));
  }

  public static List<String> loadStrings(ConfigurationSection cs, String string) {
    return color(cs.getStringList(string));
  }
}

