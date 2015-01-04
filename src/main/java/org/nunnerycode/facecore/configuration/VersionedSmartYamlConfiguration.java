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
package org.nunnerycode.facecore.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.nunnerycode.kern.apache.commons.lang.Validate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class VersionedSmartYamlConfiguration extends SmartYamlConfiguration implements VersionedSmartConfiguration {

    private YamlConfiguration checkAgainst;

    public VersionedSmartYamlConfiguration(File file, File checkAgainst) {
        super(file);
        Validate.notNull(checkAgainst, "checkAgainst cannot be null");
        this.checkAgainst = YamlConfiguration.loadConfiguration(checkAgainst);
    }

    public VersionedSmartYamlConfiguration(File file, InputStream checkAgainst) {
        super(file);
        Validate.notNull(checkAgainst, "checkAgainst cannot be null");
        this.checkAgainst = YamlConfiguration.loadConfiguration(checkAgainst);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (checkAgainst != null ? checkAgainst.hashCode() : 0);
        return result;
    }

    @Override
    public String getResourceVersion() {
        return checkAgainst != null ? checkAgainst.getString("version") : "";
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
    }

    @Override
    public String getLocalVersion() {
        return getString("version", "");
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
        try {
            save(new File(getFile().getParentFile(), getFileName() + ".old"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String key : getKeys(true)) {
            if (isConfigurationSection(key)) {
                continue;
            }
            set(key, checkAgainst.get(key));
        }
        set("version", getResourceVersion());
        return true;
    }


}
