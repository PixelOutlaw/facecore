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
                succeeded = file.createNewFile();
            } catch (IOException ignored) {
                // do nothing here
            }
        }
        return succeeded;
    }

}
