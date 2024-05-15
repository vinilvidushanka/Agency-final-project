package lk.ijse.agency.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.agency.model.*;
import lk.ijse.agency.model.tm.CartTm;
import lk.ijse.agency.model.tm.LoadingDetailTm;
import lk.ijse.agency.model.tm.LoadingTm;
import lk.ijse.agency.repository.ItemRepo;
import lk.ijse.agency.repository.PlaceLoadingRepo;
import lk.ijse.agency.repository.StockRepo;
import lk.ijse.agency.repository.VanRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DailyLoadingController {

    @FXML
    private ComboBox<String> cmbItemCode;


    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colVanId;

    @FXML
    private Label lblName;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<LoadingTm> tblLoadingReport;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtVanId;

    @FXML
    private ComboBox<String > cmbVanId;


    private ObservableList<LoadingTm> loadList = FXCollections.observableArrayList();

    public void initialize() {
        setCellValueFactory();
        getItemCode();
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


    private void getItemCode() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> codeList = ItemRepo.getCodes();
            for (String code : codeList) {
                obList.add(code);
            }

            cmbItemCode.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colVanId.setCellValueFactory(new PropertyValueFactory<>("vanId"));

    }

    @FXML
    void btnAddItemOnAction(ActionEvent event) {
        String itemCode = cmbItemCode.getValue();
        String itemName = lblName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        String vanId = cmbVanId.getValue();
        String repoId = txtId.getId();
        String date = txtDate.getId();

        for (int i = 0; i < tblLoadingReport.getItems().size(); i++) {
            if (itemCode.equals(colItemCode.getCellData(i))) {

                qty = loadList.get(i).getQty();
                loadList.get(i).setQty(qty);

                tblLoadingReport.refresh();

            }
        }

        LoadingTm loadingTm = new LoadingTm(itemCode, itemName, qty, vanId, repoId, date);

        loadList.add(loadingTm);

        tblLoadingReport.setItems(loadList);
    }

    @FXML
    void btnPlaceReportOnAction(ActionEvent event) throws SQLException {
        String itemCode = cmbItemCode.getValue();
        String itemName = lblName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        String vanId = cmbVanId.getValue();
        String repoId = txtId.getText();
        String date = txtDate.getText();

        var loading = new Loading(itemCode, itemName, qty, vanId, repoId, date);
        System.out.println(loading);

        List<LoadingDetail> ldList = new ArrayList<>();
        //List<Loading> itList = new ArrayList<>();

        for (int i = 0; i < tblLoadingReport.getItems().size(); i++) {
            LoadingTm tm = loadList.get(i);

            LoadingDetail ld = new LoadingDetail(
                    tm.getVanId(),
                    repoId,
                    tm.getItemCode(),
                    tm.getQty()
            );
            ldList.add(ld);

            PlaceLoading pl = new PlaceLoading(loading,ldList);
            System.out.println(pl.toString());
            boolean isPlaced = PlaceLoadingRepo.placeLoading(pl);
            if (isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "report placed!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "report not placed!").show();
            }
        }
    }

    @FXML
    void cmbItemCodeOnAction(ActionEvent event) {
        String itemCode = cmbItemCode.getValue();
        try {
            Stock stock = StockRepo.searchByItemCode(itemCode);
            if (stock != null) {
                lblName.setText(stock.getName());

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        txtQty.requestFocus();
    }

    @FXML
    void txtAddDateOnAction(ActionEvent event) {
        //btnAddItemOnAction(event);
    }

    @FXML
    void txtAddOrderIdOnAction(ActionEvent event) {
        //btnAddItemOnAction(event);
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        //btnAddItemOnAction(event);
    }

    @FXML
    void cmbVanIdOnAction(ActionEvent event) {
        //btnAddItemOnAction(event);
    }
}