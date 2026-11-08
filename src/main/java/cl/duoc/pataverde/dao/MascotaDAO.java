package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Mascota;

import java.util.List;

public interface MascotaDAO {

    List<Mascota> cargar();

    void guardar(List<Mascota> mascotas);
}