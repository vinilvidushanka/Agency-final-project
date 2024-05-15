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
import lk.ijse.agency.model.Employee;
import lk.ijse.agency.model.Salary;
import lk.ijse.agency.model.tm.EmployeeTm;
import lk.ijse.agency.repository.CustomerRepo;
import lk.ijse.agency.repository.EmployeeRepo;
import lk.ijse.agency.repository.RouteRepo;
import lk.ijse.agency.repository.VanRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    void btnSaveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String nic = txtNIC.getText();
        String address = txtAddress.getText();
        int contact = Integer.parseInt(txtContact.getText());
        String vanId = cmbVanId.getValue();

        EmployeeTm employee = new EmployeeTm(id, name, nic, address,contact,vanId);

        try {
            boolean isSaved = EmployeeRepo.save(employee);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "employee saved!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
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
}
