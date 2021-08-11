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

import io.pixeloutlaw.minecraft.spigot.garbage.ListExtensionsKt;
import io.pixeloutlaw.minecraft.spigot.garbage.StringExtensionsKt;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;

public class TextUtils {

  public static List<String> color(List<String> text) {
    if (text == null || text.size() == 0) {
      return new ArrayList<>();
    }
    return ListExtensionsKt.chatColorize(text);
  }

  public static String color(String text) {
    if (StringUtils.isBlank(text)) {
      return text;
    }
    return StringExtensionsKt.chatColorize(text);
  }

  public static List<String> getLore(ItemStack stack) {
    if (!stack.hasItemMeta()) {
      return new ArrayList<>();
    }
    if (stack.getItemMeta().getLore() == null) {
      return new ArrayList<>();
    }
    return stack.getItemMeta().getLore();
  }

  public static void setLore(ItemStack stack, List<String> lore) {
    setLore(stack, lore, false);
  }

  public static void setLore(ItemStack stack, List<String> lore, boolean color) {
    if (color) {
      lore = color(lore);
    }
    stack.setLore(lore);
  }

}
