package application.ui.mainPage;

import application.ui.handler.TimeGenerator;
import application.ui.handler.WindowHandlers;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
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
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class MainPage implements Initializable {

    @FXML
    private TableView<Appointment> todaySession;

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

    private ObservableList<Appointment> tmpTodayTableData;

    private ObservableList<Appointment> tmpSearchTableData;

    private WindowHandlers windowHandlers;


    public MainPage(){
        this.windowHandlers = WindowHandlers.getInstance();
    }


    public void searchPatient(MouseEvent mouseEvent) {
        try {
            windowHandlers.loadWindow("/application/ui/displayPatients/displayPatient.fxml",
                    "بحث المرضى", true,true, true, mouseEvent);

        }
        catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }

    public void searchUser(MouseEvent mouseEvent) {
        try {
            windowHandlers.loadWindow("/application/ui/displayUsers/displayUsers.fxml",
                    "بحث عن مستخدم", true, true, true, mouseEvent);
        } catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }


    public void addPatient(MouseEvent mouseEvent) {
        try {
            windowHandlers.loadWindow("/application/ui/addPatient/addPatient.fxml", "اضافة مريض", false, true, true, mouseEvent);
        } catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }

    public void addUser(MouseEvent mouseEvent){
        try {
            windowHandlers.loadWindow("/application/ui/addUsers/addUsers.fxml",
                    "اضافة مستخدم", false, true, true, mouseEvent);
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
        //TODO:: initialize today session here.
        todaySession.setItems(UiUtil.getAppointmentObservable(AppointmentDAO.findByDate(getDate())));
        tmpTodayTableData = searchSessionsTable.getItems();
    }


    @FXML
    void saveInSearch(MouseEvent event) {
        ObservableList<Appointment> appointments = searchSessionsTable.getItems();
        ObservableList<Appointment> remaining = FXCollections.observableArrayList();
        ArrayList<String> msgs = new ArrayList<>();
        for(int i = 0 ;i < appointments.size(); i++){
            Appointment appointment = appointments.get(i);
            if(AppointmentDAO.updateAppointmentByID(appointment.getAppointmentID(), appointment)){
                remaining.add(appointment);
                String msg = "لا يمكن تعديل " + appointment.getPatientName() + " : " + appointment.getPatientFileID();
                msgs.add(msg);
            }else{
                remaining.add(tmpSearchTableData.get(i));
            }
        }
        searchSessionsTable.setItems(remaining);
        tmpSearchTableData.setAll(remaining);
    }

    @FXML
    void saveInToday(MouseEvent event){
        ObservableList<Appointment> appointments = todaySession.getItems();
        ObservableList<Appointment> remaining = FXCollections.observableArrayList();
        ArrayList<String> msgs = new ArrayList<>();
        for(int i = 0 ;i < appointments.size(); i++){
            Appointment appointment = appointments.get(i);
            if(AppointmentDAO.updateAppointmentByID(appointment.getAppointmentID(), appointment)){
                remaining.add(appointment);
                String msg = "لا يمكن تعديل " + appointment.getPatientName() + " : " + appointment.getPatientFileID();
                msgs.add(msg);
            }else{
                remaining.add(tmpTodayTableData.get(i));
            }
        }
        todaySession.setItems(remaining);
        tmpTodayTableData.setAll(remaining);
    }

    @FXML
    void searchAppointment(MouseEvent event) {
        if(chooseDate.getValue() != null){
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = getDate();
            ArrayList<Appointment> list = AppointmentDAO.findByDate(date);
            searchSessionsTable.setItems(UiUtil.getAppointmentObservable(list));
            tmpSearchTableData.setAll(searchSessionsTable.getItems());
        }
    }

    private Date getDate() {
        LocalDate localDate = chooseDate.getValue();
        if(chooseDate.getValue() == null){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            return today;
        }
        Date ret = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println(ret);
        return ret;
    }
}
