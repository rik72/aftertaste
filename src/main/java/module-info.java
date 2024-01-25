module io.rik72 {
    requires jakarta.persistence;
    requires transitive javafx.controls;
    requires java.naming;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires org.yaml.snakeyaml;
    opens io.rik72.aftertaste to javafx.fxml;
    exports io.rik72.aftertaste;
}
