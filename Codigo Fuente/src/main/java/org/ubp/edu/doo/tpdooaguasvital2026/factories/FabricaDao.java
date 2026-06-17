package org.ubp.edu.doo.tpdooaguasvital2026.factories;

import org.ubp.edu.doo.tpdooaguasvital2026.dao.Dao;

public class FabricaDao {
    public static Dao fabricar(String nombreDao) {
        try {
            Class<?> daoClass = Class.forName("org.ubp.edu.doo.tpdooaguasvital2026.dao." + nombreDao);
            return (Dao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error al fabricar DAO: " + nombreDao, e);
        }
    }
}
