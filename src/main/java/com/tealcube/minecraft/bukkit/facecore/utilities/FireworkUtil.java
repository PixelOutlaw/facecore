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
import java.util.Objects;
import java.util.WeakHashMap;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class FireworkUtil {

  private static final Map<Firework, Boolean> NO_DAMAGE_FIREWORK = new WeakHashMap<>();

  public static void spawnFirework(Location loc, Type type, Color color, Color fade, boolean trail,
      boolean flicker) {

    Firework firework = (Firework) Objects.requireNonNull(loc.getWorld())
        .spawn(loc, EntityType.FIREWORK.getEntityClass(), e -> {
          Color fadeColor = fade == null ? Color.BLACK : fade;

          FireworkMeta fireworkMeta = ((Firework) e).getFireworkMeta();
          fireworkMeta.setPower(0);
          fireworkMeta.addEffect(FireworkEffect.builder()
              .with(type)
              .withColor(color)
              .flicker(flicker)
              .withFade(fadeColor)
              .trail(trail)
              .build()
          );

          ((Firework) e).setFireworkMeta(fireworkMeta);
          e.setSilent(true);
        });

    NO_DAMAGE_FIREWORK.put(firework, true);
    firework.detonate();
  }

  public static void spawnLevelupFireworks(Player player, Color color) {
    Location location = player.getEyeLocation().clone().add(new Vector(0, 5, 0));
    spawnFirework(
        location.clone().add(new Vector(Math.random() - 0.5, 0, Math.random() - 0.5).multiply(3)),
        Type.BALL, color, Color.WHITE, false, true);
    spawnFirework(
        location.clone().add(new Vector(Math.random() - 0.5, 0, Math.random() - 0.5).multiply(3)),
        Type.BALL, color, Color.WHITE, false, true);
    spawnFirework(
        location.clone().add(new Vector(Math.random() - 0.5, 0, Math.random() - 0.5).multiply(3)),
        Type.BALL, color, Color.WHITE, false, true);
  }

  public static boolean isNoDamage(Firework firework) {
    return NO_DAMAGE_FIREWORK.containsKey(firework);
  }
}
