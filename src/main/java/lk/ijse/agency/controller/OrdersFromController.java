package lk.ijse.agency.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
        String itemCode = cmbItemCode.getValue();
        String name = lblName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        String orderId= txtId.getId();
        String date=txtDate.getText();

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

    public void btnPrintOnAction(ActionEvent event) {
        HashMap hashmap = new HashMap<>();
        hashmap.put("id", txtId.getText());
        hashmap.put("code", cmbItemCode.getValue());
        hashmap.put("name",lblName.getText());
        hashmap.put("qty", txtQty.getText());

        try {
            JasperDesign load = JRXmlLoader.load(this.getClass().getResourceAsStream("/report/agencyReport.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hashmap, new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
