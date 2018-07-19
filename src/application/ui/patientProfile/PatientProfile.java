package application.ui.patientProfile;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.java.beans.Appointment;
import main.java.beans.Patient;
import main.java.model.AppointmentDAO;
import main.java.model.UserDAO;
import main.java.util.UiUtil;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PatientProfile {


    @FXML
    private TextField patientName;

    @FXML
    private TextField address;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField remainingMoney;


    @FXML
    private JFXComboBox<String> clinicNumberChooser;

    @FXML
    private TableColumn<Appointment, String> clinicNumber;

    @FXML
    private TableView<Appointment> sessionsTable;

    @FXML
    private TableColumn<Appointment, String> time;

    @FXML
    private TableColumn<Appointment, String> image;

    @FXML
    private TableColumn<Appointment, String> moneyPaid;

    @FXML
    private TableColumn<Appointment, String> confirmPayment;


    @FXML
    private JFXDatePicker dateOfBirth;

    @FXML
    private TextField fileNumber;

    private Patient patient;

    @FXML
    void save(MouseEvent event) {

    }

    public void fillData(){
        ObservableList<String> list = FXCollections.observableArrayList(UserDAO.getClinics());
        clinicNumberChooser.setItems(list);
        sessionsTable.setItems(UiUtil.getAppointmentObservable(AppointmentDAO.findByPatientID(Integer.valueOf(patient.getPatientID()))));

        patientName.setText(patient.getPatientName());
        address.setText(patient.getAddress());
        phoneNumber.setText(patient.getPhoneNumber());
        remainingMoney.setText(String.valueOf(patient.getRemainingCost()));
        clinicNumberChooser.getSelectionModel().select(patient.getClinic_number());
        LocalDate date = LocalDate.parse(patient.getDateString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateOfBirth.setValue(date);
        fileNumber.setText(patient.getFile_number());
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }
}
