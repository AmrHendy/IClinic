package application.ui.displayUsers;

import application.ui.editPassword.EditPassword;
import application.ui.handler.EditCell;
import application.ui.handler.MessagesController;
import application.ui.handler.WindowHandlers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import main.java.beans.User;
import main.java.beans.UserSignedInData;
import main.java.model.UserDAO;
import main.java.services.PasswordEncryptionService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DisplayUser implements Initializable, PropertyChangeListener {

    private final int limit = 10;

    @FXML
    private TextField userName;

    @FXML
    private TableView<User> userTable;


    @FXML
    private TableColumn<User, String> userNameColumn;

    @FXML
    private TableColumn<User, String> clinicNumber;

    @FXML
    private TableColumn<User, String> editPassword;

    private String newEditedPassword;

    private ObservableList<User> tmpTableData;

    void SaveChanges(MouseEvent event) {
        boolean showMsg = false;
        ObservableList<User> list = userTable.getItems();
        ObservableList<User> showThem = FXCollections.observableArrayList();
        ArrayList<String> msg = new ArrayList<String>();
        boolean finished = true;
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            finished = UserDAO.updateUser(user.getUserID(), user);
            if (!finished) {
                msg.add("لا يمكن تعديل " + user.getUserID() + ": " + user.getUserName());
                showMsg = true;
                showThem.add(tmpTableData.get(i));
            } else {
                showThem.add(user);
            }
        }
        if (showMsg) {
            MessagesController.getAlert(msg, Alert.AlertType.INFORMATION);
        }
        userTable.setItems(showThem);
        userTable.refresh();
    }

    @FXML
    void charEntered(KeyEvent event) {
        String name = userName.getText();
        if (name != null) {
            userTable.setItems(FXCollections.observableArrayList(UserDAO.findByName(name)));
        }
        tmpTableData.setAll(userTable.getItems());
    }

    @FXML
    void deleteUser(MouseEvent event) {
        String message = "هل تريد مسح اسم المستخدم؟ سيتم مسح كل المرضى و كل الجلسات المرتبطة بهم هل انت متاكد؟";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "" + message + "", ButtonType.OK, ButtonType.CANCEL);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.CANCEL){
            return;
        }else if(alert.getResult() == ButtonType.OK){

            boolean finished = true;
            boolean showMsg = false;
            ArrayList<String> msg = new ArrayList<String>();
            ObservableList<User> remaining = FXCollections.observableArrayList();
            for (int i = 0; i < userTable.getSelectionModel().getSelectedCells().size(); i++) {
                TablePosition pos = userTable.getSelectionModel().getSelectedCells().get(i);
                int row = pos.getRow();
                User user = userTable.getItems().get(row);
                if(user.getUserName().equals(UserSignedInData.user.getUserName())){
                    finished = UserDAO.deleteUser(user.getUserName());
                    if (!finished) {
                        msg.add("لا يمكن مسح " + user.getUserName());
                        remaining.add(user);
                        showMsg = true;
                    } else {
                        userTable.getItems().set(row, null);
                        message = "لقد قمت بحذف المستخدم الحالى البرنامج سوف يغلق نفسة اعد الدخول مرة اخرى.";
                        MessagesController.getAlert(message, Alert.AlertType.INFORMATION);
                    }
                }else{
                    msg.add("لا يمكن مسح " + user.getUserName());
                    showMsg = true;
                }
            }
            for (int i = 0; i < userTable.getItems().size(); i++) {
                User user = userTable.getItems().get(i);
                if (user != null) {
                    remaining.add(user);
                }
            }
            if (showMsg) {
                MessagesController.getAlert(msg, Alert.AlertType.INFORMATION);
                userTable.setItems(remaining);
            }
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userTable.getSelectionModel().setCellSelectionEnabled(true);
        userTable.setEditable(true);
        userTable.setOnKeyPressed(event -> {
            TablePosition<User, ?> pos = userTable.getFocusModel().getFocusedCell();
            System.out.println(pos.getRow());
            if (pos != null && event.getCode().isLetterKey()) {
                userTable.edit(pos.getRow(), pos.getTableColumn());
            }
        });

        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userNameColumn.setCellFactory(userNameColumn -> EditCell.createStringEditCell());

        userNameColumn.setOnEditCommit(event -> {
            //TODO:: test failed edit.
            int pos = event.getTablePosition().getRow();
            User user = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, user.clone());
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            user.setUserName(value);
            save(pos, user);
            userTable.refresh();
            event.consume();
        });
        clinicNumber.setCellValueFactory(new PropertyValueFactory<>("clinic"));
        clinicNumber.setCellFactory(userNameColumn -> EditCell.createStringEditCell());

        clinicNumber.setOnEditCommit(event -> {
            //TODO:: test failed edit.
            int pos = event.getTablePosition().getRow();
            User user = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, user.clone());
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            user.setClinic(Integer.valueOf(value));
            save(pos, user);
            userTable.refresh();
        });

        editPassword.setCellFactory(getEditPassword());

        userTable.setItems(FXCollections.observableArrayList(UserDAO.findByName("")));
        userTable.refresh();
        tmpTableData = userTable.getItems();
    }

    private void save(int pos, User user){
        if(!user.getUserName().equals(UserSignedInData.user.getUserName()) || !UserDAO.updateUser(user.getUserID(), user)){
            String msg = "لا يمكن تعديل " + user.getUserID() + ": " + user.getUserName();
            MessagesController.getAlert(msg, Alert.AlertType.INFORMATION);
            userTable.setItems(tmpTableData);
        }else{
            userTable.getItems().set(pos, user);
        }
    }

    private Callback<TableColumn<User, String>, TableCell<User, String>> getEditPassword(){
        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory
                = //
                new Callback<TableColumn<User, String>, TableCell<User, String>>() {
                    @Override
                    public TableCell call(final TableColumn<User, String> param) {
                        final TableCell<User, String> cell = new TableCell<User, String>() {

                            final Button btn = new Button("تعديل");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    if(getTableRow() != null && getTableRow().getItem() != null
                                            && getTableRow().getItem().getUserName().equals(UserSignedInData.user.getUserName())){
                                        btn.setOnAction(event -> {
                                            //download image
                                            try {
                                                newEditedPassword = "";
                                                WindowHandlers windowHandlers = WindowHandlers.getInstance();
                                                windowHandlers.loadWindow("/application/ui/editPassword/editPassword.fxml",
                                                        "تعديل كلمة المرور", false, false, false, null);
                                                EditPassword editPassword = windowHandlers.getLoader().getController();
                                                editPassword.addObserver(DisplayUser.this);
                                                if(newEditedPassword != null && !newEditedPassword.equals("")){
                                                    User user = getTableView().getItems().get(getIndex());
                                                    PasswordEncryptionService encryptionService = new PasswordEncryptionService();
                                                    byte[] enc = {};
                                                    byte[] salt = {};
                                                    try{
                                                        salt = encryptionService.generateSalt();
                                                        enc = encryptionService.getEncryptedPassword(newEditedPassword, salt);
                                                    } catch (Exception e) {
                                                        String msg = "لا يمكن اتمام العملية اعد المحاولة.";
                                                        MessagesController.getAlert(msg, Alert.AlertType.ERROR);
                                                        event.consume();
                                                    }
                                                    user.setSalt(salt);
                                                    user.setEncryptedPassword(enc);
                                                    if(!UserDAO.updateUser(user.getUserID(), user)){
                                                        String msg = "لا يمكن اتمام العملية اعد المحاولة.";
                                                        MessagesController.getAlert(msg, Alert.AlertType.ERROR);
                                                        event.consume();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                //TODO:: add log4j jar here.
                                            }

                                        });
                                        setGraphic(btn);
                                        setText(null);
                                    }
                                }
                            }
                        };
                        return cell;
                    }
                };
        return cellFactory;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource() instanceof EditPassword){
            //TODO:: edit the clicked user and UserSigned here.
            this.newEditedPassword = (String) evt.getNewValue();
        }
    }
}
