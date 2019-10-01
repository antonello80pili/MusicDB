module accademy.programming.db {
    requires java.sql;
    requires sqlite.jdbc;
    requires accademy.programming.common;
//    requires accademy.programming.ui;

    exports accademy.programming.db;
//    opens accademy.programming.common to javafx.base;
    opens accademy.programming.db;
}