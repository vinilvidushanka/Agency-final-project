package lk.ijse.agency.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.agency.model.Customer;
import lk.ijse.agency.model.tm.CustomerTm;
import lk.ijse.agency.repository.CustomerRepo;
import lk.ijse.agency.repository.ItemRepo;
import lk.ijse.agency.repository.RouteRepo;
import lk.ijse.agency.util.ValidateUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

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



    final LinkedHashMap<TextField, Pattern> map=new LinkedHashMap<>();


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

    public void btnSaveOnAction() {

        boolean isValidate = validateCustomer();

        if (isValidate){
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
                    initialize();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateCustomer() {
        int num=0;
        String id = txtId.getText();
        boolean isIDValidate= Pattern.matches("(C0)[A-z 0-9]{5}",id);
        if (!isIDValidate){
            num=1;
            vibrateTextField(txtId);
        }

        String name=txtName.getText();
        boolean isNameValidate= Pattern.matches("[A-z]{3,}",name);
        if (!isNameValidate){
            num=1;
            vibrateTextField(txtName);
        }

        String shopName=txtShopName.getText();
        boolean isShopNameValidate= Pattern.matches("[A-z]{3,}",shopName);
        if (!isShopNameValidate){
            num=1;
            vibrateTextField(txtShopName);
        }

        String contact=txtContact.getText();
        boolean isContactValidate= Pattern.matches("[0-9]{10}",contact);
        if (!isContactValidate){
            num=1;
            vibrateTextField(txtContact);
        }

        String address=txtAddress.getText();
        boolean isAddressValidate= Pattern.matches("[A-z]{3,}",address);
        if (!isAddressValidate){
            num=1;
            vibrateTextField(txtAddress);
        }

        if(num==1){
            num=0;
            return false;
        }else {
            num=0;
            return true;

        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();

        boolean isDeleted = CustomerRepo.delete(id);
        if (isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
        }
    }

    private void vibrateTextField(TextField textField) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(textField.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(50), new KeyValue(textField.translateXProperty(), -6)),
                new KeyFrame(Duration.millis(100), new KeyValue(textField.translateXProperty(), 6)),
                new KeyFrame(Duration.millis(150), new KeyValue(textField.translateXProperty(), -6)),
                new KeyFrame(Duration.millis(200), new KeyValue(textField.translateXProperty(), 6)),
                new KeyFrame(Duration.millis(250), new KeyValue(textField.translateXProperty(), -6)),
                new KeyFrame(Duration.millis(300), new KeyValue(textField.translateXProperty(), 6)),
                new KeyFrame(Duration.millis(350), new KeyValue(textField.translateXProperty(), -6)),
                new KeyFrame(Duration.millis(400), new KeyValue(textField.translateXProperty(), 0))

        );

        textField.setStyle("-fx-border-color: red;");
        timeline.play();

        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.seconds(3), new KeyValue(textField.styleProperty(), "-fx-border-color: #bde0fe;"))
        );

        timeline1.play();
    }
}
