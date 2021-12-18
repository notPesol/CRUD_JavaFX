module com.example.productcrud_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;


    opens com.example.productcrud_javafx to javafx.fxml;
    exports com.example.productcrud_javafx;
}