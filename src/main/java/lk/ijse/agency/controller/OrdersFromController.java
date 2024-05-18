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
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.agency.model.Order;
import lk.ijse.agency.model.OrderDetail;
import lk.ijse.agency.model.PlaceOrder;
import lk.ijse.agency.model.Stock;
import lk.ijse.agency.model.tm.CartTm;
import lk.ijse.agency.model.tm.OrderTm;
import lk.ijse.agency.repository.ItemRepo;
import lk.ijse.agency.repository.PlaceOrderRepo;
import lk.ijse.agency.repository.StockRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class OrdersFromController {

    @FXML
    private ComboBox<String> cmbItemCode;

    @FXML
    private Label lblName;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<OrderTm> tblOrders;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtQty;


    private ObservableList<OrderTm> cartList = FXCollections.observableArrayList();


    public void initialize() {
        setCellValueFactory();
        getItemCode();
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
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

    }

    @FXML
    void btnAddItemOnAction(ActionEvent event) {
        boolean isValidate = validateOrders();

        if (isValidate) {
            String itemCode = cmbItemCode.getValue();
            String name = lblName.getText();
            int qty = Integer.parseInt(txtQty.getText());
            String orderId = txtId.getId();
            String date = txtDate.getText();

            for (int i = 0; i < tblOrders.getItems().size(); i++) {
                if (itemCode.equals(colItemCode.getCellData(i))) {
                    qty += cartList.get(i).getQty();

                    cartList.get(i).setQty(qty);

                    tblOrders.refresh();

                    return;
                }
            }

            CartTm cartTm = new CartTm(itemCode, name, qty, orderId, date);

            cartList.add(cartTm);

            tblOrders.setItems(cartList);
        }
    }

    private boolean validateOrders() {
        int num=0;
        String id = txtId.getText();
        boolean isIdValidate= Pattern.matches("(O0)[0-9]{3,7}",id);
        if (!isIdValidate){
            num=1;
            vibrateTextField(txtId);
        }

        String date=txtDate.getText();
        boolean isDateValidate= Pattern.matches("[0-9 -]{10}",date);
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




        if(num==1){
            num=0;
            return false;
        }else {
            num=0;
            return true;

        }
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        String orderId = txtId.getText();
        String itemCode = cmbItemCode.getValue();
        String itemName = lblName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        String date = txtDate.getText();

        var order = new Order(orderId, itemCode, itemName,qty,date);

        List<OrderDetail> odList = new ArrayList<>();
        List<OrderDetail> stlist = new ArrayList<>();
        for (int i = 0; i < tblOrders.getItems().size(); i++) {
            CartTm tm = (CartTm) cartList.get(i);

            OrderDetail od = new OrderDetail(
                    orderId,
                    //tm.getOrderId(),
                    tm.getItemCode(),
                    tm.getQty()
            );
            odList.add(od);
        }

        PlaceOrder po = new PlaceOrder(order, odList,stlist);
        System.out.println(order.toString());
        try {
            boolean isPlaced = PlaceOrderRepo.placeOrder(po);
            System.out.println(po.toString());
            if(isPlaced) {
                new Alert(Alert.AlertType.CONFIRMATION, "order placed!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "order not placed!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
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
