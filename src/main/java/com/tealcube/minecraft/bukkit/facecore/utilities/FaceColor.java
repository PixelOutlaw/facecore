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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public enum FaceColor {
  TRUE_WHITE("|none|", new Color(255, 255, 255), true),
  WHITE("|white|", new Color(255, 251, 243), true),
  OFF_WHITE("|dwhite|", new Color(238, 236, 233), false),
  LIGHT_GRAY("|lgray|", new Color(173, 161, 156), true),
  GRAY("|gray|", new Color(126, 110, 110), true),
  DARK_GRAY("|dgray|", new Color(86, 73, 78), true),
  GREEN("|green|", new Color(66, 143, 57), true),
  LIGHT_GREEN("|lgreen|", new Color(78, 211, 42), true),
  LIME("|lime|", new Color(143, 211, 60), true),
  YELLOW("|yellow|", new Color(246, 198, 70), true),
  ORANGE("|orange|", new Color(209, 133, 30), true),
  DARK_ORANGE("|dorange|", new Color(163, 76, 38), true),
  RED("|red|", new Color(228, 35, 51), true),
  CRIMSON("|crimson|", new Color(172, 21, 55), true),
  PINK("|pink|", new Color(234, 122, 122), true),
  PURPLE("|purple|", new Color(148, 63, 197), true),
  LAVENDER("|lavender|", new Color(163, 115, 236), true),
  CYAN("|cyan|", new Color(58, 221, 246), true),
  TEAL("|teal|", new Color(40, 191, 174), true),
  BLUE("|blue|", new Color(53, 110, 228), true),
  DARK_BLUE("|dblue|", new Color(64, 54, 159), true),
  BROWN("|brown|", new Color(131, 76, 52), true),
  BLACK("|black|", new Color(37, 20, 25), true),
  CHROMATIC("|chrom|", new Color(0, 255, 255), true),
  RAINBOW("|rainbow|", new Color(0, 251, 255), true),

  UNDERLINE("|ul|", ChatColor.UNDERLINE, false),
  CHAOS("|c|", ChatColor.MAGIC, false),
  STRIKETHROUGH("|st|", ChatColor.STRIKETHROUGH, false),
  ITALIC("|i|", ChatColor.ITALIC, false),
  RESET("|r|", ChatColor.RESET, false),
  BOLD("|b|", ChatColor.BOLD, false),

  SKEW("|skew|", new Color(251, 251, 255), false),
  GROW("|grow|", new Color(251, 251, 251), false),
  FADE("|fade|", new Color(247, 251, 251), false),
  SHIMMER("|shimmer|", new Color(247, 247, 251), false);

  private final Color rawColor;
  private final ChatColor color;
  @Getter
  private final boolean shaderEffects;
  private final String chatCode;
  private final String s;
  @Getter
  private final Map<ShaderStyle, ChatColor> shaderStyles = new HashMap<>();

  FaceColor(String chatCode, Color javaColor, boolean shaderEffects) {
    this.rawColor = javaColor;
    this.shaderEffects = shaderEffects;
    this.color = ChatColor.of(javaColor);
    this.chatCode = chatCode;
    this.s = "" + this.color;
    buildShaderStyles();
  }

  FaceColor(String chatCode, ChatColor color, boolean shaderEffects) {
    this.rawColor = new Color(0);
    this.shaderEffects = shaderEffects;
    this.color = color;
    this.chatCode = chatCode;
    this.s = color + "";
    buildShaderStyles();
  }

  public Color getRawColor() {
    return rawColor;
  }

  public String getChatCode() {
    return chatCode;
  }

  public ChatColor getColor() {
    return color;
  }

  public String s() {
    return s;
  }

  public String shaded(ShaderStyle shaderStyle) {
    if (!shaderEffects) {
      return s;
    }
    return shaderStyles.get(shaderStyle) + "";
  }

  public boolean isStartOf(String string) {
    return string.toLowerCase().startsWith(s.toLowerCase());
  }

  private void buildShaderStyles() {
    if (!shaderEffects) {
      return;
    }
    for (ShaderStyle s : ShaderStyle.values()) {
      int newBlue = rawColor.getBlue() - ((1+s.ordinal()) * 4);
      if (newBlue < 0) {
        Bukkit.getLogger().info("[FaceCore] color too low for " + chatCode + " " + s);
        continue;
      }
      Color c = new Color(
          rawColor.getRed(),
          rawColor.getGreen(),
          newBlue
      );
      shaderStyles.put(s, ChatColor.of(c));
    }
  }

  @Override
  public String toString() {
    return s;
  }

  // CARDINALITY MATTERS! ONLY ADD FROM THE BOTTOM
  public enum ShaderStyle {
    SHAKE,
    WAVE,
    BOUNCE,
    FLIP,
    OUTLINE,
  }
}
