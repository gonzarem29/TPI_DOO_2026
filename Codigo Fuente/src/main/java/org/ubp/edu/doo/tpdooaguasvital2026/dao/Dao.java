package org.ubp.edu.doo.tpdooaguasvital2026.dao;

import java.util.List;

public interface Dao<T> {
    T buscar(T criterio);
    List<T> listarPorCriterio(T criterio);
    List<T> listarTodos();
    boolean insertar(T obj);
    boolean modificar(T obj);
    boolean borrar(T obj);
}
