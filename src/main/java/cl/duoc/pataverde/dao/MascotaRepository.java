package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Mascota;

import java.util.ArrayList;
import java.util.List;

public class MascotaRepository {

    private final MascotaDAO dao;
    private final List<Mascota> mascotas;

    public MascotaRepository(MascotaDAO dao) {
        this.dao = dao;
        this.mascotas = dao.cargar();
    }

    public List<Mascota> listar() {
        return new ArrayList<>(mascotas);
    }

    public void agregar(Mascota mascota) {
        mascotas.add(mascota);
        dao.guardar(mascotas);
    }

    public void eliminar(Mascota mascota) {
        mascotas.remove(mascota);
        dao.guardar(mascotas);
    }
}