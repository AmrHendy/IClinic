package application.ui.displayPatients;

import application.ui.handler.EditCell;
import application.ui.handler.MessagesController;
import application.ui.handler.WindowHandlers;
import application.ui.patientProfile.PatientProfile;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
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


    @FXML
    private TableColumn<Patient, String> showProfile;

    @FXML
    private TableColumn<Patient, String> remainingCost;

    private ObservableList<Patient> tmpTableData;

    @FXML
    void nameEntered(KeyEvent event) {
        String name = patientName.getText() == "" ? null : patientName.getText();
        ObservableList<Patient> obs = null;
        if(name != ""){
            obs = UiUtil.getPatientObservable(PatientDAO.findByNameLike(name));
            patientTable.setItems(obs);
        }
        if(obs == null || obs.size() == 0){
            showProfile.setGraphic(null);
            patientTable.getItems().clear();
        }
        patientTable.refresh();
        tmpTableData = patientTable.getItems();
    }

    @FXML
    void idEntered(KeyEvent event){
        String id = patientID.getText() == "" ? null : patientID.getText();
        ObservableList<Patient> obs = null;
        if(id != null){
            obs = UiUtil.getPatientObservable(PatientDAO.findByFileNumberLike(id));
            patientTable.setItems(obs);
        }
        if(obs == null || obs.size() == 0){
            showProfile.setGraphic(null);
            patientTable.getItems().clear();
        }
        patientTable.refresh();
        tmpTableData = patientTable.getItems();
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
            finished = PatientDAO.updatePatient(patient.getFile_number(), patient);
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

        patientTable.getSelectionModel().setCellSelectionEnabled(true);
        patientTable.setEditable(true);
        patientTable.setOnKeyPressed(event -> {
            TablePosition<Patient, ?> pos = patientTable.getFocusModel().getFocusedCell();
            if (pos != null && event.getCode().isLetterKey()) {
                patientTable.edit(pos.getRow(), pos.getTableColumn());
            }
        });
        patientFIleID.setCellFactory(tmp -> EditCell.createStringEditCell());
        patientFIleID.setOnEditCommit(event -> {
            //TODO:: test failed edit.
            int pos = event.getTablePosition().getRow();
            Patient patient = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, patient);
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            patient.setPatientID(Integer.valueOf(value));
            save(patient, pos);
            patientTable.refresh();
        });
        patientNameColumn.setCellFactory(tmp -> EditCell.createStringEditCell());
        patientNameColumn.setOnEditCommit(event -> {
            //TODO:: test failed edit.
            int pos = event.getTablePosition().getRow();
            Patient patient = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, patient);
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            patient.setPatientName(value);
            save(patient, pos);
            patientTable.refresh();
        });
        phoneNumber.setCellFactory(tmp -> EditCell.createStringEditCell());
        phoneNumber.setOnEditCommit(event -> {
            //TODO:: test failed edit.
            int pos = event.getTablePosition().getRow();
            Patient patient = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, patient);
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            patient.setPhoneNumber(value);
            save(patient, pos);
            patientTable.refresh();
        });
        address.setCellFactory(tmp -> EditCell.createStringEditCell());
        address.setOnEditCommit(event -> {
            //TODO:: test failed edit.
            int pos = event.getTablePosition().getRow();
            Patient patient = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, patient);
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            patient.setAddress(value);
            save(patient, pos);
            patientTable.refresh();
        });
        remainingCost.setCellFactory(tmp -> EditCell.createStringEditCell());
        remainingCost.setOnEditCommit(event -> {
            //TODO:: test failed edit.
            int pos = event.getTablePosition().getRow();
            Patient patient = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, patient);
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            patient.setRemainingCost(Integer.valueOf(value));
            save(patient, pos);
            patientTable.refresh();
        });

        patientFIleID.setCellValueFactory(new PropertyValueFactory<>("file_number"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        remainingCost.setCellValueFactory(new PropertyValueFactory<>("remainingCost"));


        Callback<TableColumn<Patient, String>, TableCell<Patient, String>> cellFactory
                = //
                new Callback<TableColumn<Patient, String>, TableCell<Patient, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Patient, String> param) {
                        final TableCell<Patient, String> cell = new TableCell<Patient, String>() {

                            final Button btn = new Button("اظهار الملف الشخصى");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Patient patient = getTableView().getItems().get(getIndex());
                                        WindowHandlers windowHandlers = WindowHandlers.getInstance();
                                        try{
                                            windowHandlers.loadWindow("/application/ui/patientProfile/patientProfile.fxml", "الملف الشخصى" ,
                                                    true, false, false, null);
                                            FXMLLoader loader = windowHandlers.getLoader();
                                            PatientProfile controller = loader.getController();
                                            controller.setPatient(patient);
                                            controller.fillData();
                                        }catch (Exception e){
                                            //TODO:: log4j here.
                                            e.printStackTrace();
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        showProfile.setCellFactory(cellFactory);
    }

    private void save(Patient patient, int pos){
        if(!PatientDAO.updatePatient(tmpTableData.get(pos).getFile_number(), patient)){
            String msg = "لا يمكن تعديل " + patient.getFile_number() + ": " + patient.getPatientName();
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
            patientTable.setItems(tmpTableData);
        }
    }
}
