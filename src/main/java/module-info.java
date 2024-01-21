module io.rik72.aftertaste {
    requires transitive javafx.controls;
    requires javafx.fxml;
    opens io.rik72.aftertaste to javafx.fxml;
    exports io.rik72.aftertaste;
}