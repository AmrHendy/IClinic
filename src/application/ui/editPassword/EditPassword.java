package application.ui.editPassword;


import application.ui.displayUsers.DisplayUser;
import application.ui.handler.MessagesController;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import main.java.beans.UserSignedInData;
import main.java.services.PasswordEncryptionService;

import java.beans.PropertyChangeEvent;

public class EditPassword {
    @FXML
    private JFXPasswordField currentPassword;

    @FXML
    private JFXPasswordField newPassword;

    @FXML
    private JFXPasswordField confirmNewPassword;

    private DisplayUser displayUser;

    @FXML
    void cancel(ActionEvent event) {
        if(currentPassword.getText().equals("") || newPassword.getText().equals("") || confirmNewPassword.getText().equals("")){
            String msg = "جميع البيانات لم يتم حفظها هل انت متاكد انك تريد الخروج؟";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "" + msg + "", ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK){
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            return;
        }
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void save(ActionEvent event) {
        if(currentPassword.getText().equals("") || newPassword.getText().equals("") || confirmNewPassword.getText().equals("")
                || !newPassword.getText().equals(confirmNewPassword.getText())){
            MessagesController.getAlert("فشل تحديث كلمة المرور. تاكد من ادخال القيم الصحيحة.", Alert.AlertType.ERROR);
            return;
        }
        PasswordEncryptionService encryptionService = new PasswordEncryptionService();
        byte[] enc = {};
        byte[] salt = {};
        try{
            salt = UserSignedInData.user.getSalt();
            enc = encryptionService.getEncryptedPassword(currentPassword.getText(), salt);
            if(!encryptionService.authenticate(currentPassword.getText(), enc, salt)){
                String msg = "لا يمكن اتمام التسجيل اعد المحاولة.";
                MessagesController.getAlert(msg, Alert.AlertType.ERROR);
                return;
            }
        } catch (Exception e) {
            String msg = "لا يمكن اتمام التسجيل اعد المحاولة.";
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
            return;
        }
        displayUser.propertyChange(new PropertyChangeEvent(this, "password", null, newPassword.getText()));
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void addObserver(DisplayUser displayUser){
        this.displayUser = displayUser;
    }
}
