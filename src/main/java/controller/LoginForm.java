package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import main.java.helper.AlertHelper;
import main.java.model.UserDAO;
import org.w3c.dom.UserDataHandler;

public class LoginForm {
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Window owner = submitButton.getScene().getWindow();
        if(nameField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your name");
            return;
        }

        if(passwordField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        boolean loginSuccess = UserDAO.login(nameField.getText(), passwordField.getText());
        if(loginSuccess){

        }
        else{
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Try Again Login",
                    "Please Enter Correct UserName and Password");
        }
    }
}