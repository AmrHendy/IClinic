package application.ui.addPatient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import main.java.beans.Patient;
import main.java.model.PatientDAO;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddPatient implements Initializable {
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
    private JFXButton save;

    @FXML
    private JFXButton cancel;

    @FXML
    void cancel(MouseEvent event) {

    }

    @FXML
    void save(MouseEvent event) {
        //TODO:: add validation here.
        Patient patient = new Patient();
        patient.setPatientName(patientName.getText());
        patient.setAddress(address.getText());
        LocalDate date = dateOfBirth.getValue();
        patient.setBirthdate(Date.valueOf(date));
        //TODO:: add set phone number here.
        patient.setPhoneNumber(phoneNumber.getText());
        PatientDAO.addPatient(patient);
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
