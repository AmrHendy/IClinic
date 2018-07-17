package application.ui.mainPage;

import application.ui.handler.TimeGenerator;
import application.ui.handler.WindowHandlers;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.java.beans.Appointment;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPage implements Initializable {

    @FXML
    private TableView<Appointment> TodaySession;

    @FXML
    private TableColumn<Appointment, String> time;

    @FXML
    private TableColumn<Appointment, String> patientNumber;

    @FXML
    private TableColumn<Appointment, String> patientName;

    @FXML
    private TableColumn<Appointment, String> phoneNumber;

    @FXML
    private TableView<Appointment> searchSessionsTable;

    @FXML
    private TableColumn<Appointment, String> time1;

    @FXML
    private TableColumn<Appointment, ?> patientName1;

    @FXML
    private TableColumn<Appointment, ?> patientNumber1;

    @FXML
    private TableColumn<Appointment, ?> phoneNumber1;

    @FXML
    private JFXComboBox<String> timePeriods;

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
        //TODO:: get current day scheudle function here you can use Time generator in handler package.
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        patientNumber.setCellValueFactory(new PropertyValueFactory<>("patientNumber"));

        time1.setCellValueFactory(new PropertyValueFactory<>("time"));
        patientName1.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        phoneNumber1.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        patientNumber1.setCellValueFactory(new PropertyValueFactory<>("patientNumber"));

        TimeGenerator timeGenerator = new TimeGenerator();
        ObservableList<Appointment> list = timeGenerator.getDayTimeEmpty();
        timePeriods.getItems().setAll();

        TodaySession.setItems(list);
    }



}
