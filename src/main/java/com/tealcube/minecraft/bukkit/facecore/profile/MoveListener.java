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
package com.tealcube.minecraft.bukkit.facecore.profile;

import com.tealcube.minecraft.bukkit.facecore.event.LandEvent;
import com.tealcube.minecraft.bukkit.facecore.event.LaunchEvent;
import com.tealcube.minecraft.bukkit.facecore.utilities.MoveUtil;
import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

  private final Map<Player, Boolean> groundedLastTickMap = new WeakHashMap<>();

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerMove(PlayerMoveEvent event) {
    if (event.getPlayer().getVehicle() != null) {
      return;
    }
    if (event.getFrom().getX() != event.getTo().getX()
        || event.getFrom().getY() != event.getTo().getY()
        || event.getFrom().getZ() != event.getTo().getZ()) {
      MoveUtil.setLastMoved(event.getPlayer());
    }

    boolean grounded = ((Entity) event.getPlayer()).isOnGround();
    boolean wasOnGround = groundedLastTickMap.getOrDefault(event.getPlayer(), false);

    if (wasOnGround && !grounded) {
      LaunchEvent ev = new LaunchEvent(event.getPlayer(), event.getPlayer().getLocation());
      Bukkit.getPluginManager().callEvent(ev);
    } else if (!wasOnGround && grounded) {
      LandEvent ev = new LandEvent(event.getPlayer(), event.getPlayer().getLocation());
      Bukkit.getPluginManager().callEvent(ev);
    }

    if (grounded) {
      MoveUtil.setLastGrounded(event.getPlayer());
    }
    groundedLastTickMap.put(event.getPlayer(), grounded);
  }
}
