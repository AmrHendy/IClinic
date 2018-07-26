package application.ui.patientProfile;


import application.ui.handler.CustomImage;
import application.ui.handler.MessagesController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientProfile implements Initializable{


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
    private TableColumn<CustomImage, ImageView> image;

    @FXML
    private TableColumn<Appointment, String> moneyPaid;

    @FXML
    private TableColumn<Appointment, String> confirmPayment;

    @FXML
    private TableColumn<Appointment, String> downloadImage;

    @FXML
    private TableColumn<Appointment, String> uploadImage;

    @FXML
    private JFXDatePicker dateOfBirth;

    @FXML
    private TextField fileNumber;

    private Patient patient;

    private ObservableList<Appointment> tmpTableData;

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
        newPatient.setBirthdate(Date.valueOf(dateOfBirth.getValue()));
        newPatient.setFile_number(fileNumber.getText());
        if(!PatientDAO.updatePatient(patient.getFile_number(), newPatient)){
            String msg = "لم تنجح العملية.";
            Alert alert = MessagesController.getAlert(msg, Alert.AlertType.ERROR);
        }else{
            String msg = "تمت العملية بنجاح.";
            Alert alert = MessagesController.getAlert(msg, Alert.AlertType.ERROR);
        }
    }

    public void fillData() {

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

    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sessionsTable.getSelectionModel().setCellSelectionEnabled(true);
        sessionsTable.setEditable(true);
        sessionsTable.setOnKeyPressed(event -> {
            TablePosition<Appointment, ?> pos = sessionsTable.getFocusModel().getFocusedCell();
            if (pos != null && event.getCode().isLetterKey()) {
                sessionsTable.edit(pos.getRow(), pos.getTableColumn());
            }
        });

        date.setCellValueFactory(new PropertyValueFactory<>("dateOnly"));
        time.setCellValueFactory(new PropertyValueFactory<>("timeOnly"));
        clinicNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        moneyPaid.setCellValueFactory(new PropertyValueFactory<>("paidCost"));
        confirmPayment.setCellValueFactory(new PropertyValueFactory<>("dummy1"));
        uploadImage.setCellValueFactory(new PropertyValueFactory<>("dummy2"));
        downloadImage.setCellValueFactory(new PropertyValueFactory<>("dummy3"));
        image.setCellValueFactory(new PropertyValueFactory<CustomImage, ImageView>("image"));

        confirmPayment.setCellFactory(getConfirm());
        downloadImage.setCellFactory(getDownload());
        uploadImage.setCellFactory(getUpload());
    }

    private Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>> getConfirm(){
        Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>> cellFactory
                = //
                new Callback<TableColumn<Appointment, String>, TableCell<Appointment, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Appointment, String> param) {
                        final TableCell<Appointment, String> cell = new TableCell<Appointment, String>() {

                            final Button btn = new Button("Confirm Payment");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        //TODO:: add confirmation action here.
                                        //take the following code as a guide for you to get the current appointment.
                                        /*Person person = getTableView().getItems().get(getIndex());
                                        System.out.println(person.getFirstName()
                                                + "   " + person.getLastName());*/
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

                            final Button btn = new Button("Save Image");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Appointment appointment = getTableView().getItems().get(getIndex());
                                        java.awt.Image image = appointment.getImage();
                                        FileChooser fileChooser = new FileChooser();
                                        fileChooser.setTitle("Select an Image");
                                        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
                                        File file = fileChooser.showSaveDialog(getScene().getWindow());
                                        if(file != null){
                                            String name = file.getName();
                                            String extension = name.substring(1+name.lastIndexOf(".")).toLowerCase();
                                            try {
                                                ImageIO.write(toBufferedImage(image), extension, file);
                                            } catch (IOException e) {
                                                //TODO:: put log4j here.
                                                String msg = "لم يتم حفظ الصورة.";
                                                Alert alert = MessagesController.getAlert(msg, Alert.AlertType.ERROR);
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

                            final Button btn = new Button("Save Image");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Appointment appointment = getTableView().getItems().get(getIndex());

                                        final FileChooser fileChooser = new FileChooser();
                                        File file = fileChooser.showOpenDialog(getScene().getWindow());
                                        if (file != null) {
                                            try {
                                                Image img = ImageIO.read(file);
                                                appointment.setImage(img);
                                                sessionsTable.getItems().set(getIndex(), appointment);
                                                tmpTableData.setAll(sessionsTable.getItems());
                                                sessionsTable.refresh();
                                            } catch (IOException e) {
                                                //TODO:: add log4j here.
                                                String msg = "لم يتم تحميل الصورة.";
                                                Alert alert = MessagesController.getAlert(msg, Alert.AlertType.ERROR);
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

    private BufferedImage toBufferedImage(Image img){
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }


    @FXML
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
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
        }
        sessionsTable.setItems(showThem);
    }
}
