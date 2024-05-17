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
import lk.ijse.agency.model.DailyTransaction;
import lk.ijse.agency.model.tm.DailyTransactionTm;
import lk.ijse.agency.repository.CustomerRepo;
import lk.ijse.agency.repository.DailyTransactionRepo;
import lk.ijse.agency.repository.VanRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DailyTransactionFormController {

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colVanId;

    @FXML
    private TableView<DailyTransactionTm> tblDailyTransaction;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtVanId;
    @FXML
    private AnchorPane root;

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

    private List<DailyTransaction> dailyTransactionList = new ArrayList<>();

    public void initialize() throws SQLException {
        this.dailyTransactionList = getAllDailyTransaction();
        setCellValueFactory();
        loadDailyTransactionTable();
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

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colVanId.setCellValueFactory(new PropertyValueFactory<>("vanId"));

    }

    private void loadDailyTransactionTable() {
        ObservableList<DailyTransactionTm> tmList = FXCollections.observableArrayList();

        for (DailyTransaction dailyTransaction : dailyTransactionList) {
            DailyTransactionTm dailyTransactionTm = new DailyTransactionTm(
                    dailyTransaction.getBillId(),
                    dailyTransaction.getAmount(),
                    dailyTransaction.getDate(),
                    dailyTransaction.getVanId()

            );

            tmList.add(dailyTransactionTm);
        }
        tblDailyTransaction.setItems(tmList);
        DailyTransactionTm selectedItem = (DailyTransactionTm) tblDailyTransaction.getSelectionModel().getSelectedItem();
        System.out.println("selectedItem = " + selectedItem);
    }

    private List<DailyTransaction> getAllDailyTransaction() throws SQLException {
        List<DailyTransaction> dailyTransactionList = null;
        dailyTransactionList = DailyTransactionRepo.getAll();
        return dailyTransactionList;
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtId.setText("");
        txtAmount.setText("");
        txtDate.setText("");
        cmbVanId.setValue("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String billId = txtId.getText();

        boolean isDeleted = DailyTransactionRepo.delete(billId);
        if (isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "transaction deleted!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String billId = txtId.getText();
        double amount = Double.parseDouble(txtAmount.getText());
        String date = txtDate.getText();
        String vanId = cmbVanId.getValue();


        DailyTransaction dailyTransaction = new DailyTransaction(billId, amount, date, vanId);

        try {
            boolean isSaved = DailyTransactionRepo.save(dailyTransaction);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "daily transaction saved!").show();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String billId = txtId.getText();
        double amount = Double.parseDouble(txtAmount.getText());
        String date = txtDate.getText();
        String vanId = cmbVanId.getValue();


        DailyTransaction dailyTransaction = new DailyTransaction(billId, amount, date, vanId);

        try {
            boolean isUpdated = DailyTransactionRepo.update(dailyTransaction);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "daily transaction updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String billId = txtId.getText();

        DailyTransaction dailyTransaction = DailyTransactionRepo.searchById(billId);

        if (dailyTransaction != null) {
            txtId.setText(dailyTransaction.getBillId());
            txtAmount.setText(String.valueOf(dailyTransaction.getAmount()));
            txtDate.setText(dailyTransaction.getDate());
            cmbVanId.setValue(dailyTransaction.getVanId());

        }
    }

}
