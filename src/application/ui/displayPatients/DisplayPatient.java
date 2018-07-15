package application.ui.displayPatients;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.beans.Patient;
import main.java.model.PatientDAO;
import main.java.util.UiUtil;

public class DisplayPatient {
    private final int limit = 10;

    @FXML
    private TextField patientID;

    @FXML
    private TextField patientName;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    void charEntered(KeyEvent event) {
        String name = patientName.getText();
        String id = patientID.getText();
        if(name != null && id != null) {
            //TODO:: add function to get by id and name (AND).
        }else if(name != null){
            ObservableList<Patient> obs = UiUtil.getPatientObservable(PatientDAO.findByName(name, limit));
            patientTable.setItems(obs);
        }else if(id != null){
            Patient patient= PatientDAO.findByFileNumber(id);
            patientTable.getItems().add(patient);
        }
    }

    @FXML
    void deletePatients(MouseEvent event) {

    }

    @FXML
    void saveChanges(MouseEvent event) {

    }
}
