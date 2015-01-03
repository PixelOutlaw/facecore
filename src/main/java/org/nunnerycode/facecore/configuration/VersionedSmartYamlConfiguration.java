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

import java.io.*;

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
    }

    @Override
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
    }

    @Override
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
