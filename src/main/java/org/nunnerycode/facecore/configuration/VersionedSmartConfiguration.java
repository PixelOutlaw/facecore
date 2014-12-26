package org.nunnerycode.facecore.configuration;

public interface VersionedSmartConfiguration extends SmartConfiguration {

    String getResourceVersion();

    String getLocalVersion();

    boolean needsToUpdate();

    boolean update();

}
