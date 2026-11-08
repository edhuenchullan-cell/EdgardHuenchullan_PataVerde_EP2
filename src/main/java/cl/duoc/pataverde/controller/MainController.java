package cl.duoc.pataverde.controller;

import cl.duoc.pataverde.dao.MascotaDAOJson;
import cl.duoc.pataverde.dao.MascotaRepository;
import cl.duoc.pataverde.model.Mascota;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    private final MascotaRepository repositorio = new MascotaRepository(new MascotaDAOJson());
    private final ObservableList<Mascota> mascotas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        mascotas.setAll(repositorio.listar());
        tblMascotas.setItems(mascotas);

        lblMensaje.setText("Se cargaron " + mascotas.size() + " mascotas.");
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