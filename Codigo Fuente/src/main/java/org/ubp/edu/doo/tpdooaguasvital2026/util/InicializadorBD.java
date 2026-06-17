package org.ubp.edu.doo.tpdooaguasvital2026.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public class InicializadorBD {

    public static void inicializar() {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:tp-doo-aguas-vital-2026.db";
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                InputStream is = InicializadorBD.class.getResourceAsStream("/org/ubp/edu/doo/tpdooaguasvital2026/esquema-aguas-vital.sql");
                if (is == null) {
                    System.out.println("ERROR: No se encontro el archivo esquema-aguas-vital.sql");
                    return;
                }
                String sql = new BufferedReader(new InputStreamReader(is))
                        .lines()
                        .filter(line -> !line.trim().startsWith("--") && !line.trim().isEmpty())
                        .collect(Collectors.joining("\n"));

                String[] sentencias = sql.split(";");
                for (String s : sentencias) {
                    String trimmed = s.trim();
                    if (trimmed.isEmpty()) {
                        continue;
                    }
                    try {
                        stmt.execute(trimmed);
                    } catch (Exception e) {
                        System.err.println("Error ejecutando: " + trimmed.substring(0, Math.min(80, trimmed.length())));
                        System.err.println(e.getMessage());
                    }
                }
            }
            System.out.println("Base de datos inicializada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al inicializar BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        inicializar();
    }
}
