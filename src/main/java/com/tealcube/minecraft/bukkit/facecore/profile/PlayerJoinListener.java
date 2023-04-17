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

import com.tealcube.minecraft.bukkit.facecore.FacecorePlugin;
import com.tealcube.minecraft.bukkit.facecore.utilities.MoveUtil;
import java.util.Optional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public final class PlayerJoinListener implements Listener {

  @EventHandler(priority = EventPriority.LOW)
  public void onPlayerLogin(final PlayerLoginEvent event) {
    if (!FacecorePlugin.isLoginAllowed()) {
      event.setKickMessage("Server is shutting down! Bad timing I guess...");
      event.setResult(Result.KICK_OTHER);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoinEvent(final PlayerJoinEvent event) {
    Optional<Profile> profileOptional = PlayerResolver.getInstance()
        .findProfile(event.getPlayer().getUniqueId());
    Profile p = profileOptional.orElse(
        new Profile(event.getPlayer().getUniqueId(), event.getPlayer().getName()));
    if (!p.getName().equals(event.getPlayer().getName())) {
      p.setLastKnownName(p.getName());
      p.setName(event.getPlayer().getName());
    }
    PlayerResolver.getInstance().addProfile(p);
    MoveUtil.setLastMoved(event.getPlayer());
  }

}
