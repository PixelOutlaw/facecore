package com.tealcube.minecraft.bukkit.facecore.pojo;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@Data
public class RandomSound {

  private float pitchRange;
  private float pitchBase;
  private float volume;
  private final Sound sound;

  public RandomSound(Sound sound, float volume, float pitchBase, float pitchRange) {
    this.sound = sound;
    this.volume = volume;
    this.pitchBase = pitchBase;
    this.pitchRange = pitchRange;
  }

  public static RandomSound load(ConfigurationSection section) {
    String soundString = section.getString("sound");
    Sound sound = Sound.ENTITY_CHICKEN_DEATH;
    try {
      sound = Sound.valueOf(soundString);
    } catch (Exception ignored) {

    }
    return new RandomSound(
        sound,
        (float) section.getDouble("volume", 1),
        (float) section.getDouble("pitch-base", 0.9),
        (float) section.getDouble("pitch-range", 0.2)
    );
  }

  public void play(Location location) {
    location.getWorld().playSound(location, sound, volume, (float) (pitchBase + Math.random() * pitchRange));
  }

  public void play(Location location, Player... players) {
    for (Player p : players) {
      p.playSound(location, sound, volume, (float) (pitchBase + Math.random() * pitchRange));
    }
  }

}
