package lk.ijse.agency.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.agency.model.*;
import lk.ijse.agency.model.tm.SaleCartTm;
import lk.ijse.agency.repository.ItemRepo;
import lk.ijse.agency.repository.PlaceSalesRepo;
import lk.ijse.agency.repository.StockRepo;
import lk.ijse.agency.repository.VanRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesReportFormController {

    @FXML
    private ComboBox<String> cmbItemCode;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblName;

    @FXML
    private Label lblPrice;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<SaleCartTm> tblSale;

    @FXML
    private TextField txtBillCode;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtFQty;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtVanId;

    @FXML
    private ComboBox<String> cmbVanId;

    private double amount = 0;

    private ObservableList<SaleCartTm> cartList = FXCollections.observableArrayList();

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
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    @FXML
    void btnAddItemOnAction(ActionEvent event) {
        String billCode=txtBillCode.getText();
        String itemCode = cmbItemCode.getValue();
        String itemName = lblName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(lblPrice.getText());
        double amount = qty * unitPrice;
        String date=txtDate.getText();
        String vanId=cmbVanId.getValue();
        int freeQty = Integer.parseInt(txtFQty.getText());

        for (int i = 0; i < tblSale.getItems().size(); i++) {
            if (itemCode.equals(colAmount.getCellData(i))) {

                qty = cartList.get(i).getQty();
                cartList.get(i).setQty(qty);

                tblSale.refresh();
            }
        }

        SaleCartTm saleCartTm = new SaleCartTm(billCode,itemCode, itemName, qty,unitPrice,amount,date,vanId,freeQty);

        cartList.add(saleCartTm);

        tblSale.setItems(cartList);
        calculateAmount();
    }

    private void calculateAmount() {
        amount = 0;
        for (int i = 0; i < tblSale.getItems().size(); i++) {
            amount += (double) colAmount.getCellData(i);
        }
        lblAmount.setText(String.valueOf(amount));
    }


    @FXML
    void cmbItemCodeOnAction(ActionEvent event) {
        String itemCode = cmbItemCode.getValue();
        try {
            Stock stock = StockRepo.searchByItemCode(itemCode);
            if (stock != null) {
                lblName.setText(stock.getName());
                lblPrice.setText(String.valueOf(stock.getUnitPrice()));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        txtQty.requestFocus();



    }

    @FXML
    void txtAddDateOnAction(ActionEvent event) {
        btnAddItemOnAction(event);
    }

    @FXML
    void txtAddOrderIdOnAction(ActionEvent event) {
        btnAddItemOnAction(event);
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddItemOnAction(event);
    }

    public void btnPlaceReportOnAction(ActionEvent event) {
        String billCode = txtBillCode.getText();
        String itemCode = cmbItemCode.getValue();
        String itemName = lblName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        int freeQty = Integer.parseInt(txtFQty.getText());
        String date = txtDate.getText();
        String vanId = cmbVanId.getValue();
        double unitPrice = Double.parseDouble(lblPrice.getText());
        double amount = Double.parseDouble(lblAmount.getText());




        var salesReport = new SalesReport(billCode, itemCode, itemName,qty,freeQty,date,vanId,unitPrice,amount);
        List<SalesReport> slList = new ArrayList<>();

        for (int i = 0; i < tblSale.getItems().size(); i++) {
            SaleCartTm tm = cartList.get(i);


        }

        PlaceSalesReport ps = new PlaceSalesReport(salesReport,slList);
        try {
            boolean isPlaced = PlaceSalesRepo.placeReport(ps);
            if(isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "order placed!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "order not placed!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void txtFQtyOnAction(ActionEvent event) {
        btnAddItemOnAction(event);
    }

    public void txtAddVanIdOnAction(ActionEvent event) {
        btnAddItemOnAction(event);
    }
}
