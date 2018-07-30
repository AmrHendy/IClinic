package application.ui.addUsers;

import application.ui.handler.MessagesController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddUsers implements Initializable {

    //mandatory
    @FXML
    private JFXTextField userName;

    //mandatory and equal confirm password.
    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXPasswordField confirmPassword;

    //mandatory
    @FXML
    private JFXTextField clinicNumber;

    @FXML
    void cancel(MouseEvent event) {
        return;
        /*
        if(userName.getText() != null || password.getText() != null || confirmPassword.getText() != null || clinicNumber.getValue() != null){
            String msg = "جميع البيانات لم يتم حفظها هل انت متاكد انك تريد الخروج؟";
            Alert alert = MessagesController.getAlert(msg, Alert.AlertType.CONFIRMATION);
        }
         */
    }

    @FXML
    void save(MouseEvent event) {
        String pass = password.getText();
        boolean can = true;
        String msg = "";
        ArrayList<String> msgs = getMessages();
        can = msgs.size() > 0 ? false : true;
        PasswordEncryptionService encryptionService = new PasswordEncryptionService();
        byte[] enc = {};
        byte[] salt = {};
        try{
            salt = encryptionService.generateSalt();
            enc = encryptionService.getEncryptedPassword(pass, salt);
        } catch (Exception e) {
            msg = "لا يمكن اتمام التسجيل اعد المحاولة.";
            can = false;
            msgs.add(msg);
        }
        //TODO:: add check if not inserted.
        //check already added in line 75.
        if(!can){
            Alert alert = MessagesController.getAlert(msgs, Alert.AlertType.ERROR);
        }else{
            User user = new User();
            user.setClinic(Integer.valueOf(clinicNumber.getText()));
            user.setEncryptedPassword(enc);
            user.setSalt(salt);
            user.setUserName(userName.getText());
            if(!UserDAO.register(user)){
                msg = "لا يمكن اتمام اضافة مستخدم اعد المحاولة.";
                Alert alert = MessagesController.getAlert(msg, Alert.AlertType.ERROR);
            }
        }
    }

    private ArrayList<String> getMessages(){
        String pass = password.getText();
        String conPass = confirmPassword.getText();
        ArrayList<String> msgs = new ArrayList<>();
        String msg = null;
        if(userName.getText() == null){
            msg = "لا توجد قيمة لاسم المستخدم";
            msgs.add(msg);
        }
        if(!pass.equals(conPass)){
            msg = "رقم المرور غير متشابهة مع تاكيد رقم المرور.";
            msgs.add(msg);
        }
        if(password.getText().length() < 4){
            msg = "رقم المرور اقل عن اربعة حروف او ارقام";
            msgs.add(msg);
        }
        if(clinicNumber.getText() == null){
            msg = "يجب اختيار رقم عيادة للمريض";
            msgs.add(msg);
        }
        return msgs;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
