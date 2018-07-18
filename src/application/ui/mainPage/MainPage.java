package application.ui.mainPage;

import application.ui.handler.TimeGenerator;
import application.ui.handler.WindowHandlers;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.java.beans.Appointment;
import main.java.model.AppointmentDAO;
import main.java.util.UiUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
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
    private TableColumn<Appointment, String> money;

    @FXML
    private JFXDatePicker chooseDate;

    @FXML
    private TableView<Appointment> searchSessionsTable;

    @FXML
    private TableColumn<Appointment, String> time1;

    @FXML
    private TableColumn<Appointment, String> patientName1;

    @FXML
    private TableColumn<Appointment, String> patientNumber1;

    @FXML
    private TableColumn<Appointment, String> phoneNumber1;

    @FXML
    private TableColumn<Appointment, String> money1;

    private

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
        time.setCellValueFactory(new PropertyValueFactory<>("timeOnly"));
        patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        patientNumber.setCellValueFactory(new PropertyValueFactory<>("patientFileID"));
        money.setCellValueFactory(new PropertyValueFactory<>("paidCost"));

        time1.setCellValueFactory(new PropertyValueFactory<>("timeOnly"));
        patientName1.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        phoneNumber1.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        patientNumber1.setCellValueFactory(new PropertyValueFactory<>("patientFileID"));
        money1.setCellValueFactory(new PropertyValueFactory<>("paidCost"));
    }


    @FXML
    void saveInSearch(MouseEvent event) {
        ObservableList<Appointment> tableData = searchSessionsTable.getItems();
        for(Appointment item : ta)
    }

    @FXML
    void saveInToday(MouseEvent event){

    }

    @FXML
    void searchAppointment(MouseEvent event) {
        if(chooseDate.getValue() != null){
            SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd");
            //TODO:: search for it.
            Date date = dt.parse(chooseDate.getValue());
            ArrayList<Appointment> list = AppointmentDAO.findByDate(date);
            searchSessionsTable.setItems(UiUtil.getAppointmentObservable(list));

        }
    }



}
