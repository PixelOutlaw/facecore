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
import net.md_5.bungee.api.ChatColor;

public enum FaceColor {
  WHITE("|white|", new Color(255, 251, 243)),
  LIGHT_GRAY("|lgray|", new Color(173, 161, 156)),
  GRAY("|gray|", new Color(126, 110, 110)),
  DARK_GRAY("|dgray|", new Color(86, 73, 78)),
  GREEN("|green|", new Color(57, 159, 13)),
  LIGHT_GREEN("|lgreen|", new Color(76, 234, 25)),
  LIME("|lime|", new Color(138, 248, 40)),
  YELLOW("|yellow|", new Color(255, 212, 66)),
  ORANGE("|orange|", new Color(248, 132, 44)),
  DARK_ORANGE("|dorange|", new Color(194, 106, 29)),
  RED("|red|", new Color(252, 61, 56)),
  CRIMSON("|crimson|", new Color(239, 30, 24)),
  PINK("|pink|", new Color(243, 109, 124)),
  PURPLE("|purple|", new Color(166, 63, 201)),
  CYAN("|cyan|", new Color(77, 242, 255)),
  TEAL("|teal|", new Color(22, 166, 162)),
  BLUE("|blue|", new Color(47, 136, 225)),
  DARK_BLUE("|dblue|", new Color(34, 94, 183)),
  BROWN("|brown|", new Color(152, 79, 37)),
  TAN("|tan|", new Color(236, 161, 106)),
  BLACK("|black|", new Color(45, 27, 31)),

  UNDERLINE("|ul|", ChatColor.UNDERLINE),
  CHAOS("|c|", ChatColor.MAGIC),
  STRIKETHROUGH("|st|", ChatColor.STRIKETHROUGH),
  ITALIC("|i|", ChatColor.ITALIC),
  RESET("|r|", ChatColor.RESET),
  BOLD("|b|", ChatColor.BOLD);

  private final Color rawColor;
  private final ChatColor color;
  private final String chatCode;
  private final String s;

  FaceColor(String chatCode, Color javaColor) {
    this.rawColor = javaColor;
    this.color = ChatColor.of(javaColor);
    this.chatCode = chatCode;
    this.s = "" + this.color;
  }

  FaceColor(String chatCode, ChatColor color) {
    this.rawColor = new Color(0);
    this.color = color;
    this.chatCode = chatCode;
    this.s = "" + color;
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

  @Override
  public String toString() {
    return s;
  }
}
