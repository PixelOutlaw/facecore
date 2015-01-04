package org.nunnerycode.facecore.database;

import org.nunnerycode.facecore.database.settings.MySqlSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class MySqlDatabase extends Database {
    private Connection connection;

    public MySqlDatabase(MySqlSettings settings) {
        super(settings);
    }

    @Override
    public boolean initialize() {
        if (connection != null) {
            return false;
        }
        // Check for MySQL driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            return false;
        }
        // Initialize the connection
        try {
            connection = DriverManager.getConnection(getDatabaseSettings().toString(),
                    getDatabaseSettings().getUsername(), getDatabaseSettings().getPassword());
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean shutdown() {
        if (connection == null) {
            return false;
        }
        // Attempt to close the connection
        try {
            connection.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isPool() {
        return false;
    }

    @Override
    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("connection isn't initialized");
        }
        return connection;
    }
}
