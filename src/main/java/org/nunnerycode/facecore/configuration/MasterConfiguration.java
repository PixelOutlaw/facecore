package org.nunnerycode.facecore.configuration;

import org.bukkit.configuration.MemoryConfiguration;

public class MasterConfiguration extends MemoryConfiguration {

    public void load(SmartConfiguration... configurations) {
        for (SmartConfiguration configuration : configurations) {
            for (String key : configuration.getKeys(true)) {
                if (configuration.isConfigurationSection(key)) {
                    continue;
                }
                set(configuration.getFileName().toLowerCase() + "." + key, configuration.get(key));
            }
        }
    }

}
