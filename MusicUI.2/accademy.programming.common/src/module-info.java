module accademy.programming.common {


    requires java.desktop;
    requires javafx.base;

    exports accademy.programming.common;
    opens accademy.programming.common to javafx.base;
//    opens accademy.programming.ui;

}