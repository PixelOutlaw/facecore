/*
 * This file is part of Facecore, licensed under the ISC License.
 *
 * Copyright (c) 2014 Richard Harrah
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.facecore.profile;

import com.sk89q.squirrelid.Profile;
import com.sk89q.squirrelid.cache.ProfileCache;
import com.sk89q.squirrelid.cache.SQLiteCache;
import com.sk89q.squirrelid.resolver.CacheForwardingService;
import com.sk89q.squirrelid.resolver.HttpRepositoryService;
import com.tealcube.minecraft.bukkit.facecore.FacecorePlugin;
import com.tealcube.minecraft.bukkit.kern.apache.commons.lang3.Validate;
import com.tealcube.minecraft.bukkit.kern.shade.google.common.base.Optional;

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
