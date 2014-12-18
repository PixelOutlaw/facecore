package org.nunnerycode.facecore.logging;

import org.bukkit.plugin.Plugin;
import org.nunnerycode.facecore.utilities.IOUtils;
import org.nunnerycode.kern.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.logging.Level;

/**
 * A class that logs given messages to a file.
 */
public final class PluginLogger {

    private final File file;

    /**
     * Constructs a new PluginLogger with a debug file in the given Plugin's data folder.
     * @param plugin Plugin owning this PluginLogger
     */
    public PluginLogger(Plugin plugin) {
        this.file = new File(plugin.getDataFolder(), "debug.log");
    }

    /**
     * Constructs a new PluginLogger with the given debug file.
     * @param file File to log to
     */
    public PluginLogger(File file) {
        this.file = file;
    }

    /**
     * Logs a message with a default {@link java.util.logging.Level} of {@link java.util.logging.Level#INFO}.
     * @param message message to log
     */
    public void log(String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message with a given {@link java.util.logging.Level}.
     * @param level Level of logging message
     * @param message message to log
     */
    public void log(Level level, String message) {
        Validate.notNull(level, "level cannot be null");
        Validate.notNull(message, "message cannot be null");
        Validate.notNull(file, "file cannot be null");
        File writeFile = file;
        if (!IOUtils.createFile(writeFile)) {
            return;
        }
        try (FileWriter fileWriter = new FileWriter(writeFile, true); PrintWriter writer = new PrintWriter(fileWriter)) {
            writer.println("[" + level.getName() + "] " + Calendar.getInstance().getTime().toString() + " | " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs messages with a default {@link java.util.logging.Level} of {@link java.util.logging.Level#INFO}.
     * @param messages messages to log
     */
    public void log(Iterable<String> messages) {
        log(Level.INFO, messages);
    }

    /**
     * Logs messages with a given {@link java.util.logging.Level}.
     * @param level Level of logging message
     * @param messages messages to log
     */
    public void log(Level level, Iterable<String> messages) {
        Validate.notNull(level, "level cannot be null");
        Validate.notNull(messages, "message cannot be null");
        Validate.notNull(file, "file cannot be null");
        File writeFile = file;
        if (!IOUtils.createFile(writeFile)) {
            return;
        }
        try (FileWriter fileWriter = new FileWriter(writeFile, true); PrintWriter writer = new PrintWriter(fileWriter)) {
            String time = Calendar.getInstance().getTime().toString();
            for (String message : messages) {
                writer.println("[" + level.getName() + "] " + time + " | " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
