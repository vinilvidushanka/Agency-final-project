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
import java.util.regex.Pattern;

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
        boolean isValidate = validateLoading();

        if (isValidate) {
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
    }

    private boolean validateLoading() {
        int num=0;
        String id = txtId.getText();
        boolean isIdValidate= Pattern.matches("(R0)[0-9]{6}",id);
        if (!isIdValidate){
            num=1;
            vibrateTextField(txtId);
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




        if(num==1){
            num=0;
            return false;
        }else {
            num=0;
            return true;

        }
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