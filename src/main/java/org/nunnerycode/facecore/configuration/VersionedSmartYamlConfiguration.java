package org.nunnerycode.facecore.configuration;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class VersionedSmartYamlConfiguration extends SmartYamlConfiguration implements VersionedSmartConfiguration {

    private YamlConfiguration checkAgainst;

    public VersionedSmartYamlConfiguration(File file, File checkAgainst) {
        super(file);
        this.checkAgainst = YamlConfiguration.loadConfiguration(checkAgainst);
    }

    public VersionedSmartYamlConfiguration(File file, InputStream checkAgainst) {
        super(file);
        Reader reader = new InputStreamReader(checkAgainst);
        this.checkAgainst = YamlConfiguration.loadConfiguration(reader);
        try {
            reader.close();
        } catch (IOException ignored) {
            // do nothing
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (checkAgainst != null ? checkAgainst.hashCode() : 0);
        return result;
    }    @Override
    public String getResourceVersion() {
        return checkAgainst.getString("version");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VersionedSmartYamlConfiguration)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        VersionedSmartYamlConfiguration that = (VersionedSmartYamlConfiguration) o;

        return super.equals(o) && !(checkAgainst != null ? !checkAgainst.equals(that.checkAgainst) : that.checkAgainst != null);
    }    @Override
    public String getLocalVersion() {
        return getString("version");
    }

    @Override
    public boolean needsToUpdate() {
        return !getLocalVersion().equals(getResourceVersion());
    }

    @Override
    public boolean update() {
        if (!needsToUpdate()) {
            return false;
        }
        for (String key : getKeys(true)) {
            if (isConfigurationSection(key)) {
                continue;
            }
            if (!isSet(key)) {
                set(key, checkAgainst.get(key));
            }
        }
        set("version", getResourceVersion());
        return true;
    }




}
