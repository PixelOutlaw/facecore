package org.nunnerycode.facecore.profile;

import com.sk89q.squirrelid.Profile;
import com.sk89q.squirrelid.cache.ProfileCache;
import com.sk89q.squirrelid.cache.SQLiteCache;
import com.sk89q.squirrelid.resolver.CacheForwardingService;
import com.sk89q.squirrelid.resolver.HttpRepositoryService;
import org.nunnerycode.facecore.FacecorePlugin;
import org.nunnerycode.kern.apache.commons.lang3.Validate;
import org.nunnerycode.kern.shade.google.common.base.Optional;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public final class PlayerResolver {

    private ProfileCache cache;
    private CacheForwardingService forwardingService;

    public PlayerResolver(FacecorePlugin plugin) {
        try {
            cache = new SQLiteCache(new File(plugin.getDataFolder(), "players.sqlite"));
            forwardingService = new CacheForwardingService(HttpRepositoryService.forMinecraft(), cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Profile> getProfile(String name) {
        Validate.notNull(name, "name cannot be null");
        Optional<Profile> profileOptional = Optional.absent();
        if (forwardingService != null) {
            try {
                Profile p = forwardingService.findByName(name);
                profileOptional = Optional.fromNullable(p);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        return profileOptional;
    }

    public Optional<Profile> getProfile(UUID uuid) {
        Validate.notNull(uuid, "uuid cannot be null");
        Optional<Profile> profileOptional = Optional.absent();
        if (cache != null) {
            Profile p = cache.getIfPresent(uuid);
            profileOptional = Optional.fromNullable(p);
        }
        return profileOptional;
    }

    public PlayerResolver addProfile(Optional<Profile> profileOptional) {
        Validate.notNull(profileOptional, "profileOptional cannot be null");
        if (profileOptional.isPresent()) {
            cache.put(profileOptional.get());
        }
        return this;
    }

}
