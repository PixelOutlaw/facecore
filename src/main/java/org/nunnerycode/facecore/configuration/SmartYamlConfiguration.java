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
