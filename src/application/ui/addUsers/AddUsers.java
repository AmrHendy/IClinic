package application.ui.addUsers;

import application.ui.handler.MessagesController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import main.java.beans.User;
import main.java.model.UserDAO;
import main.java.services.PasswordEncryptionService;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUsers implements Initializable {

    //mandatory
    @FXML
    private JFXTextField userName;

    //mandatory and equal confirm password.
    @FXML
    private JFXTextField password;

    @FXML
    private JFXTextField confirmPassword;

    //mandatory
    @FXML
    private JFXComboBox<String> clinicNumber;

    @FXML
    void cancel(MouseEvent event) {
        String msg = "جميع البيانات لم يتم حفظها هل انت متاكد انك تريد الخروج؟";
        Alert alert = MessagesController.getAlert(msg, Alert.AlertType.CONFIRMATION);
        alert.setTitle("تحذير");
        if(alert.getResult() == ButtonType.YES) {
            //((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @FXML
    void save(MouseEvent event) {
        String pass = password.getText();
        String conPass = confirmPassword.getText();
        if(!pass.equals(conPass)){
            //TODO:: add pop up menu here.
            return;
        }
        PasswordEncryptionService encryptionService = new PasswordEncryptionService();
        byte[] enc = {};
        byte[] salt = {};
        try{
            salt = encryptionService.generateSalt();
            enc = encryptionService.getEncryptedPassword(pass, salt);
        } catch (Exception e) {
            //TODO:: log4j and pop menu.
            return;
        }
        User user = new User();
        user.setClinic(Integer.valueOf(clinicNumber.getValue()));
        user.setEncryptedPassword(enc);
        user.setSalt(salt);
        user.setUserName(userName.getText());
        //TODO:: add check if not inserted.
        UserDAO.register(user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO:: add Values from database.
        ObservableList<String> options = FXCollections.observableArrayList(
                        "1",
                        "2"
                );
        clinicNumber.getItems().addAll(options);
    }
}
