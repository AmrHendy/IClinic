package application.ui.mainPage;

import application.ui.handler.WindowHandlers;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPage implements Initializable {

    @FXML
    private TableView<Patient> TodaySession;

    @FXML
    private TableColumn<Patient, String> date;

    @FXML
    private TableColumn<Patient, String> patientName;

    @FXML
    private TableColumn<Patient, String> phoneNumber;

    private WindowHandlers windowHandlers;


    public MainPage(){
        this.windowHandlers = WindowHandlers.getInstance();
    }


    public void searchPatient(MouseEvent mouseEvent) {
        try {
            windowHandlers.loadWindow("/application/ui/displayPatients/displayPatient.fxml",
                    "Search Patients", true,true, mouseEvent);

        }
        catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }

    public void searchUser(MouseEvent mouseEvent) {
        try {
            windowHandlers.loadWindow("/application/ui/displayUsers/displayUsers.fxml", "Search Users", true, true, mouseEvent);
        } catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }


    public void addPatient(MouseEvent mouseEvent) {
        try {
            windowHandlers.loadWindow("/application/ui/addPatient/addPatient.fxml", "Add User", false, true, mouseEvent);
        } catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }

    public void addUser(MouseEvent mouseEvent){
        try {
            windowHandlers.loadWindow("/application/ui/addUsers/addUsers.fxml", "Add User", false, true, mouseEvent);
        } catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addDates();
    }

    private void addDates(){
        Patient p = new Patient("sdlkfj", "name", "01234");
        TodaySession.getItems().add(p);
    }

    public class Patient{
        private SimpleStringProperty date;
        private SimpleStringProperty patientName;
        private SimpleStringProperty phoneNumber;

        public Patient(String date, String patientName, String phoneNumber){
            this.date = new SimpleStringProperty(date);
            this.phoneNumber = new SimpleStringProperty(phoneNumber);
            this.patientName = new SimpleStringProperty(patientName);
        }
        public String getDate() {
            return date.get();
        }
        public String getPatientName() {
            return patientName.get();
        }

        public String getPhoneNumber() {
            return phoneNumber.get();
        }




    }
}
