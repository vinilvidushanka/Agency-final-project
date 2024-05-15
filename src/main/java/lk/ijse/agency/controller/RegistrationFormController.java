package lk.ijse.agency.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.agency.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        String Id = txtUserId.getText();
        String userName = txtUsername.getText();
        String Password = txtPassword.getText();

        saveUser(Id, userName, Password);
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
}
