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

import com.tealcube.minecraft.bukkit.facecore.FacecorePlugin;
import com.tealcube.minecraft.bukkit.facecore.pojo.RandomSound;
import java.awt.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DropTrail extends BukkitRunnable {

  private final Player owner;
  private final Item item;
  private final DustOptions dustOptions;
  private final RandomSound randomSound;

  public DropTrail(Item item, Player owner, Color dropRgb, RandomSound randomSound) {
    this.item = item;
    this.owner = owner;
    this.randomSound = randomSound;
    dustOptions = new DustOptions(org.bukkit.Color.fromRGB(
        dropRgb.getRed(), dropRgb.getGreen(), dropRgb.getBlue()), 2);
    this.runTaskTimer(FacecorePlugin.getInstance(), 0L, 1L);
    if (owner != null) {
      owner.playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.MASTER, 4f, 2f);
    } else {
      item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.MASTER, 4f, 2f);
    }
  }

  @Override
  public void run() {
    if (!item.isValid() || (item.getVelocity().getY() < 0.01 && item.isOnGround())) {
      cancel();
      if (owner != null) {
        if (randomSound == null) {
          owner.playSound(item.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.MASTER, 4f, 2f);
        } else {
          randomSound.play(item.getLocation(), owner);
        }
      } else {
        if (randomSound == null) {
          item.getWorld().playSound(item.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.MASTER, 4f, 2f);
        } else {
          randomSound.play(item.getLocation());
        }
      }
      return;
    }
    Location loc = item.getLocation().clone().add(item.getVelocity()).add(0, 0.5, 0);
    if (owner != null) {
      owner.spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0, dustOptions);
    } else {
      item.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0, dustOptions);
    }
  }
}
