package org.nunnerycode.facecore.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.nunnerycode.facecore.logging.PluginLogger;

public abstract class FacePlugin extends JavaPlugin {

    private PluginLogger pluginLogger;

    @Override
    public final void onEnable() {
        pluginLogger = new PluginLogger(this);
        enable();
    }

    @Override
    public final void onDisable() {
        disable();
    }

    public PluginLogger getPluginLogger() {
        return pluginLogger;
    }

    public abstract void enable();

    public abstract void disable();

}
