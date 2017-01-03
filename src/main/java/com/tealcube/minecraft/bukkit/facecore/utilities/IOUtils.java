/**
 * The MIT License
 * Copyright (c) 2015 Teal Cube Games
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.facecore.utilities;

import org.apache.commons.lang3.Validate;

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
     *
     * @param path path to File
     * @return if creation was successful
     */
    public static boolean createFile(String path) {
        Validate.notNull(path, "path cannot be null");
        return createFile(new File(path));
    }

    /**
     * Create a {@link java.io.File} from the given {@link java.io.File}.
     *
     * @param file File to create
     * @return if creation was successful
     */
    public static boolean createFile(File file) {
        Validate.notNull(file, "file cannot be null");
        boolean succeeded = file.exists();
        if (!succeeded) {
            try {
                if (!createDirectory(file.getParentFile())) {
                    return false;
                }
                succeeded = file.createNewFile();
            } catch (IOException ignored) {
                // do nothing here
            }
        }
        return succeeded;
    }

    /**
     * Create a directory at the given path.
     *
     * @param path path to directory
     * @return if creation was successful
     */
    public static boolean createDirectory(String path) {
        Validate.notNull(path, "path cannot be null");
        return createDirectory(new File(path));
    }

    /**
     * Create a directory from the given {@link java.io.File}.
     *
     * @param file directory to create
     * @return if creation was successful
     */
    public static boolean createDirectory(File file) {
        Validate.notNull(file, "file cannot be null");
        return file.exists() || file.mkdirs();
    }

}
