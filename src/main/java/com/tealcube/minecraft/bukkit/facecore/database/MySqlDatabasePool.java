/*
 * The MIT License
 * Copyright (c) 2015 Teal Cube Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.facecore.database;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySqlDatabasePool extends Database {
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String database;
    private PoolingDataSource<PoolableConnection> poolingDataSource;

    public MySqlDatabasePool(String host, String port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    public boolean initialize() {
        ConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory("jdbc:mysql://" + host + ":" + port + "/" + database,
                        username, password);
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
            throw new IllegalStateException("database pool is unavailable", e);
        }
    }
}
