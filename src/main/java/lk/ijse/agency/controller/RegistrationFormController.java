package lk.ijse.agency.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.agency.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegistrationFormController {

    @FXML
    public AnchorPane rootNode;
    @FXML
    public TextField txtUsername;

    @FXML
    public TextField txtPassword;

    @FXML
    private TextField txtUserId;

    @FXML
    void btnRegisterOnAction(ActionEvent event) {

        boolean isValidate = validateRegister();
        if (isValidate) {
            String Id = txtUserId.getText();
            String userName = txtUsername.getText();
            String Password = txtPassword.getText();

            saveUser(Id, userName, Password);
        }
    }

    private boolean validateRegister() {
        int num=0;
        String id = txtUserId.getText();
        boolean isIDValidate= Pattern.matches("(DA)[0-9]{3,7}",id);
        if (!isIDValidate){
            num=1;
            vibrateTextField(txtUserId);
        }

        String userName = txtUsername.getText();
        boolean isUNValidate= Pattern.matches("[A-z]{3,}",userName);
        if (!isUNValidate){
            num=1;
            vibrateTextField(txtUsername);
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


    private void saveUser(String Id, String userName, String Password) {
        try {
            String sql = "INSERT INTO admin VALUES(?, ?, ?)";

            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setObject(1, Id);
            pstm.setObject(2, userName);
            pstm.setObject(3, Password);

            if(pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "user saved!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something happend!").show();
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Login Form");
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
