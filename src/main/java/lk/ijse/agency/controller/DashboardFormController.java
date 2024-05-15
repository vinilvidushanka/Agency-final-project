package lk.ijse.agency.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.agency.db.DbConnection;
import lk.ijse.agency.model.Route;
import lk.ijse.agency.model.tm.RouteTm;
import lk.ijse.agency.repository.RouteRepo;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DashboardFormController {

    @FXML
    private TableColumn<?, ?> colDay;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colRoute;

    @FXML
    private TableColumn<?, ?> colVanId;
    @FXML
    public TableView<RouteTm> tblVan;

    @FXML
    private Label lblCusCount;

    @FXML
    private Label lblName;

    @FXML
    private AnchorPane root;
    public AnchorPane rootNode;
    @FXML
    private AnchorPane Load;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblEmpCount;

    private int customerCount;

    private double creditAmount;

    private int employeeCount;

    private String name;

    public DashboardFormController() {
    }

    private void setCustomerCount(int customerCount) {
        lblCusCount.setText(String.valueOf(customerCount));
    }

    private void setEmployeeCount(int employeeCount) {
        lblEmpCount.setText(String.valueOf(employeeCount));
    }

    private void setCreditAmount(double creditAmount) {
        lblAmount.setText(String.valueOf(creditAmount));
    }

    private int getCustomerCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS customer_count FROM customer";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        int customerCount = 0;
        if(resultSet.next()) {
            customerCount = resultSet.getInt("customer_count");
        }
        return customerCount;
    }
    private List<Route> routeList = new ArrayList<>();

    private int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS Employee_count FROM employee";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        int employeeCount = 0;
        if(resultSet.next()) {
            employeeCount = resultSet.getInt("employee_count");
        }
        return employeeCount;
    }

    private double getCreditAmount() throws SQLException {
        String sql = "SELECT SUM(amount) AS credit_amount FROM credit_bill";


        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        double creditAmount = 0.00;
        if(resultSet.next()) {
            creditAmount =  resultSet.getDouble("credit_amount");
        }
        return creditAmount;
    }


    public void initialize() throws SQLException {
        this.routeList = getAllRoutes();
        setCellValueFactory();
        loadRouteTable();
        initClock();

        try {
            customerCount = getCustomerCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setCustomerCount(customerCount);

        try {
            employeeCount = getEmployeeCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setEmployeeCount(employeeCount);

        try {
            creditAmount = getCreditAmount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setCreditAmount(creditAmount);




    }


    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd        HH:mm:ss");
            lblTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }



    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("routeId"));
        colRoute.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        colVanId.setCellValueFactory(new PropertyValueFactory<>("vanId"));

    }

    private void loadRouteTable() {
        ObservableList<RouteTm> tmList = FXCollections.observableArrayList();

        for (Route route : routeList) {
            RouteTm routeTm = new RouteTm(
                    route.getRouteId(),
                    route.getName(),
                    route.getDay(),
                    route.getVanId()

            );

            tmList.add(routeTm);
        }
        tblVan.setItems(tmList);
        RouteTm selectedItem = tblVan.getSelectionModel().getSelectedItem();
        //System.out.println("selectedItem = " + selectedItem);
    }

    private List<Route> getAllRoutes() {
        List<Route> routeList = null;
        try {
            routeList = RouteRepo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return routeList;
    }



    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/customer_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnOrdersOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/orders_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/employee_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnSalesReportOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/salesreport_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }


    public void btnDaillyTransactionOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/dailyTransaction_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnCreditBillsOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/creditbill_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnStocksOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/stock_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }


    public void btnDailyLoadingOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/dailyLoading_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }


    public void btnExpensesOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/expenses_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnExitOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
        stage.setTitle("Login Form");
    }

    public void btnSalaryOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/salary_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnVanOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/van_form.fxml"));

        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
        stage.setTitle("Van Form");
    }

    public void btnRouteOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/route_form.fxml"));

        Stage stage = (Stage) rootNode.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
        stage.setTitle("Route Form");
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }
}
