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
import lk.ijse.agency.model.tm.ExpensesTm;
import lk.ijse.agency.model.tm.EmployeeTm;
import lk.ijse.agency.repository.CustomerRepo;
import lk.ijse.agency.repository.EmployeeRepo;
import lk.ijse.agency.repository.ExpensesRepo;
import lk.ijse.agency.repository.VanRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpensesFormController {

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colVanId;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<EmployeeTm> tblExpences;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtVanId;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtDate;

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

    private List<Expenses> expensesList = new ArrayList<>();

    public void initialize() {
        this.expensesList = getAllExpenses();
        setCellValueFactory();
        loadExpensesTable();
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

    private void loadExpensesTable() {
        ObservableList<EmployeeTm> tmList = FXCollections.observableArrayList();

        for (Expenses expenses : expensesList) {
            ExpensesTm expensesTm = new ExpensesTm(
                    expenses.getCode(),
                    expenses.getVanId(),
                    expenses.getAmount(),
                    expenses.getDescription(),
                    expenses.getDate()
            );

            tmList.add(expensesTm);
        }
        tblExpences.setItems(tmList);
        ExpensesTm selectedItem = (ExpensesTm) tblExpences.getSelectionModel().getSelectedItem();
        //System.out.println("selectedItem = " + selectedItem);
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colVanId.setCellValueFactory(new PropertyValueFactory<>("vanId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private List<Expenses> getAllExpenses() {
        List<Expenses> expensesList = null;
        try {
            expensesList = ExpensesRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return expensesList;
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtCode.setText("");
        cmbVanId.setValue("");
        txtAmount.setText("");
        txtDescription.setText("");
        txtDate.setText("");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String vanId = cmbVanId.getValue();
        double amount = Double.parseDouble(txtAmount.getText());
        String description = txtDescription.getText();
        String date = txtDate.getText();

        Expenses expenses = new Expenses(code, vanId, amount, description,date);

        try {
            boolean isSaved = ExpensesRepo.save(expenses);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "expenses saved!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String vanId = cmbVanId.getValue();
        double amount = Double.parseDouble(txtAmount.getText());
        String description = txtDescription.getText();
        String date = txtDate.getText();

        Expenses expenses = new Expenses(code, vanId, amount, description,date);

        try {
            boolean isUpdated = ExpensesRepo.update(expenses);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "expenses updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String code = txtCode.getText();

        try {
            Expenses expenses = ExpensesRepo.searchById(code);

            if (expenses != null) {
                txtCode.setText(expenses.getCode());
                cmbVanId.setValue(expenses.getVanId());
                txtAmount.setText(String.valueOf(expenses.getAmount()));
                txtDescription.setText(expenses.getDescription());
                txtDate.setText(expenses.getDate());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String code = txtCode.getText();

        boolean isDeleted = ExpensesRepo.delete(code);
        if (isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "expenses deleted!").show();
        }
    }
}
