package org.nunnerycode.facecore.profile;

import com.sk89q.squirrelid.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.nunnerycode.kern.shade.google.common.base.Optional;

public final class PlayerJoinListener implements Listener {

    private final PlayerResolver resolver;

    public PlayerJoinListener(PlayerResolver resolver) {
        this.resolver = resolver;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Optional<Profile> profileOptional = resolver.getProfile(event.getPlayer().getUniqueId());
        if (profileOptional.isPresent()) {
            resolver.addProfile(profileOptional);
            return;
        }
        profileOptional = resolver.getProfile(event.getPlayer().getName());
        if (profileOptional.isPresent()) {
            resolver.addProfile(profileOptional);
        }
    }

}
