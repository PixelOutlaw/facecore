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

import com.tealcube.minecraft.bukkit.config.SmartYamlConfiguration;
import com.tealcube.minecraft.bukkit.kern.shade.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerResolver {

    private static PlayerResolver instance;
    private final Map<String, Profile> profileMap = new ConcurrentHashMap<>();

    private PlayerResolver() {
        // do nothing
    }

    public List<Optional<Profile>> findAllProfiles(String name) {
        List<Optional<Profile>> optionals = new ArrayList<>();
        for (Profile profile : profileMap.values()) {
            if (profile != null && (profile.getName().equals(name) || profile.getLastKnownName().equals(name))) {
                optionals.add(Optional.of(profile));
            }
        }
        return optionals;
    }

    public List<Optional<Profile>> findAllProfiles(UUID uuid) {
        List<Optional<Profile>> optionals = new ArrayList<>();
        for (Profile profile : profileMap.values()) {
            if (profile != null && profile.getUuid().equals(uuid)) {
                optionals.add(Optional.of(profile));
            }
        }
        return optionals;
    }

    public Optional<Profile> findProfile(String name) {
        Optional<Profile> optional = Optional.absent();
        List<Optional<Profile>> profiles = findAllProfiles(name);
        if (profiles.size() > 0) {
            return profiles.get(0);
        }
        return optional;
    }

    public Optional<Profile> findProfile(UUID uuid) {
        Optional<Profile> optional = Optional.absent();
        List<Optional<Profile>> profiles = findAllProfiles(uuid);
        if (profiles.size() > 0) {
            return profiles.get(0);
        }
        return optional;
    }

    public void addProfile(Profile profile) {
        if (profile == null) {
            return;
        }
        profileMap.put(profile.getName().toLowerCase(), profile);
    }

    public void loadFrom(SmartYamlConfiguration config) {
        config.load();
        for (String s : config.getKeys(false)) {
            if (!config.isConfigurationSection(s)) {
                continue;
            }
            UUID uuid = UUID.fromString(s);
            String name = config.getString(s + ".name");
            String lastKnownName = config.getString(s + ".last-known-name");
            Profile profile = new Profile(uuid, name, lastKnownName);
            addProfile(profile);
        }
    }

    public void saveTo(SmartYamlConfiguration config) {
        for (Profile p : profileMap.values()) {
            config.set(p.getUuid().toString() + ".name", p.getName());
            config.set(p.getUuid().toString() + ".last-known-name", p.getLastKnownName());
        }
        config.save();
    }

    public static PlayerResolver getInstance() {
        if (instance == null) {
            instance = new PlayerResolver();
        }
        return instance;
    }

}
