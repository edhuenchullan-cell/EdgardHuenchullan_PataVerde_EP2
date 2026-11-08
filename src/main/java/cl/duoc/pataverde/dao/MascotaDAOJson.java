package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Mascota;
import cl.duoc.pataverde.model.Perro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAOJson implements MascotaDAO {

    private final Path archivo = Path.of("data", "mascotas.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public List<Mascota> cargar() {
        List<Mascota> mascotas = new ArrayList<>();

        if (!Files.exists(archivo)) {
            return mascotas;
        }

        try {
            String texto = Files.readString(archivo);
            DatosClinica datos = gson.fromJson(texto, DatosClinica.class);
            if (datos != null) {
                mascotas.addAll(datos.getPerros());
                mascotas.addAll(datos.getGatos());
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo de datos.", e);
        }
        return mascotas;
    }

    @Override
    public void guardar(List<Mascota> mascotas) {
        DatosClinica datos = new DatosClinica();

        for (Mascota mascota : mascotas) {
            if (mascota instanceof Perro) {
                datos.getPerros().add((Perro) mascota);
            } else if (mascota instanceof Gato) {
                datos.getGatos().add((Gato) mascota);
            }
        }

        try {
            Files.createDirectories(archivo.getParent());
            Files.writeString(archivo, gson.toJson(datos));
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de datos.", e);
        }
    }
}