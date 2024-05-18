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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import java.util.regex.Pattern;

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
        boolean isValidate = validateStock();

        if (isValidate) {
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
    }

    private boolean validateStock() {
        int num=0;
        String code = txtId.getText();
        boolean isCodeValidate= Pattern.matches("(it0)[0-9]{3,7}",code);
        if (!isCodeValidate){
            num=1;
            vibrateTextField(txtId);
        }

        String name=txtName.getText();
        boolean isNameValidate= Pattern.matches("[A-z 0-9]{3,}",name);
        if (!isNameValidate){
            num=1;
            vibrateTextField(txtName);
        }

        String uPrice=txtUnitPrice.getText();
        boolean isPriceValidate= Pattern.matches("[0-9 .]{2,}",uPrice);
        if (!isPriceValidate){
            num=1;
            vibrateTextField(txtUnitPrice);
        }

        String qty=txtQty.getText();
        boolean isQtyValidate= Pattern.matches("[0-9 ]{1,}",qty);
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
                initialize();
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
            initialize();
        }
    }

    public void btnPrintOnAction(ActionEvent event) {

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            JasperDesign load = JRXmlLoader.load(this.getClass().getResourceAsStream("/report/stockReportAgency.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,connection);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException e) {
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
