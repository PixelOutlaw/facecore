package org.nunnerycode.facecore.configuration;

import java.io.File;

public interface SmartConfiguration {

    void load();

    void save();

    File getFile();

    String getFileName();

}
