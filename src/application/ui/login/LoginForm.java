package application.ui.login;

import application.ui.handler.WindowHandlers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import main.java.beans.UserSignedInData;
import main.java.helper.AlertHelper;
import main.java.model.UserDAO;
import javafx.scene.input.MouseEvent;

public class LoginForm {
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    protected void handleSubmitButtonAction(MouseEvent event){
        Window owner = submitButton.getScene().getWindow();
        if(nameField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "خطأ فى تسجيل الدخول",
                    "من فضلك ادخل اسم المستخدم");
            return;
        }

        if(passwordField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "خطأ فى تسجيل الدخول",
                    "من فضلك ادخل كلمة المرور");
            return;
        }

        boolean loginSuccess = UserDAO.login(nameField.getText(), passwordField.getText());
        if(loginSuccess){
                try {
                    WindowHandlers windowHandlers = WindowHandlers.getInstance();
                    windowHandlers.loadWindow("/application/ui/mainPage/mainPage.fxml",
                            "Main Page", true,true, false, event);
                }
                catch (Exception e) {
                    //TODO:: put log4j jar for error logging
                    e.printStackTrace();
                }
        }
        else{
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "خطأ فى تسجيل الدخول",
                    "تأكد من ادخال اسم المستخدم و كلمة المرور صحيحة");
        }
    }
}