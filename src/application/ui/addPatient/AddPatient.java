package application.ui.addPatient;

import application.ui.handler.MessagesController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.java.beans.Patient;
import main.java.beans.User;
import main.java.model.PatientDAO;
import main.java.model.UserDAO;
import main.java.validator.PatientValidator;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddPatient implements Initializable {
    //mandatory
    @FXML
    private JFXTextField fileNumber;

    //mandatory
    @FXML
    private JFXTextField patientName;

    @FXML
    private JFXTextField address;
    //mandatory
    @FXML
    private JFXTextField phoneNumber;

    @FXML
    private JFXDatePicker dateOfBirth;
    //mandatory
    @FXML
    private JFXComboBox<String> clinicNumber;

    @FXML
    private JFXTextField requiredCost;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;

    @FXML
    void cancel(MouseEvent event) {
        if(!patientName.getText().equals("") || !address.getText().equals("")||
                !phoneNumber.getText().equals("")|| dateOfBirth.getValue() != null
                || clinicNumber.getValue() != null || !fileNumber.getText().equals("")){
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
    void save(MouseEvent event) {

        Patient patient = new Patient();
        patient.setPatientName(patientName.getText());
        patient.setAddress(address.getText());
        LocalDate date = dateOfBirth.getValue();
        if(date != null){
            patient.setBirthdate(Date.valueOf(date));
        }
        patient.setPhoneNumber(phoneNumber.getText());
        patient.setFile_number(fileNumber.getText());
        String req = (requiredCost.getText().isEmpty()) ? "0" : requiredCost.getText();
        patient.setRemainingCost(Integer.valueOf(req));
        if(clinicNumber.getValue() != null){
            patient.setClinic_number(clinicNumber.getValue());
        }
        ArrayList<String> msgs = PatientValidator.insertingValidator(patient);
        if(msgs.size() > 0){
            Alert alert = MessagesController.getAlert(msgs, Alert.AlertType.INFORMATION);
        }else{
            if(!PatientDAO.addPatient(patient)){
                String msg = "لا يمكن اتمام اضافة مريض اعد المحاولة.";
                Alert alert = MessagesController.getAlert(msg, Alert.AlertType.INFORMATION);
            }else{
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO:: How to add clinics.
        ObservableList<String> options = FXCollections.observableArrayList(UserDAO.getClinics());
        clinicNumber.getItems().addAll(options);
    }
}
