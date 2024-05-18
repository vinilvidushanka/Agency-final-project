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
import lk.ijse.agency.model.Employee;
import lk.ijse.agency.model.Salary;
import lk.ijse.agency.model.tm.EmployeeTm;
import lk.ijse.agency.repository.CustomerRepo;
import lk.ijse.agency.repository.EmployeeRepo;
import lk.ijse.agency.repository.RouteRepo;
import lk.ijse.agency.repository.VanRepo;
import lk.ijse.agency.util.ValidateUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeFormController {

    @FXML
    private TableView<EmployeeTm> tblEmployee;
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNIC;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colVanId;
    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtVanId;
    @FXML
    private TextField txtNIC;
    @FXML
    private TextField txtContact;

    @FXML
    private ComboBox<String> cmbVanId;

    private LinkedHashMap<TextField, Pattern> map=new LinkedHashMap<>();

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }

    private List<Employee> employeeList = new ArrayList<>();

    public void initialize() {
        this.employeeList = getAllEmployees();
        setCellValueFactory();
        loadEmployeeTable();
        getVanId();


    }

    private void getVanId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = VanRepo.getVanId();
            for (String id : codeList) {
                obList.add(id);
            }

            cmbVanId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadEmployeeTable() {
        ObservableList<EmployeeTm> tmList = FXCollections.observableArrayList();

        for (Employee employee : employeeList) {
            EmployeeTm employeeTm = new EmployeeTm(
                    employee.getId(),
                    employee.getName(),
                    employee.getNic(),
                    employee.getAddress(),
                    employee.getContact(),
                    employee.getVanId()
            );

            tmList.add(employeeTm);
        }
        tblEmployee.setItems(tmList);
        EmployeeTm selectedItem = (EmployeeTm) tblEmployee.getSelectionModel().getSelectedItem();
        //System.out.println("selectedItem = " + selectedItem);
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colVanId.setCellValueFactory(new PropertyValueFactory<>("vanId"));
    }

    private List<Employee> getAllEmployees() {
        List<Employee> employeeList = null;
        try {
            employeeList = EmployeeRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtNIC.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        cmbVanId.setValue("");
    }

    @FXML
    void btnSaveOnAction( ) {
        boolean isValidate = validateEmployee();
        if (isValidate) {
            String id = txtId.getText();
            String name = txtName.getText();
            String nic = txtNIC.getText();
            String address = txtAddress.getText();
            int contact = Integer.parseInt(txtContact.getText());
            String vanId = cmbVanId.getValue();

            EmployeeTm employee = new EmployeeTm(id, name, nic, address, contact, vanId);

            try {
                boolean isSaved = EmployeeRepo.save(employee);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "employee saved!").show();
                    initialize();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateEmployee() {
        int num=0;
        String id = txtId.getText();
        boolean isIDValidate= Pattern.matches("(E0)[A-z 0-9]{5}",id);
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

        String NIC=txtNIC.getText();
        boolean isNICValidate= Pattern.matches("[0-9 v]{12}",NIC);
        if (!isNICValidate){
            num=1;
            vibrateTextField(txtNIC);
        }


        String address=txtAddress.getText();
        boolean isAddressValidate= Pattern.matches("[A-z]{3,}",address);
        if (!isAddressValidate){
            num=1;
            vibrateTextField(txtAddress);
        }

        String contact=txtContact.getText();
        boolean isContactValidate= Pattern.matches("[0-9]{10}",contact);
        if (!isContactValidate){
            num=1;
            vibrateTextField(txtContact);
        }

        if(num==1){
            num=0;
            return false;
        }else {
            num=0;
            return true;

        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String nic = txtNIC.getText();
        String address = txtAddress.getText();
        int contact = Integer.parseInt(txtContact.getText());
        String vanId = cmbVanId.getValue();

        Employee employee = new Employee(id, name, nic, address,contact,vanId);

        try {
            boolean isUpdated = EmployeeRepo.update(employee);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
            String id = txtId.getText();

            try {
                Employee employee = EmployeeRepo.searchById(id);

                if (employee != null) {
                    txtId.setText(employee.getId());
                    txtName.setText(employee.getName());
                    txtNIC.setText(employee.getNic());
                    txtAddress.setText(employee.getAddress());
                    txtContact.setText(String.valueOf(employee.getContact()));
                    cmbVanId.setValue(employee.getVanId());
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();

        boolean isDeleted = EmployeeRepo.delete(id);
        if (isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "employee deleted!").show();
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
