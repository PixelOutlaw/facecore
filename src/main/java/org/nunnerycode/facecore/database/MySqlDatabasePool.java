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
