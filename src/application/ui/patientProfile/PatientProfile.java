package application.ui.patientProfile;


import application.ui.displayPatients.DisplayPatient;
import application.ui.handler.CustomImage;
import application.ui.handler.EditCell;
import application.ui.handler.MessagesController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import main.java.beans.Appointment;
import main.java.beans.Patient;
import main.java.model.AppointmentDAO;
import main.java.model.PatientDAO;
import main.java.model.UserDAO;
import main.java.util.UiUtil;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientProfile implements Initializable  {


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
    private TableColumn<Appointment, ImageView> image;

    @FXML
    private TableColumn<Appointment, String> moneyPaid;

    @FXML
    private TableColumn<Appointment, String> confirmPayment;

    @FXML
    private TableColumn<Appointment, String> downloadImage;

    @FXML
    private TableColumn<Appointment, String> uploadImage;

    @FXML
    private TableColumn<Appointment, String> comment;

    @FXML
    private JFXDatePicker dateOfBirth;

    @FXML
    private TextField fileNumber;

    private Patient patient;

    private ObservableList<Appointment> tmpTableData;

    private DisplayPatient controller;

    @FXML
    private TableColumn<?, ?> date;

    @FXML
    void save(MouseEvent event) {
        Patient newPatient = new Patient();
        newPatient.setPatientName(patientName.getText());
        newPatient.setAddress(address.getText());
        newPatient.setPhoneNumber(phoneNumber.getText());
        newPatient.setRemainingCost(Integer.valueOf(remainingMoney.getText()));
        newPatient.setClinic_number(Integer.valueOf(clinicNumberChooser.getValue()));
        if(dateOfBirth.getValue() != null){
            newPatient.setBirthdate(Date.valueOf(dateOfBirth.getValue()));
        }
        newPatient.setFile_number(fileNumber.getText());
        if(!PatientDAO.updatePatient(patient.getFile_number(), newPatient)){
            String msg = "لم تنجح العملية.";
            Alert alert = MessagesController.getAlert(msg, Alert.AlertType.INFORMATION);
        }else{
            String msg = "تمت العملية بنجاح.";
            Alert alert = MessagesController.getAlert(msg, Alert.AlertType.INFORMATION);
            controller.propertyChange(new PropertyChangeEvent(this, "all", patient, newPatient));
        }
    }

    public void fillData(DisplayPatient controller) {

        ObservableList<String> list = FXCollections.observableArrayList(UserDAO.getClinics());
        clinicNumberChooser.setItems(list);
        sessionsTable.setItems(UiUtil.getAppointmentObservable(AppointmentDAO.findByPatientID(Integer.valueOf(patient.getPatientID()))));
        tmpTableData = sessionsTable.getItems();

        patientName.setText(patient.getPatientName());
        address.setText(patient.getAddress());
        phoneNumber.setText(patient.getPhoneNumber());
        remainingMoney.setText(String.valueOf(patient.getRemainingCost()));
        clinicNumberChooser.getSelectionModel().select(patient.getClinic_number());
        if (!patient.getDateString().isEmpty()){
            LocalDate date = LocalDate.parse(patient.getDateString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dateOfBirth.setValue(date);
        }
        fileNumber.setText(patient.getFile_number());
        this.controller = controller;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sessionsTable.setEditable(true);
        sessionsTable.getSelectionModel().setCellSelectionEnabled(true);
        date.setCellValueFactory(new PropertyValueFactory<>("dateOnly"));
        time.setCellValueFactory(new PropertyValueFactory<>("timeOnly"));
        clinicNumber.setCellValueFactory(new PropertyValueFactory<>("clinicNumber"));
        moneyPaid.setCellValueFactory(new PropertyValueFactory<>("paidCost"));
        moneyPaid.setCellFactory(money -> EditCell.createStringEditCell());
        moneyPaid.setOnEditCommit(event -> {
            int pos = event.getTablePosition().getRow();
            Appointment appointment = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, appointment.clone());
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            appointment.setPaidCost(Integer.valueOf(value));
            editComment(pos, appointment);
            sessionsTable.refresh();
        });
        image.setCellValueFactory(new PropertyValueFactory<Appointment, ImageView>("image"));
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        comment.setCellFactory(comment -> EditCell.createStringEditCell());
        comment.setOnEditCommit(event -> {
            int pos = event.getTablePosition().getRow();
            Appointment appointment = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, appointment.clone());
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            appointment.setComment(value);
            editComment(pos, appointment);
            sessionsTable.refresh();
        });
        confirmPayment.setCellFactory(getConfirm());
        downloadImage.setCellFactory(getDownload());
        uploadImage.setCellFactory(getUpload());
    }

    private void editComment(int pos, Appointment appointment){
        if(!AppointmentDAO.updateAppointmentByID(appointment.getAppointmentID(), appointment)) {
            String msg = "لم يتم التعديل";
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
            sessionsTable.setItems(tmpTableData);
        }else{
            sessionsTable.getItems().set(pos, appointment);
        }
    }

    private Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>> getConfirm(){
        Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>> cellFactory
                = //
                new Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Appointment, String> param) {
                        final TableCell<Appointment, String> cell = new TableCell<Appointment, String>() {

                            final Button btn = new Button("تاكيد الدفع");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if(getTableRow() != null && getTableRow().getItem() != null){
                                    btn.setDisable(getTableRow().getItem().isConfirmedPaid());
                                }
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Appointment appointment = getTableView().getItems().get(getIndex());
                                        AppointmentDAO.confirmPaidCost(appointment.getPatientFileID(), appointment.getDate());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        return cellFactory;
    }

    private Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>> getDownload(){
        Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>> cellFactory
                = //
                new Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Appointment, String> param) {
                        final TableCell<Appointment, String> cell = new TableCell<Appointment, String>() {

                            final Button btn = new Button("تنزيل صورة");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        //download image

                                        Appointment appointment = getTableView().getItems().get(getIndex());
                                        BufferedImage img = appointment.getBufferdImage();
                                        FileChooser fileChooser = new FileChooser();
                                        fileChooser.setTitle("Select an Image");
                                        fileChooser.setInitialFileName("" + appointment.getPatientName() + " : " + appointment.getDateString() + ".jpg");
                                        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
                                        File file = fileChooser.showSaveDialog(getScene().getWindow());
                                        if(file != null){
                                            String name = file.getName();
                                            String extension = name.substring(1+name.lastIndexOf(".")).toLowerCase();
                                            try {
                                                ImageIO.write(img, extension, file);
                                            } catch (IOException e) {
                                                //TODO:: put log4j here.
                                                String msg = "لم يتم حفظ الصورة.";
                                                Alert alert = MessagesController.getAlert(msg, Alert.AlertType.INFORMATION);
                                            }
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
        return cellFactory;
    }

    private Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>> getUpload(){

        Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>> cellFactory
                = //
                new Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Appointment, String> param) {
                        final TableCell<Appointment, String> cell = new TableCell<Appointment, String>() {

                            final Button btn = new Button("رفع الصورة");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        //upload image.
                                        Appointment appointment = getTableView().getItems().get(getIndex());

                                        final FileChooser fileChooser = new FileChooser();
                                        File file = fileChooser.showOpenDialog(getScene().getWindow());
                                        if (file != null) {
                                            try {
                                                BufferedImage img = ImageIO.read(file);
                                                javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(img, null);
                                                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
                                                imageView.setImage(fxImage);
                                                appointment.setImage(imageView);
                                                AppointmentDAO.updateAppointmentByID(appointment.getAppointmentID(), appointment);
                                                sessionsTable.getItems().set(getIndex(), appointment);
                                                tmpTableData  = sessionsTable.getItems() ;
                                                sessionsTable.refresh();
                                            } catch (IOException e) {
                                                //TODO:: add log4j here.
                                                String msg = "لم يتم تحميل الصورة.";
                                                Alert alert = MessagesController.getAlert(msg, Alert.AlertType.INFORMATION);
                                            }
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
        return cellFactory;
    }




    void saveTableData(MouseEvent event) {
        boolean showMsg = false;
        ObservableList<Appointment> list = sessionsTable.getItems();
        ObservableList<Appointment> showThem = FXCollections.observableArrayList();
        ArrayList<String> msg = new ArrayList<String>();
        boolean finished = true;
        for(int i = 0 ;i < list.size(); i++){
            Appointment appointment  = list.get(i);
            finished = AppointmentDAO.updateAppointmentByID(appointment.getAppointmentID(), appointment);
            if(!finished){
                msg.add("لا يمكن تعديل " + appointment.getDate() + ": " + patient.getPatientName());
                showMsg = true;
                showThem.add(tmpTableData.get(i));
            }else{
                showThem.add(appointment);
            }
        }
        if(showMsg){
            MessagesController.getAlert(msg, Alert.AlertType.INFORMATION);
        }
        sessionsTable.setItems(showThem);
    }
}
