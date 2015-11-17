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
package com.tealcube.minecraft.bukkit.facecore;

import com.tealcube.minecraft.bukkit.config.SmartYamlConfiguration;
import com.tealcube.minecraft.bukkit.facecore.plugin.FacePlugin;
import com.tealcube.minecraft.bukkit.facecore.profile.PlayerJoinListener;
import com.tealcube.minecraft.bukkit.facecore.profile.PlayerResolver;
import com.tealcube.minecraft.bukkit.lumberjack.Lumberjack;
import com.tealcube.minecraft.bukkit.lumberjack.shade.slf4j.Logger;
import org.bukkit.event.HandlerList;

import java.io.File;

public final class FacecorePlugin extends FacePlugin {

    private static FacecorePlugin _INSTANCE;
    private Logger logger;
    private File loggerFile;
    private SmartYamlConfiguration playerDataYAML;
    private PlayerJoinListener playerJoinListener;

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
        logger = Lumberjack.loggerToFile(FacecorePlugin.class, loggerFile.getAbsolutePath());
        playerDataYAML = new SmartYamlConfiguration(new File(getDataFolder(), "playerData.yml"));
        PlayerResolver.getInstance().loadFrom(playerDataYAML);
        playerJoinListener = new PlayerJoinListener(this);
        getServer().getPluginManager().registerEvents(playerJoinListener, this);
        logger.info("Facecore v" + getDescription().getVersion() + " enabled");
    }

    @Override
    public void disable() {
        logger.info("Facecore v" + getDescription().getVersion() + " disabled");
        PlayerResolver.getInstance().saveTo(playerDataYAML);
        HandlerList.unregisterAll(this);
        playerJoinListener = null;
        playerDataYAML = null;
    }

    public void debug(String message) {
        logger.info(message);
    }

}
