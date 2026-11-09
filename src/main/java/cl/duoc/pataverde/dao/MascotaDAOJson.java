package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Mascota;
import cl.duoc.pataverde.model.Perro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAOJson implements MascotaDAO {

    private final Path archivoPerros = Path.of("perros.json");
    private final Path archivoGatos = Path.of("gatos.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public List<Mascota> cargar() {
        List<Mascota> mascotas = new ArrayList<>();

        try {
            if (Files.exists(archivoPerros)) {
                Perro[] perros = gson.fromJson(Files.readString(archivoPerros), Perro[].class);
                if (perros != null) {
                    for (Perro perro : perros) {
                        mascotas.add(perro);
                    }
                }
            }

            if (Files.exists(archivoGatos)) {
                Gato[] gatos = gson.fromJson(Files.readString(archivoGatos), Gato[].class);
                if (gatos != null) {
                    for (Gato gato : gatos) {
                        mascotas.add(gato);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error al leer los archivos de datos.");
        }

        return mascotas;
    }

    @Override
    public void guardar(List<Mascota> mascotas) {
        List<Perro> perros = new ArrayList<>();
        List<Gato> gatos = new ArrayList<>();

        for (Mascota mascota : mascotas) {
            if (mascota instanceof Perro) {
                perros.add((Perro) mascota);
            } else if (mascota instanceof Gato) {
                gatos.add((Gato) mascota);
            }
        }

        try {
            Files.writeString(archivoPerros, gson.toJson(perros));
            Files.writeString(archivoGatos, gson.toJson(gatos));
        } catch (Exception e) {
            throw new IllegalStateException("Error al guardar los archivos de datos.");
        }
    }
}