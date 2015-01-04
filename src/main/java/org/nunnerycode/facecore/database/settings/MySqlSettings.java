package org.nunnerycode.facecore.database.settings;

public final class MySqlSettings implements DatabaseSettings {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String database;

    public MySqlSettings(String host, String port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (database != null ? database.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MySqlSettings that = (MySqlSettings) o;

        return !(database != null ? !database.equals(that.database) : that.database != null) &&
                !(host != null ? !host.equals(that.host) : that.host != null) &&
                !(password != null ? !password.equals(that.password) : that.password != null) &&
                !(port != null ? !port.equals(that.port) : that.port != null) &&
                !(username != null ? !username.equals(that.username) : that.username != null);
    }

    @Override
    public String toString() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

}
