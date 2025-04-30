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

import com.tealcube.minecraft.bukkit.facecore.utilities.FaceColor.ShaderStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class PaletteUtil {

  public static final List<FaceColor> COLORS = List.of(FaceColor.values());
  public static final Map<String, String> colorDataMap = buildMap();

  private static final LegacyComponentSerializer HOLY_FUCK_FUCK_KYORI = LegacyComponentSerializer.builder()
      .character('§').hexCharacter('#').hexColors().useUnusualXRepeatedCharacterHexFormat().build();

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
    for (Entry<String, String> c : colorDataMap.entrySet()) {
      s = s.replace(c.getKey(), c.getValue());
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

  public static TextComponent loadComponent(ConfigurationSection cs, String field) {
    return loadComponent(cs, field, null);
  }

  public static TextComponent loadComponent(ConfigurationSection cs, String field, String fallback) {
    String result = cs.getString(field, fallback);
    return StringUtils.isBlank(result) ? Component.empty() : covertStringToRetardComponent(PaletteUtil.color(result));
  }

  public static List<TextComponent> loadComponents(ConfigurationSection cs, String field) {
    List<TextComponent> components = new ArrayList<>();
    for (String s : cs.getStringList(field)) {
      components.add(covertStringToRetardComponent(PaletteUtil.color(s)));
    }
    return components;
  }

  public static TextComponent convert(String s) {
    return covertStringToRetardComponent(PaletteUtil.color(s));
  }

  public static List<TextComponent> convert(List<String> strs) {
    List<TextComponent> comps = new ArrayList<>();
    for (String s : strs) {
      comps.add(covertStringToRetardComponent(PaletteUtil.color(s)));
    }
    return comps;
  }

  public static Map<String, String> buildMap() {
    Map<String, String> map = new HashMap<>();
    for (FaceColor c : COLORS) {
      map.put(c.getChatCode(), c.s());
      if (c.isShaderEffects()) {
        map.put("|" + c.getChatCode().replace("|", "") + "_shake|", c.shaded(ShaderStyle.SHAKE));
        map.put("|" + c.getChatCode().replace("|", "") + "_flip|", c.shaded(ShaderStyle.FLIP));
        map.put("|" + c.getChatCode().replace("|", "") + "_bounce|", c.shaded(ShaderStyle.BOUNCE));
        map.put("|" + c.getChatCode().replace("|", "") + "_wave|", c.shaded(ShaderStyle.WAVE));
        map.put("|" + c.getChatCode().replace("|", "") + "_outline|", c.shaded(ShaderStyle.OUTLINE));
      }
    }
    return map;
  }

  private static net.kyori.adventure.text.TextComponent covertStringToRetardComponent(String str) {
    return (net.kyori.adventure.text.TextComponent) MiniMessage.miniMessage()
        .deserialize(MiniMessage.miniMessage().serialize(HOLY_FUCK_FUCK_KYORI.deserialize(str))).decorationIfAbsent(
            TextDecoration.ITALIC, State.FALSE);
  }
}

