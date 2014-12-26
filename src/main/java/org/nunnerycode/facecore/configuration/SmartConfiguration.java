package org.nunnerycode.facecore.configuration;

import org.bukkit.configuration.Configuration;

import java.io.File;

public interface SmartConfiguration extends Configuration {

    void save();

    void load();

    File getFile();

    String getFileName();

}
