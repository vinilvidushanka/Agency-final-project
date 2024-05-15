package lk.ijse.agency.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.agency.model.Customer;
import lk.ijse.agency.model.tm.CustomerTm;
import lk.ijse.agency.repository.CustomerRepo;
import lk.ijse.agency.repository.ItemRepo;
import lk.ijse.agency.repository.RouteRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormController {

    public TableColumn colID;

    @FXML
    private TableColumn<?, ?> colAddress;
    public TableColumn colName;
    public TableColumn colShopName;
    public TableColumn colContact;
    public TableColumn colRouteID;
    public TableView tblCustomer;
    public TextField txtRouteID;
    public TextField txtAddress;
    public TextField txtContact;
    public TextField txtShopName;
    @FXML
    public TextField txtName;
    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtId;
    @FXML
    private ComboBox<String> cmbRouteId;


    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            Customer customer = CustomerRepo.searchById(id);

            if (customer != null) {
                txtId.setText(customer.getId());
                txtName.setText(customer.getName());
                txtShopName.setText(customer.getShopName());
                txtContact.setText(customer.getContact());
                txtAddress.setText(customer.getAddress());
                cmbRouteId.setValue(customer.getRouteId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }
    private List<Customer> customerList = new ArrayList<>();

    public void initialize() {
        this.customerList = getAllCustomers();
        setCellValueFactory();
        loadCustomerTable();
        getRouteId();
    }

    private void getRouteId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = RouteRepo.getId();
            for (String id : codeList) {
                obList.add(id);
            }

            cmbRouteId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colShopName.setCellValueFactory(new PropertyValueFactory<>("shopName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colRouteID.setCellValueFactory(new PropertyValueFactory<>("routeId"));
    }

    private void loadCustomerTable() {
        ObservableList<CustomerTm> tmList = FXCollections.observableArrayList();

        for (Customer customer : customerList) {
            CustomerTm customerTm = new CustomerTm(
                    customer.getId(),
                    customer.getName(),
                    customer.getShopName(),
                    customer.getContact(),
                    customer.getAddress(),
                    customer.getRouteId()
            );

            tmList.add(customerTm);
        }
        tblCustomer.setItems(tmList);
        CustomerTm selectedItem = (CustomerTm) tblCustomer.getSelectionModel().getSelectedItem();
       // System.out.println("selectedItem = " + selectedItem);
    }

    private List<Customer> getAllCustomers() {
        List<Customer> customerList = null;
        try {
            customerList = CustomerRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtShopName.setText("");
        txtContact.setText("");
        txtAddress.setText("");
        cmbRouteId.setValue("");
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String shopName = txtAddress.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String routeId = cmbRouteId.getValue();

        Customer customer = new Customer(id, name, shopName, contact,address,routeId);

        try {
            boolean isUpdated = CustomerRepo.update(customer);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String shopName = txtShopName.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String routeId = cmbRouteId.getValue();

        Customer customer = new Customer(id, name, shopName, contact,address,routeId);

        try {
            boolean isSaved = CustomerRepo.save(customer);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer saved!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();

        boolean isDeleted = CustomerRepo.delete(id);
        if (isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
        }
    }
}
