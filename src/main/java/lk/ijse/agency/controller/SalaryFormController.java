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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import java.util.regex.Pattern;

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
        boolean isValidate = validateSalary();

        if (isValidate) {
            String salary_id = txtId.getText();
            String employee_id = cmbEmpId.getValue();
            String name = txtName.getText();
            String month = txtMonth.getText();
            double amount = Double.parseDouble(txtAmount.getText());
            String date = txtDate.getText();


            Salary salary = new Salary(salary_id, employee_id, name, month, amount, date);

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
    }

    private boolean validateSalary() {
        int num=0;
        String id = txtId.getText();
        boolean isIDValidate= Pattern.matches("(S0)[0-9]{5}",id);
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

        String month=txtMonth.getText();
        boolean isMonthValidate= Pattern.matches("[A-z]{3,}",month);
        if (!isMonthValidate){
            num=1;
            vibrateTextField(txtMonth);
        }

        String amount=txtAmount.getText();
        boolean isAmountValidate= Pattern.matches("[0-9 .]{3,}",amount);
        if (!isAmountValidate){
            num=1;
            vibrateTextField(txtAmount);
        }

        String date=txtDate.getText();
        boolean isDateValidate= Pattern.matches("[0-9 -]{12}",date);
        if (!isDateValidate){
            num=1;
            vibrateTextField(txtDate);
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
