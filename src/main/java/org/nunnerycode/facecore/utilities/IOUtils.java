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
package org.nunnerycode.facecore.utilities;

import org.nunnerycode.kern.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;

/**
 * A utility class that contains a few useful methods for dealing with I/O.
 */
public final class IOUtils {

    private IOUtils() {
        // do nothing
    }

    /**
     * Create a {@link java.io.File} at the given path.
     * @param path path to File
     * @return if creation was successful
     */
    public static boolean createFile(String path) {
        Validate.notNull(path, "path cannot be null");
        return createFile(new File(path));
    }

    /**
     * Create a {@link java.io.File} from the given {@link java.io.File}.
     * @param file File to create
     * @return if creation was successful
     */
    public static boolean createFile(File file) {
        Validate.notNull(file, "file cannot be null");
        boolean succeeded = file.exists();
        if (!succeeded) {
            try {
                succeeded = file.isDirectory() ? file.mkdirs() : file.createNewFile();
            } catch (IOException ignored) {
                // do nothing here
            }
        }
        return succeeded;
    }

}
