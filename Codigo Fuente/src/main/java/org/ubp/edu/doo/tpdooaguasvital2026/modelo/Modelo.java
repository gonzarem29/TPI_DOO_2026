package org.ubp.edu.doo.tpdooaguasvital2026.modelo;

import org.modelmapper.ModelMapper;
import org.ubp.edu.doo.tpdooaguasvital2026.dao.Dao;

public abstract class Modelo {
    public Dao dao;
    public ModelMapper mapper = new ModelMapper();
}
