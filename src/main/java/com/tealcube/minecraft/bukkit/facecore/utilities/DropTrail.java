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
import java.awt.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DropTrail extends BukkitRunnable {

  private final Player owner;
  private final Item item;
  private final DustOptions dustOptions;

  public DropTrail(Item item, Player owner, Color dropRgb) {
    this.item = item;
    this.owner = owner;
    dustOptions = new DustOptions(org.bukkit.Color.fromRGB(
        dropRgb.getRed(), dropRgb.getGreen(), dropRgb.getBlue()), 2);
    this.runTaskTimer(FacecorePlugin.getInstance(), 0L, 1L);
  }

  @Override
  public void run() {
    if (!item.isValid() || (item.getVelocity().getY() < 0.01 && item.isOnGround())) {
      cancel();
      return;
    }
    Location loc = item.getLocation().clone().add(item.getVelocity()).add(0, 0.5, 0);
    owner.spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0, dustOptions);
  }
}
