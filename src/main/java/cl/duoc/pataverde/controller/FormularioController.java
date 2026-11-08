package cl.duoc.pataverde.controller;

import cl.duoc.pataverde.dao.MascotaRepository;
import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Mascota;
import cl.duoc.pataverde.model.Perro;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormularioController {

    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextField txtPeso;
    @FXML private TextField txtRaza;
    @FXML private CheckBox chkVacunado;
    @FXML private CheckBox chkExterior;
    @FXML private Label lblError;

    private MascotaRepository repositorio;
    private Mascota mascotaRegistrada;

    @FXML
    private void initialize() {
        cmbTipo.getItems().addAll("Perro", "Gato");
        cmbTipo.setValue("Perro");
    }

    public void setRepositorio(MascotaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Mascota getMascotaRegistrada() {
        return mascotaRegistrada;
    }

    @FXML
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String textoEdad = txtEdad.getText().trim();
        String textoPeso = txtPeso.getText().trim();
        String raza = txtRaza.getText().trim();
        boolean esPerro = "Perro".equals(cmbTipo.getValue());

        if (nombre.isEmpty() || textoEdad.isEmpty() || textoPeso.isEmpty()) {
            lblError.setText("Nombre, edad y peso son obligatorios.");
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(textoEdad);
        } catch (NumberFormatException e) {
            lblError.setText("La edad debe ser un número entero (ejemplo: 3).");
            return;
        }
        if (edad < 0 || edad > 30) {
            lblError.setText("La edad debe estar entre 0 y 30 años.");
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(textoPeso.replace(',', '.'));
        } catch (NumberFormatException e) {
            lblError.setText("El peso debe ser un número (ejemplo: 4.5).");
            return;
        }
        if (peso <= 0) {
            lblError.setText("El peso debe ser mayor que cero.");
            return;
        }


        if (esPerro && raza.isEmpty()) {
            lblError.setText("La raza es obligatoria para los perros.");
            return;
        }

        Mascota nueva;
        if (esPerro) {
            nueva = new Perro(nombre, edad, peso, raza, chkVacunado.isSelected());
        } else {
            nueva = new Gato(nombre, edad, peso, chkExterior.isSelected());
        }

        repositorio.agregar(nueva);
        mascotaRegistrada = nueva;
        cerrar();
    }

    @FXML
    private void cancelar() {
        cerrar();
    }

    private void cerrar() {
        Stage ventana = (Stage) txtNombre.getScene().getWindow();
        ventana.close();
    }
}