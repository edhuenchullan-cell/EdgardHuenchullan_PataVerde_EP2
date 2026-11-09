package cl.duoc.pataverde.repository;

import cl.duoc.pataverde.dao.MascotaDAO;
import cl.duoc.pataverde.model.Mascota;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MascotaRepository {

    private final MascotaDAO dao;
    private final ObservableList<Mascota> mascotas = FXCollections.observableArrayList();

    public MascotaRepository(MascotaDAO dao) {
        this.dao = dao;
    }

    public void cargarDatos() {
        mascotas.setAll(dao.cargar());
    }

    public ObservableList<Mascota> getMascotas() {
        return FXCollections.unmodifiableObservableList(mascotas);
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