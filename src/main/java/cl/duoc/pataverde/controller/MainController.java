package cl.duoc.pataverde.controller;

import cl.duoc.pataverde.dao.MascotaDAOJson;
import cl.duoc.pataverde.dao.MascotaRepository;
import cl.duoc.pataverde.model.Mascota;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
}