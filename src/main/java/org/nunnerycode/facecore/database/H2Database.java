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
package org.nunnerycode.facecore.database;

import org.nunnerycode.facecore.database.settings.H2Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class H2Database extends Database {

    private Connection connection;

    public H2Database(H2Settings settings) {
        super(settings);
    }

    @Override
    public boolean initialize() {
        if (connection != null) {
            return false;
        }
        try {
            Class.forName("org.h2.jdbcx.JdbcDataSource");
        } catch (ClassNotFoundException e) {
            return false;
        }
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
