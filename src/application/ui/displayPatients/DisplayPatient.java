package application.ui.displayPatients;

import application.ui.handler.MessagesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.beans.Patient;
import main.java.beans.User;
import main.java.model.PatientDAO;
import main.java.model.UserDAO;
import main.java.util.UiUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DisplayPatient implements Initializable {

    private final int limit = 10;

    @FXML
    private TextField patientID;

    @FXML
    private TextField patientName;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, String> patientNameColumn;

    @FXML
    private TableColumn<Patient, String> patientFIleID;

    @FXML
    private TableColumn<Patient, String> phoneNumber;

    @FXML
    private TableColumn<Patient, String> address;

    private ObservableList<Patient> tmpTableData;

    @FXML
    void charEntered(KeyEvent event) {
        String name = patientName.getText();
        String id = patientID.getText();
        if(name != null){
            ObservableList<Patient> obs = UiUtil.getPatientObservable(PatientDAO.findByName(name, limit));
            patientTable.setItems(obs);
        }else if(id != null){
            Patient patient= PatientDAO.findByFileNumber(id);
            patientTable.getItems().add(patient);
        }
        tmpTableData.setAll(patientTable.getItems());
    }

    @FXML
    void deletePatients(MouseEvent event) {
        boolean finished = true;
        boolean showMsg = false;
        ArrayList<String> msg = new ArrayList<String>();
        ObservableList<Patient> remaining = FXCollections.observableArrayList();
        for (int i = 0; i < patientTable.getSelectionModel().getSelectedCells().size(); i++) {
            TablePosition pos = patientTable.getSelectionModel().getSelectedCells().get(i);
            int row = pos.getRow();
            Patient patient= patientTable.getItems().get(row);
            finished = PatientDAO.deletePatient(patient.getPatientID());
            if(!finished){
                msg.add("لا يمكن مسح " + patient.getPatientID() + " " + patient.getPatientName());
                remaining.add(patient);
                showMsg = true;
            }else{
                patientTable.getItems().set(row, null);
            }
        }
        for(int i = 0 ;i < patientTable.getItems().size(); i++){
            Patient patient = patientTable.getItems().get(i);
            if(patient != null){
                remaining.add(patient);
            }
        }
        if(showMsg){
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
            patientTable.setItems(remaining);
        }
    }

    @FXML
    void saveChanges(MouseEvent event) {
        boolean showMsg = false;
        ObservableList<Patient> list = patientTable.getItems();
        ObservableList<Patient> showThem = FXCollections.observableArrayList();
        ArrayList<String> msg = new ArrayList<String>();
        boolean finished = true;
        for(int i = 0 ;i < list.size(); i++){
            Patient patient  = list.get(i);
            finished = PatientDAO.updatePatient(patient.getPatientID(), patient);
            if(!finished){
                msg.add("لا يمكن تعديل " + patient.getPatientID() + ": " + patient.getPatientName());
                showMsg = true;
                showThem.add(tmpTableData.get(i));
            }else{
                showThem.add(patient);
            }
        }
        if(showMsg){
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
        }
        patientTable.setItems(showThem);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO:: add remaining money here also.
        patientFIleID.setCellValueFactory(new PropertyValueFactory<>("file_number"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        ObservableList<Patient> firstTenID = FXCollections.observableArrayList();
        for(int i = 0 ;i < limit; i++){
            //TODO:: find by id must return only one Patient?
            //firstTenID.add(PatientDAO.findByID(i));
        }
        patientTable.getItems().setAll(firstTenID);
        //tmpTableData.setAll(patientTable.getItems());
    }
}
