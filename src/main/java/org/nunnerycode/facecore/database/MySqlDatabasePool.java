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

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.nunnerycode.facecore.database.settings.MySqlSettings;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySqlDatabasePool extends Database {
    private PoolingDataSource<PoolableConnection> poolingDataSource;
    public MySqlDatabasePool(MySqlSettings settings) {
        super(settings);
    }

    @Override
    public boolean initialize() {
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(getDatabaseSettings().toString(),
                getDatabaseSettings().getUsername(), getDatabaseSettings().getPassword());
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
        poolableConnectionFactory.setPool(connectionPool);
        poolingDataSource = new PoolingDataSource<>(connectionPool);
        return true;
    }

    @Override
    public boolean shutdown() {
        poolingDataSource = null;
        return true;
    }

    @Override
    public boolean isPool() {
        return true;
    }

    @Override
    public Connection getConnection() {
        if (poolingDataSource == null) {
            throw new IllegalStateException("database pool is not initialized");
        }
        try {
            return poolingDataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("database pool is unavailable");
        }
    }
}
