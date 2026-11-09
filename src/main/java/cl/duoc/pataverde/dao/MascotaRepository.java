package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Mascota;

import java.util.ArrayList;
import java.util.List;

public class MascotaRepository {

    private final MascotaDAO dao;
    private final List<Mascota> mascotas = new ArrayList<>();

    public MascotaRepository(MascotaDAO dao) {
        this.dao = dao;
    }

    public void cargarDatos() {
        mascotas.clear();
        mascotas.addAll(dao.cargar());
    }

    public List<Mascota> listar() {
        return new ArrayList<>(mascotas);
    }

    public void agregar(Mascota mascota) {
        List<Mascota> copia = new ArrayList<>(mascotas);
        copia.add(mascota);
        dao.guardar(copia);
        mascotas.add(mascota);
    }

    public void eliminar(Mascota mascota) {
        List<Mascota> copia = new ArrayList<>(mascotas);
        copia.remove(mascota);
        dao.guardar(copia);
        mascotas.remove(mascota);
    }
}