package com.example.productcrud_javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.events.MouseEvent;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public TextField idField;
    public TextField nameField;
    public TextField codeField;
    public TextField priceField;
    public TextField remainField;

    public Button buttonAdd;
    public Button buttonUpdate;
    public Button buttonDelete;
    public Button buttonClear;

    public TableView<Product> tableView;
    public TableColumn<Product, Integer> columnId;
    public TableColumn<Product, String> columnName;
    public TableColumn<Product, String> columnCode;
    public TableColumn<Product, Float> columnPrice;
    public TableColumn<Product, Integer> columnRemain;

    private boolean isOnUpdate = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("init method");
        idField.setDisable(true);
        buttonUpdate.setDisable(true);
        buttonDelete.setDisable(true);
        buttonClear.setDisable(true);
        showProducts();
    }

    public void onClick(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == buttonAdd) {
            System.out.println("Call insertProduct method");
            insertProduct();
        } else if (source == buttonUpdate) {
            System.out.println("Call updateProduct method");
            updateProduct();
        } else if (source == buttonDelete) {
            System.out.println("Call deleteProduct method");
            deleteProduct();
        } else if (source == buttonClear) {
            System.out.println("Call clear method");
            clear();
        }
    }

    @FXML
    public void onRowClicked() {
        Product product = tableView.getSelectionModel().getSelectedItem();
        if (product == null) {
            return;
        }
        clear();
        isOnUpdate = true;
        setButton();
        setField(product);
    }


    private Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/db_example";
        String user = "springuser";
        String pass = "1234";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            showSQLException(ex);
        }
        return connection;
    }

    private ObservableList<Product> getProductList() {
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        Connection con = getConnection();
        String query = "SELECT * FROM product";
        Statement st;
        ResultSet rs;
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            Product product;
            while (rs.next()) {
                product = new Product(rs.getInt("id"), rs.getString("name"),
                        rs.getString("code"), rs.getFloat("price"), rs.getInt("remain"));
                productsList.add(product);
            }
        } catch (SQLException ex) {
            showSQLException(ex);
        }
        return productsList;
    }

    private void showProducts() {
        ObservableList<Product> productsList = getProductList();

        columnId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnCode.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));
        columnRemain.setCellValueFactory(new PropertyValueFactory<Product, Integer>("remain"));

        tableView.setItems(productsList);

        isOnUpdate = false;

    }

    private int insertProduct() {
        int responseCode = -1;

        if (priceField.getText().trim().isEmpty() || remainField.getText().trim().isEmpty()) {
            return responseCode;
        }

        String name = nameField.getText().trim();
        String code = codeField.getText().trim();
        float price = Float.parseFloat(priceField.getText().trim());
        int remain = Integer.parseInt(remainField.getText().trim());

        if (name.isEmpty() || code.isEmpty()) {
            return responseCode;
        }

        String sql = "INSERT INTO product(name, code, price, remain)" +
                "VALUES(?, ?, ?, ?)";
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, code);
            ps.setFloat(3, price);
            ps.setInt(4, remain);
            responseCode = ps.executeUpdate();

        } catch (SQLException e) {
            showSQLException(e);
        }
        clear();
        showProducts();
        return responseCode;
    }

    private int updateProduct() {
        int responseCode = -1;

        if (priceField.getText().trim().isEmpty() || remainField.getText().trim().isEmpty()) {
            return responseCode;
        }

        String name = nameField.getText().trim();
        String code = codeField.getText().trim();
        float price = Float.parseFloat(priceField.getText().trim());
        int remain = Integer.parseInt(remainField.getText().trim());
        int id = Integer.parseInt(idField.getText());

        if (name.isEmpty() || code.isEmpty() || id < 0 || price < 0 || remain < 0) {
            return responseCode;
        }

        String sql = "UPDATE product SET name = ?, code = ?, price = ?, remain = ? WHERE id = ?";
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, code);
            ps.setFloat(3, price);
            ps.setInt(4, remain);
            ps.setInt(5, id);
            responseCode = ps.executeUpdate();
        } catch (SQLException ex) {
            showSQLException(ex);
        }
        clear();
        showProducts();
        isOnUpdate = false;
        setButton();
        return responseCode;
    }

    private int deleteProduct() {
        int responseCode = -1;

        if (idField.getText().isEmpty()) {
            return responseCode;
        }

        int id = Integer.parseInt(idField.getText());

        String sql = "DELETE FROM product WHERE id = ?";
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            responseCode = ps.executeUpdate();
        }catch (SQLException ex) {
            showSQLException(ex);
        }
        clear();
        showProducts();
        isOnUpdate = false;
        setButton();
        return responseCode;
    }

    private void setField(Product product) {
        idField.setText(String.valueOf(product.getId()));
        nameField.setText(product.getName());
        codeField.setText(product.getCode());
        priceField.setText(String.valueOf(product.getPrice()));
        remainField.setText(String.valueOf(product.getRemain()));
    }

    private void clear() {
        idField.setText("");
        nameField.setText("");
        codeField.setText("");
        priceField.setText("");
        remainField.setText("");
        isOnUpdate = false;
        setButton();
    }

    private void showSQLException(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        ex.printStackTrace();
    }

    private void setButton() {
        buttonAdd.setDisable(isOnUpdate);
        buttonClear.setDisable(!isOnUpdate);
        buttonUpdate.setDisable(!isOnUpdate);
        buttonDelete.setDisable(!isOnUpdate);
    }


}