package cl.duoc.pataverde.controller;

import cl.duoc.pataverde.dao.MascotaDAOJson;
import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Mascota;
import cl.duoc.pataverde.model.Perro;
import cl.duoc.pataverde.repository.MascotaRepository;          // CAMBIO: nuevo paquete
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    // Tabla
    @FXML private TableView<Mascota> tblMascotas;
    @FXML private TableColumn<Mascota, String> colTipo;
    @FXML private TableColumn<Mascota, String> colNombre;
    @FXML private TableColumn<Mascota, Integer> colEdad;
    @FXML private TableColumn<Mascota, Double> colPeso;
    @FXML private TableColumn<Mascota, String> colCosto;

    // Formulario
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextField txtPeso;
    @FXML private TextField txtRaza;
    @FXML private CheckBox chkVacunado;
    @FXML private CheckBox chkAdopcion;
    @FXML private CheckBox chkExterior;

    // Búsqueda y mensajes
    @FXML private TextField txtBuscar;
    @FXML private Label lblMensaje;

    private final MascotaRepository repositorio = new MascotaRepository(new MascotaDAOJson());
    // CAMBIO: ya no hay una ObservableList propia; el filtro se pone sobre la lista del Repository
    private final FilteredList<Mascota> mascotasFiltradas = new FilteredList<>(repositorio.getMascotas());

    @FXML
    private void initialize() {
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colCosto.setCellValueFactory(fila ->
                new SimpleStringProperty(formatearCosto(fila.getValue().calcularCostoConsulta())));
        tblMascotas.setItems(mascotasFiltradas);

        cmbTipo.getItems().addAll("Perro", "Gato");
        cambiarTipo();

        txtBuscar.textProperty().addListener((obs, anterior, nuevo) -> filtrar(nuevo));

        try {
            repositorio.cargarDatos();                          // CAMBIO: ya no se copia a otra lista
            mostrarInfo("Se cargaron " + repositorio.getMascotas().size() + " mascotas.");
        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void cambiarTipo() {
        boolean esPerro = "Perro".equals(cmbTipo.getValue());
        boolean esGato = "Gato".equals(cmbTipo.getValue());

        txtRaza.setDisable(!esPerro);
        chkVacunado.setDisable(!esPerro);
        chkAdopcion.setDisable(!esPerro);
        chkExterior.setDisable(!esGato);
    }

    @FXML
    private void registrar() {
        String tipo = cmbTipo.getValue();
        String nombre = txtNombre.getText().trim();
        String textoEdad = txtEdad.getText().trim();
        String textoPeso = txtPeso.getText().trim();
        String raza = txtRaza.getText().trim();

        if (tipo == null) {
            mostrarError("Tipo: debe seleccionar Perro o Gato.");
            return;
        }

        if (nombre.isEmpty()) {
            mostrarError("Nombre: no puede estar vacío.");
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(textoEdad);
        } catch (NumberFormatException e) {
            mostrarError("Edad: debe ser un número entero.");
            return;
        }
        if (edad < 0 || edad > 30) {
            mostrarError("Edad: debe estar entre 0 y 30 años.");
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(textoPeso.replace(',', '.'));
        } catch (NumberFormatException e) {
            mostrarError("Peso: debe ser un número (ejemplo: 4.5).");
            return;
        }
        if (peso <= 0) {
            mostrarError("Peso: debe ser mayor que cero.");
            return;
        }

        boolean esPerro = tipo.equals("Perro");
        if (esPerro && raza.isEmpty()) {
            mostrarError("Raza: no puede estar vacía.");
            return;
        }

        Mascota nueva;
        if (esPerro) {
            Perro perro = new Perro(nombre, edad, peso, raza, chkVacunado.isSelected());
            if (chkAdopcion.isSelected()) {
                perro.marcarDisponibleParaAdopcion();
            }
            nueva = perro;
        } else {
            nueva = new Gato(nombre, edad, peso, chkExterior.isSelected());
        }

        try {
            repositorio.agregar(nueva);                         // CAMBIO: ya no hace falta mascotas.add(...)
        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
            return;
        }
        mostrarExito(nueva.getNombre() + " (" + nueva.getTipo() + ") registrado correctamente.");
        limpiarFormulario();
    }

    @FXML
    private void eliminar() {
        Mascota seleccionada = tblMascotas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarError("Debe seleccionar una mascota en la tabla para eliminarla.");
            return;
        }

        try {
            repositorio.eliminar(seleccionada);                 // CAMBIO: ya no hace falta mascotas.remove(...)
        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
            return;
        }
        mostrarExito(seleccionada.getNombre() + " (" + seleccionada.getTipo() + ") eliminado correctamente.");
    }

    private void filtrar(String texto) {
        String criterio = texto.trim().toLowerCase();

        mascotasFiltradas.setPredicate(mascota -> {
            if (criterio.isEmpty()) {
                return true;
            }
            String nombre = mascota.getNombre().toLowerCase();
            String tipo = mascota.getTipo().toLowerCase();
            return nombre.contains(criterio) || tipo.contains(criterio);
        });

        mostrarInfo("Mostrando " + mascotasFiltradas.size() + " de " + repositorio.getMascotas().size() + " mascotas.");
    }

    private void limpiarFormulario() {
        cmbTipo.setValue(null);
        txtNombre.clear();
        txtEdad.clear();
        txtPeso.clear();
        txtRaza.clear();
        chkVacunado.setSelected(false);
        chkAdopcion.setSelected(false);
        chkExterior.setSelected(false);
        cambiarTipo();
    }

    private String formatearCosto(double costo) {
        return String.format("$%,.0f", costo).replace(',', '.');
    }

    private void mostrarInfo(String texto) {
        lblMensaje.setStyle("");
        lblMensaje.setText(texto);
    }

    private void mostrarExito(String texto) {
        lblMensaje.setStyle("-fx-text-fill: green;");
        lblMensaje.setText(texto);
    }

    private void mostrarError(String texto) {
        lblMensaje.setStyle("-fx-text-fill: red;");
        lblMensaje.setText(texto);
    }
}