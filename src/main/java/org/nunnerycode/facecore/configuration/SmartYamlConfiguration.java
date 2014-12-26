package org.nunnerycode.facecore.configuration;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SmartYamlConfiguration extends YamlConfiguration implements SmartConfiguration {
    private final File file;

    public SmartYamlConfiguration(File file) {
        super();
        this.file = file;
        load();
    }

    @Override
    public void save() {
        try {
            save(this.file);
        } catch (Exception ignored) {
            // do nothing
        }
    }

    @Override
    public void load() {
        try {
            load(this.file);
        } catch (Exception ignored) {
            // do nothing
        }
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public String getFileName() {
        return getFile().getName().substring(0, getFile().getName().lastIndexOf("."));
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (file != null ? file.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmartYamlConfiguration)) {
            return false;
        }

        SmartYamlConfiguration that = (SmartYamlConfiguration) o;

        return super.equals(o) && !(file != null ? !file.equals(that.file) : that.file != null);
    }
}
