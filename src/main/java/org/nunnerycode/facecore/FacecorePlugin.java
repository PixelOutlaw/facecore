package org.nunnerycode.facecore;

import org.bukkit.event.HandlerList;
import org.nunnerycode.facecore.plugin.FacePlugin;
import org.nunnerycode.facecore.profile.PlayerJoinListener;
import org.nunnerycode.facecore.profile.PlayerResolver;

public final class FacecorePlugin extends FacePlugin {

    private PlayerResolver playerResolver;
    private PlayerJoinListener playerJoinListener;

    @Override
    public void enable() {
        playerResolver = new PlayerResolver(this);
        playerJoinListener = new PlayerJoinListener(playerResolver);
        getServer().getPluginManager().registerEvents(playerJoinListener, this);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
        playerJoinListener = null;
        playerResolver = null;
    }

    public PlayerResolver getPlayerResolver() {
        return playerResolver;
    }

}
