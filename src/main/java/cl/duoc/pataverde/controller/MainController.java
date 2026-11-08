package cl.duoc.pataverde.controller;

import cl.duoc.pataverde.dao.MascotaDAOJson;
import cl.duoc.pataverde.dao.MascotaRepository;
import cl.duoc.pataverde.model.Mascota;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML private TableView<Mascota> tblMascotas;
    @FXML private TableColumn<Mascota, String> colTipo;
    @FXML private TableColumn<Mascota, String> colNombre;
    @FXML private TableColumn<Mascota, Integer> colEdad;
    @FXML private TableColumn<Mascota, Double> colPeso;
    @FXML private Label lblMensaje;
    @FXML private TextField txtBuscar;

    private final MascotaRepository repositorio = new MascotaRepository(new MascotaDAOJson());
    private final ObservableList<Mascota> mascotas = FXCollections.observableArrayList();
    private final FilteredList<Mascota> mascotasFiltradas = new FilteredList<>(mascotas);   // NUEVO

    @FXML
    private void initialize() {
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        mascotas.setAll(repositorio.listar());
        tblMascotas.setItems(mascotasFiltradas);


        txtBuscar.textProperty().addListener((obs, anterior, nuevo) -> filtrar(nuevo));

        lblMensaje.setText("Se cargaron " + mascotas.size() + " mascotas.");
    }

    // NUEVO
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

        lblMensaje.setStyle("");
        lblMensaje.setText("Mostrando " + mascotasFiltradas.size() + " de " + mascotas.size() + " mascotas.");
    }

    // NUEVO
    @FXML
    private void eliminar() {
        Mascota seleccionada = tblMascotas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarError("Selecciona una mascota de la tabla para eliminarla.");
            return;
        }

        repositorio.eliminar(seleccionada);
        mascotas.remove(seleccionada);
        mostrarExito("Se eliminó a " + seleccionada.getNombre() + " (" + seleccionada.getTipo() + ").");
    }

    @FXML
    private void abrirFormulario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cl/duoc/pataverde/view/formulario-view.fxml"));
            Parent vista = loader.load();

            FormularioController formulario = loader.getController();
            formulario.setRepositorio(repositorio);

            Stage ventana = new Stage();
            ventana.setTitle("Registrar mascota");
            ventana.setScene(new Scene(vista));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.showAndWait();

            Mascota nueva = formulario.getMascotaRegistrada();
            if (nueva != null) {
                mascotas.add(nueva);
                mostrarExito("Se registró a " + nueva.getNombre() + " (" + nueva.getTipo() + ").");
            } else {
                lblMensaje.setStyle("");
                lblMensaje.setText("Registro cancelado.");
            }
        } catch (IOException e) {
            mostrarError("No se pudo abrir el formulario de registro.");
        }
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