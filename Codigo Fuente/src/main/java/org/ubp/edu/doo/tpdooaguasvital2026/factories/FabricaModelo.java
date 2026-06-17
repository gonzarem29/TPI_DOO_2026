package org.ubp.edu.doo.tpdooaguasvital2026.factories;

public class FabricaModelo {
    public static Object fabricar(String nombreModelo) {
        try {
            Class<?> modeloClass = Class.forName("org.ubp.edu.doo.tpdooaguasvital2026.modelo." + nombreModelo);
            return modeloClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error al fabricar Modelo: " + nombreModelo, e);
        }
    }
}
