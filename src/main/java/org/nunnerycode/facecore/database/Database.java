package org.nunnerycode.facecore.database;

import org.nunnerycode.facecore.database.settings.DatabaseSettings;

import java.sql.Connection;

/**
 * An abstract base class for all database representations.
 */
public abstract class Database {

    private final DatabaseSettings databaseSettings;

    protected Database(DatabaseSettings settings) {
        this.databaseSettings = settings;
    }

    /**
     * Initializes the database connection. Returns true if successful, false if not.
     * @return success
     */
    public abstract boolean initialize();

    /**
     * Shuts down the database connection. Returns true if successful, false if not.
     * @return success
     */
    public abstract boolean shutdown();

    /**
     * Returns true if the database uses a pool of {@link java.sql.Connection}s.
     * @return if database uses pool of Connections.
     */
    public abstract boolean isPool();

    /**
     * Returns a {@link java.sql.Connection} for use.
     *
     * If {@link org.nunnerycode.facecore.database.Database#isPool()} is true, returns a new
     * {@link java.sql.Connection}, otherwise returns available {@link java.sql.Connection}.
     * @return Connection for use
     */
    public abstract Connection getConnection();

    /**
     * Gets and returns the {@link org.nunnerycode.facecore.database.settings.DatabaseSettings} for this Database.
     * @return settings
     */
    public final DatabaseSettings getDatabaseSettings() {
        return databaseSettings;
    }

}
