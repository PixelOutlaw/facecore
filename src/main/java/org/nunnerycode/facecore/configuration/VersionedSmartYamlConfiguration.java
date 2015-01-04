package org.nunnerycode.facecore.configuration;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * An extension of IvoryYamlConfiguration that can backup and update itself.
 */
public class VersionedSmartYamlConfiguration extends SmartYamlConfiguration implements VersionedSmartConfiguration {

    private YamlConfiguration checkAgainst;
    private VersionUpdateType updateType;

    /**
     * Instantiates a new VersionedIvoryYamlConfiguration with a selected {@link java.io.File} to load/save from/to, a
     * {@link java.io.File} to check against, and an {@link VersionedSmartConfiguration.VersionUpdateType}.
     *
     * @param file         file to load/save from/to
     * @param checkAgainst file to check against
     * @param updateType   type of updating
     */
    public VersionedSmartYamlConfiguration(File file, File checkAgainst,
                                           VersionUpdateType updateType) {
        this(file, '.', checkAgainst, updateType);
    }

    /**
     * Instantiates a new VersionedIvoryYamlConfiguration with a selected {@link java.io.File} to load/save from/to, a
     * {@link java.io.InputStream} to check against, and an {@link VersionedSmartConfiguration.VersionUpdateType}.
     *
     * @param file         file to load/save from/to
     * @param checkAgainst resource to check against
     * @param updateType   type of updating
     */
    public VersionedSmartYamlConfiguration(File file, InputStream checkAgainst,
                                           VersionUpdateType updateType) {
        this(file, '.', checkAgainst, updateType);
    }

    /**
     * Instantiates a new VersionedIvoryYamlConfiguration with a selected {@link java.io.File} to load/save from/to, a
     * {@link java.io.File} to check against, and an {@link VersionedSmartConfiguration.VersionUpdateType}.
     *
     * @param file         file to load/save from/to
     * @param separator    character to separate file sections on
     * @param checkAgainst file to check against
     * @param updateType   type of updating
     */
    public VersionedSmartYamlConfiguration(File file, char separator, File checkAgainst,
                                           VersionUpdateType updateType) {
        super(file, separator);
        if (checkAgainst != null && checkAgainst.exists()) {
            this.checkAgainst = new SmartYamlConfiguration(checkAgainst);
        }
        this.updateType = updateType;
    }

    /**
     * Instantiates a new VersionedIvoryYamlConfiguration with a selected {@link java.io.File} to load/save from/to, a
     * {@link java.io.InputStream} to check against, and an {@link VersionedSmartConfiguration.VersionUpdateType}.
     *
     * @param file         file to load/save from/to
     * @param separator    character to separate file sections on
     * @param checkAgainst resource to check against
     * @param updateType   type of updating
     */
    public VersionedSmartYamlConfiguration(File file, char separator, InputStream checkAgainst,
                                           VersionUpdateType updateType) {
        super(file, separator);
        if (checkAgainst != null) {
            this.checkAgainst = YamlConfiguration.loadConfiguration(checkAgainst);
        }
        this.updateType = updateType;
    }

    /**
     * Gets and returns the version passed into the constructor.
     *
     * @return version passed into the constructor
     */
    @Override
    public String getVersion() {
        return checkAgainst == null ? "" : checkAgainst.getString("version", "");
    }

    /**
     * Gets and returns the version contained in the actual YAML file.
     *
     * @return version in the YAML file
     */
    @Override
    public String getLocalVersion() {
        return getString("version", "");
    }

    /**
     * Returns true if this file needs to update itself and false if not.
     *
     * @return if file needs to update
     */
    @Override
    public boolean needsToUpdate() {
        return getVersion() != null && getLocalVersion() != null && !getLocalVersion()
                .equals(getVersion());
    }

    /**
     * Attempts to update itself and returns if it succeeded.
     *
     * @return if update was successful
     */
    @Override
    public boolean update() {
        if (!needsToUpdate()) {
            return false;
        }
        File directory = getFile().getParentFile();
        File saveTo = new File(directory, getFile().getName().replace(".yml", ".yml.backup"));
        switch (updateType) {
            case BACKUP_NO_UPDATE:
                try {
                    if (getFile().exists()) {
                        save(saveTo);
                    }
                } catch (IOException e) {
                    return false;
                }
                return true;
            case BACKUP_AND_UPDATE:
                try {
                    if (getFile().exists()) {
                        save(saveTo);
                    }
                } catch (IOException e) {
                    return false;
                }
                for (String key : checkAgainst.getKeys(true)) {
                    if (checkAgainst.isConfigurationSection(key)) {
                        continue;
                    }
                    if (!isSet(key)) {
                        set(key, checkAgainst.get(key));
                    }
                }
                set("version", getVersion());
                save();
                return true;
            case BACKUP_AND_NEW:
                try {
                    if (getFile().exists()) {
                        save(saveTo);
                    }
                } catch (IOException e) {
                    return false;
                }
                for (String key : checkAgainst.getKeys(true)) {
                    if (checkAgainst.isConfigurationSection(key)) {
                        continue;
                    }
                    set(key, checkAgainst.get(key));
                }
                set("version", getVersion());
                save();
                return true;
            default:
                return true;
        }
    }


}
