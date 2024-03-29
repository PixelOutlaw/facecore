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

import io.pixeloutlaw.minecraft.spigot.config.SmartYamlConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class UnicodeUtil {

  private static Map<String, String> cachedUnicode;

  public static void cacheUnicode(SmartYamlConfiguration config) {
    cachedUnicode = new HashMap<>();
    try {
      ConfigurationSection section = config.getConfigurationSection("unicode");
      if (section == null) {
        return;
      }
      for (String id : section.getKeys(false)) {
        cachedUnicode.put("<" + id + ">", PaletteUtil.culturallyEnrich(section.getString(id)));
      }
    } catch (Exception e) {
      Bukkit.getLogger().severe("[Facecore] Something went wrong!! Unicode failed!!");
      e.printStackTrace();
    }
  }

  public static String unicodePlacehold(String string) {
    for (Entry<String, String> entry : cachedUnicode.entrySet()) {
      string = string.replaceAll(entry.getKey(), entry.getValue());
    }
    return string;
  }

  public static List<String> unicodePlacehold(List<String> stringList) {
    List<String> strs = new ArrayList<>();
    for (String s :stringList) {
      strs.add(unicodePlacehold(s));
    }
    return strs;
  }

}
