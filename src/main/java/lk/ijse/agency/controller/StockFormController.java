package lk.ijse.agency.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Customer;
import lk.ijse.agency.model.Stock;
import lk.ijse.agency.model.tm.StockTm;
import lk.ijse.agency.repository.CustomerRepo;
import lk.ijse.agency.repository.ExpensesRepo;
import lk.ijse.agency.repository.StockRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockFormController {

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<StockTm> tblStock;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtUnitPrice;



    private List<Stock> stockList = new ArrayList<>();

    public void initialize() {
        this.stockList = getAllStock();
        setCellValueFactory();
        loadStockTable();
    }

    private void loadStockTable() {
        ObservableList<StockTm> tmList = FXCollections.observableArrayList();

        for (Stock stock : stockList) {
            StockTm stockTm = new StockTm(
                    stock.getItemCode(),
                    stock.getName(),
                    stock.getQty(),
                    stock.getUnitPrice()

                    );

            tmList.add(stockTm);
        }
        tblStock.setItems(tmList);
        // StockTm selectedItem = tblStock.getSelectionModel().getSelectedItem();
        //System.out.println("selectedItem = " + selectedItem);
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

    }

    private List<Stock> getAllStock() {
        List<Stock> stockList /*= null*/;
        try {
            stockList = StockRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stockList;
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtUnitPrice.setText("");
        txtQty.setText("");

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String item_code = txtId.getText();
        String name = txtName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double unit_price = Double.parseDouble(txtUnitPrice.getText());


        Stock stock = new Stock(item_code, name, qty, unit_price);

        try {
            boolean isSaved = StockRepo.save(stock);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "stock saved!").show();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String item_code = txtId.getText();
        String name = txtName.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double unit_price = Double.parseDouble(txtUnitPrice.getText());


        Stock stock = new Stock(item_code, name, qty, unit_price);

        try {
            boolean isUpdated = StockRepo.update(stock);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "stock updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String item_code = txtId.getText();

        try {
            Stock stock = StockRepo.searchById(item_code);

            if (stock != null) {
                txtId.setText(stock.getItemCode());
                txtName.setText(stock.getName());
                txtUnitPrice.setText(String.valueOf(stock.getUnitPrice()));
                txtQty.setText(String.valueOf(stock.getQty()));

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        String itemCode = txtId.getText();

        boolean isDeleted = StockRepo.delete(itemCode);
        if (isDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "item deleted!").show();
        }
    }

    public void btnPrintOnAction(ActionEvent event) {

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            JasperDesign load = JRXmlLoader.load(this.getClass().getResourceAsStream("/report/agencyStock.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,connection);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
