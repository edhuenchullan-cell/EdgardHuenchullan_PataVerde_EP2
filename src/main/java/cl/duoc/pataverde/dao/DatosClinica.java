package cl.duoc.pataverde.dao;

import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Perro;

import java.util.ArrayList;
import java.util.List;

public class DatosClinica {

    private List<Perro> perros = new ArrayList<>();
    private List<Gato> gatos = new ArrayList<>();

    public List<Perro> getPerros() {
        return perros;
    }

    public List<Gato> getGatos() {
        return gatos;
    }
}