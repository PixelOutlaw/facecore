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
package com.tealcube.minecraft.bukkit.facecore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class MySqlDatabase extends Database {
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String database;
    private Connection connection;

    public MySqlDatabase(String host, String port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
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
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username,
                    password);
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
            connection = null;
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
