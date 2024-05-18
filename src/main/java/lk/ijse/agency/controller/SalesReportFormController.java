package lk.ijse.agency.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.agency.model.*;
import lk.ijse.agency.model.tm.SaleCartTm;
import lk.ijse.agency.repository.ItemRepo;
import lk.ijse.agency.repository.PlaceSalesRepo;
import lk.ijse.agency.repository.StockRepo;
import lk.ijse.agency.repository.VanRepo;
import lk.ijse.agency.util.ValidateUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

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

    private LinkedHashMap<TextField, Pattern> map=new LinkedHashMap<>();
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
    void btnAddItemOnAction( ) {
        boolean isValidate = validateSale();

        if (isValidate) {
            String billCode = txtBillCode.getText();
            String itemCode = cmbItemCode.getValue();
            String itemName = lblName.getText();
            int qty = Integer.parseInt(txtQty.getText());
            double unitPrice = Double.parseDouble(lblPrice.getText());
            double amount = qty * unitPrice;
            String date = txtDate.getText();
            String vanId = cmbVanId.getValue();
            int freeQty = Integer.parseInt(txtFQty.getText());

            for (int i = 0; i < tblSale.getItems().size(); i++) {
                if (itemCode.equals(colAmount.getCellData(i))) {

                    qty = cartList.get(i).getQty();
                    cartList.get(i).setQty(qty);

                    tblSale.refresh();
                }
            }

            SaleCartTm saleCartTm = new SaleCartTm(billCode, itemCode, itemName, qty, unitPrice, amount, date, vanId, freeQty);

            cartList.add(saleCartTm);

            tblSale.setItems(cartList);
            calculateAmount();
        }
    }

    private boolean validateSale() {
        int num=0;
        String id = txtBillCode.getText();
        boolean isIdValidate= Pattern.matches("(R0)[0-9]{6}",id);
        if (!isIdValidate){
            num=1;
            vibrateTextField(txtBillCode);
        }

        String date=txtDate.getText();
        boolean isDateValidate= Pattern.matches("[0-9 -]{12}",date);
        if (!isDateValidate){
            num=1;
            vibrateTextField(txtDate);
        }

        String qty=txtQty.getText();
        boolean isQtyValidate= Pattern.matches("[0-9]{1,}",qty);
        if (!isQtyValidate){
            num=1;
            vibrateTextField(txtQty);
        }

        String Fqty=txtFQty.getText();
        boolean isFQtyValidate= Pattern.matches("[0-9]{1,}",Fqty);
        if (!isFQtyValidate){
            num=1;
            vibrateTextField(txtFQty);
        }


        if(num==1){
            num=0;
            return false;
        }else {
            num=0;
            return true;

        }
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
        btnAddItemOnAction();
    }

    @FXML
    void txtAddOrderIdOnAction(ActionEvent event) {
        btnAddItemOnAction();
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddItemOnAction();
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
        btnAddItemOnAction();
    }

    public void txtAddVanIdOnAction(ActionEvent event) {
        btnAddItemOnAction();
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
