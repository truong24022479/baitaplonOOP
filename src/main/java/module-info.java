module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.desktop;
    //requires java.desktop;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}