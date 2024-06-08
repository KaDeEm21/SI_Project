module com.example.si_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.si_project to javafx.fxml;
    exports com.example.si_project;
}