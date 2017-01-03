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
package com.tealcube.minecraft.bukkit.facecore.database;

import java.sql.Connection;

/**
 * An abstract base class for all database representations.
 */
public abstract class Database {

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
     * If {@link Database#isPool()} is true, returns a new
     * {@link java.sql.Connection}, otherwise returns available {@link java.sql.Connection}.
     * @return Connection for use
     */
    public abstract Connection getConnection();

}
