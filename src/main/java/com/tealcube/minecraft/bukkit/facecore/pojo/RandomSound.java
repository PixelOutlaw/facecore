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
package com.tealcube.minecraft.bukkit.facecore.pojo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@Data
public class RandomSound {

  private final float pitchRange;
  private final float pitchBase;
  private final float volume;
  private final Sound sound;
  private final SoundCategory soundCategory;

  private static RandomSound defaultSound = new RandomSound(SoundCategory.MASTER,
      Sound.ENTITY_CHICKEN_DEATH, 1f, 0.9f, 0.2f);

  public RandomSound(SoundCategory soundCategory, Sound sound, float volume,
      float pitchBase, float pitchRange) {
    this.soundCategory = soundCategory;
    this.sound = sound;
    this.volume = volume;
    this.pitchBase = pitchBase;
    this.pitchRange = pitchRange;
  }

  public static RandomSound load(ConfigurationSection section) {
    if (section == null) {
      Bukkit.getLogger().warning("Failed to load sound - null config section - using default");
      return defaultSound;
    }
    String soundString = section.getString("sound");
    Sound sound = Sound.ENTITY_CHICKEN_DEATH;
    try {
      sound = Sound.valueOf(soundString);
    } catch (Exception ignored) {}
    String categoryString = section.getString("sound-category");
    SoundCategory category = SoundCategory.MASTER;
    if (StringUtils.isNotBlank(categoryString)) {
      try {
        category = SoundCategory.valueOf(categoryString);
      } catch (Exception ignored) {
      }
    }

    return new RandomSound(
        category,
        sound,
        (float) section.getDouble("volume", 1),
        (float) section.getDouble("pitch-base", 0.9),
        (float) section.getDouble("pitch-range", 0.2)
    );
  }

  public void play(Location location) {
    location.getWorld().playSound(location, sound, soundCategory, volume, (float) (pitchBase + Math.random() * pitchRange));
  }

  public void play(Location location, Player... players) {
    for (Player p : players) {
      p.playSound(location, sound, soundCategory, volume, (float) (pitchBase + Math.random() * pitchRange));
    }
  }

}
