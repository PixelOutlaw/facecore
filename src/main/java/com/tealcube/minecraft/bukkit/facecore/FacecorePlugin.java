/*
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
package com.tealcube.minecraft.bukkit.facecore;

import com.tealcube.minecraft.bukkit.facecore.plugin.FacePlugin;
import com.tealcube.minecraft.bukkit.facecore.profile.PlayerJoinListener;
import com.tealcube.minecraft.bukkit.facecore.profile.PlayerResolver;
import io.github.Cnly.BusyInv.BusyInv.listeners.BusyListener;
import io.pixeloutlaw.minecraft.spigot.config.SmartYamlConfiguration;
import org.bukkit.event.HandlerList;

import java.io.File;

public final class FacecorePlugin extends FacePlugin {

  private static FacecorePlugin _INSTANCE;
  private File loggerFile;
  private SmartYamlConfiguration playerDataYAML;
  private PlayerJoinListener playerJoinListener;
  private BusyListener busyListener;

  public static FacecorePlugin getInstance() {
    return _INSTANCE;
  }

  public File getLoggerFile() {
    return loggerFile;
  }

  @Override
  public void enable() {
    _INSTANCE = this;
    loggerFile = new File(getDataFolder(), "debug.log");
    playerDataYAML = new SmartYamlConfiguration(new File(getDataFolder(), "playerData.yml"));
    PlayerResolver.getInstance().loadFrom(playerDataYAML);

    playerJoinListener = new PlayerJoinListener();
    busyListener = new BusyListener();

    getServer().getPluginManager().registerEvents(playerJoinListener, this);
    getServer().getPluginManager().registerEvents(busyListener, this);
  }

  @Override
  public void disable() {
    PlayerResolver.getInstance().saveTo(playerDataYAML);
    HandlerList.unregisterAll(this);
    playerJoinListener = null;
    playerDataYAML = null;
  }

}
