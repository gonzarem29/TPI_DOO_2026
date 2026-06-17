package org.ubp.edu.doo.tpdooaguasvital2026.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionSql {

    private final String URL = "jdbc:sqlite:tp-doo-aguas-vital-2026.db";
    private Connection connection = null;

    public ConexionSql() {
        abrir();
    }

    private void abrir() {
        try {
            this.connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            this.connection = null;
        }
    }

    public void cerrar() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            this.connection = null;
        }
    }

    public String getURL() {
        return URL;
    }

    public Connection getConnection() {
        return connection;
    }

}
