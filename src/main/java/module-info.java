module cl.duoc.pataverde {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens cl.duoc.pataverde.model to javafx.base, com.google.gson;
    opens cl.duoc.pataverde.dao to com.google.gson;
    opens cl.duoc.pataverde.controller to javafx.fxml;

    exports cl.duoc.pataverde;
}