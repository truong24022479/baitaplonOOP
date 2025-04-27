module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
<<<<<<< HEAD
    requires java.desktop;
=======
    requires javafx.base;
    requires java.datatransfer;
>>>>>>> f32b9f7f305b9b9bb86d8b8e364971ec5fc80a1d

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}