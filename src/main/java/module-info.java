module org.allvens {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    requires java.desktop;

    opens org.allvens to javafx.fxml;
    opens org.allvens.controller to javafx.fxml;
    exports org.allvens;
}