package application.ui.addUsers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        //TODO:: add pop up menu to confirm exit.
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
        try{
            enc = encryptionService.getEncryptedPassword(pass, encryptionService.generateSalt());
        } catch (Exception e) {
            //TODO:: log4j and pop menu.
            return;
        }
        User user = new User();
        user.setClinic(Integer.valueOf(clinicNumber.getValue()));
        user.setEncryptedPassword(enc);
        //TODO:: make userID a auto increment value.
        user.setUserID();
        user.setUserName(userName.getText());
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
