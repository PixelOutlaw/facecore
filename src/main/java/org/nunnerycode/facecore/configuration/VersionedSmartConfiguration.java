package org.nunnerycode.facecore.configuration;

public interface VersionedSmartConfiguration extends SmartConfiguration {

    String getVersion();

    String getLocalVersion();

    boolean needsToUpdate();

    boolean update();

    public static enum VersionUpdateType {
        BACKUP_NO_UPDATE, BACKUP_AND_UPDATE, BACKUP_AND_NEW, NOTHING
    }

}
