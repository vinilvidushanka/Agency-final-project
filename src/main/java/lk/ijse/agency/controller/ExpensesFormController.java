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
import java.util.regex.Pattern;

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
        boolean isValidate = validateExpenses();

        if (isValidate) {
            String code = txtCode.getText();
            String vanId = cmbVanId.getValue();
            double amount = Double.parseDouble(txtAmount.getText());
            String description = txtDescription.getText();
            String date = txtDate.getText();

            Expenses expenses = new Expenses(code, vanId, amount, description, date);

            try {
                boolean isSaved = ExpensesRepo.save(expenses);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "expenses saved!").show();
                    initialize();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateExpenses() {
        int num=0;
        String code = txtCode.getText();
        boolean isCodeValidate= Pattern.matches("(EX)[0-9]{3,7}",code);
        if (!isCodeValidate){
            num=1;
            vibrateTextField(txtCode);
        }

        String amount=txtAmount.getText();
        boolean isAmountValidate= Pattern.matches("[0-9 .]{3,}",amount);
        if (!isAmountValidate){
            num=1;
            vibrateTextField(txtAmount);
        }

        String description=txtDescription.getText();
        boolean isDescriptonValidate= Pattern.matches("[A-z]{3,}",description);
        if (!isDescriptonValidate){
            num=1;
            vibrateTextField(txtDescription);
        }

        String date=txtDate.getText();
        boolean isDateValidate= Pattern.matches("[0-9 -]{10}",date);
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
                initialize();
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
