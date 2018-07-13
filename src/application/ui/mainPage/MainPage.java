package application.ui.mainPage;

import application.ui.handler.ScheduleLine;
import application.ui.handler.TimeGenerator;
import application.ui.handler.WindowHandlers;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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
    private TableView<ScheduleLine> TodaySession;

    @FXML
    private TableColumn<ScheduleLine, String> time;

    @FXML
    private TableColumn<ScheduleLine, String> patientNumber;

    @FXML
    private TableColumn<ScheduleLine, String> patientName;

    @FXML
    private TableColumn<ScheduleLine, String> phoneNumber;

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

        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        patientNumber.setCellValueFactory(new PropertyValueFactory<>("patientNumber"));

        TimeGenerator timeGenerator = new TimeGenerator();
        ObservableList<ScheduleLine> list = timeGenerator.getDayTimeEmpty();
        TodaySession.setItems(list);

    }

}
