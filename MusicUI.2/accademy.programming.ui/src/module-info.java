module accademy.programming.ui {
    requires javafx.fxml;
    requires javafx.controls;
    requires accademy.programming.db;
    requires java.sql;
    requires accademy.programming.common;
    requires java.desktop;

    exports accademy.programming.ui;
//    opens accademy.programming.common to javafx.base;
    opens accademy.programming.ui;
}