package lk.ijse.agency.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.agency.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class LoginFormController {
    public TextField txtUserName;
    @FXML
    private PasswordField txtPassword;
    public AnchorPane rootNode;

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        boolean isValidate = validatelogin();

        if (isValidate) {
            String userName = txtUserName.getText();
            String Password = txtPassword.getText();

            try {
                checkCredential(userName, Password);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "OOPS! something went wrong").show();
            }
        }
    }

    private boolean validatelogin() {
        int num=0;
        String userName = txtUserName.getText();
        boolean isUNValidate= Pattern.matches("[A-z]{3,}",userName);
        if (!isUNValidate){
            num=1;
            vibrateTextField(txtUserName);
        }

        String Password=txtPassword.getText();
        boolean isPWValidate= Pattern.matches("[A-z 0-9]{3,}",Password);
        if (!isPWValidate){
            num=1;
            vibrateTextField(txtPassword);
        }
        if(num==1){
            num=0;
            return false;
        }else {
            num=0;
            return true;

        }
    }

    private void checkCredential(String userName, String Password) throws SQLException, IOException {
        String sql = "SELECT userName, Password FROM admin WHERE userName = ?";

        Connection connection = DbConnection.getInstance().
                getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, userName);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String dbPw = resultSet.getString(2);

            if(dbPw.equals(Password )) {
                navigateToTheDashboard();
            } else {
                new Alert(Alert.AlertType.ERROR, "Password is incorrect!").show();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "user id not found!").show();
        }
    }

    private void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");

    }

    public void linkRegistrationOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/registration_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Registration Form");

        stage.show();
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
