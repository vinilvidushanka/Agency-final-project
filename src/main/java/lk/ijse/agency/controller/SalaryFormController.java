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
import lk.ijse.agency.model.Employee;
import lk.ijse.agency.model.Expenses;
import lk.ijse.agency.model.Salary;
import lk.ijse.agency.model.Stock;
import lk.ijse.agency.model.tm.SalaryTm;
import lk.ijse.agency.model.tm.StockTm;
import lk.ijse.agency.repository.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalaryFormController {

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colEmpId;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colMonth;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<SalaryTm> tblSalary;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtEmpId;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtMonth;

    @FXML
    private TextField txtName;

    @FXML
    private ComboBox<String> cmbEmpId;

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }
    private List<Salary> salaryList = new ArrayList<>();

    public void initialize() {
        this.salaryList = getAllSalary();
        setCellValueFactory();
        loadSalaryTable();
        getEmpId();
    }

    private void getEmpId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = EmployeeRepo.getEmpId();
            for (String id : codeList) {
                obList.add(id);
            }

            cmbEmpId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSalaryTable() {
        ObservableList<SalaryTm> tmList = FXCollections.observableArrayList();

        for (Salary salary : salaryList) {
            SalaryTm salaryTm = new SalaryTm(
                    salary.getSalaryId(),
                    salary.getEmpId(),
                    salary.getName(),
                    salary.getMonth(),
                    salary.getAmount(),
                    salary.getDate()

            );

            tmList.add(salaryTm);
        }
        tblSalary.setItems(tmList);
        SalaryTm selectedItem = (SalaryTm) tblSalary.getSelectionModel().getSelectedItem();
        //System.out.println("selectedItem = " + selectedItem);
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("salaryId"));
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private List<Salary> getAllSalary() {
        List<Salary> salaryList = null;
        try {
            salaryList = SalaryRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salaryList;
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String salary_id = txtId.getText();
        String employee_id = cmbEmpId.getValue();
        String name = txtName.getText();
        String month =txtMonth.getText();
        double amount = Double.parseDouble(txtAmount.getText());
        String date = txtDate.getText();


        Salary salary = new Salary(salary_id, employee_id, name, month,amount,date);

        try {
            boolean isSaved = SalaryRepo.save(salary);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "salary saved!").show();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String salary_id = txtId.getText();
        String employee_id = cmbEmpId.getValue();
        String name = txtName.getText();
        String month = txtMonth.getText();
        double amount = Double.parseDouble(txtAmount.getText());
        String date = txtDate.getText();

        Salary salary = new Salary(salary_id, employee_id, name, month,amount,date);

        try {
            boolean isUpdated = SalaryRepo.update(salary);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "salary updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String code = txtId.getText();

        try {
            Salary salary = SalaryRepo.searchById(code);

            if (salary != null) {
                txtId.setText(salary.getSalaryId());
                cmbEmpId.setValue(salary.getEmpId());
                txtName.setText(salary.getName());
                txtMonth.setText(salary.getMonth());
                txtAmount.setText(String.valueOf(salary.getAmount()));
                txtDate.setText(salary.getDate());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {
        txtId.setText("");
        cmbEmpId.setValue("");
        txtName.setText("");
        txtMonth.setText("");
        txtAmount.setText("");
        txtDate.setText("");
    }

    @FXML
    void cmbEmpIdOnAction(ActionEvent event) {
        String id = cmbEmpId.getValue();
        try {
            Employee employee = EmployeeRepo.searchByEmpId(id);
            if (employee != null) {
                txtName.setText(employee.getName());

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
