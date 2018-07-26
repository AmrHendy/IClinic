package application.ui.displayUsers;

import application.ui.handler.EditCell;
import application.ui.handler.MessagesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.beans.User;
import main.java.beans.UserSignedInData;
import main.java.model.UserDAO;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DisplayUser implements Initializable {

    private final int limit = 10;

    @FXML
    private TextField userName;

    @FXML
    private TableView<User> userTable;


    @FXML
    private TableColumn<User, String> userNameColumn;

    @FXML
    private TableColumn<User, String> clinicNumber;

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
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
        }
        userTable.setItems(showThem);
        userTable.refresh();
    }

    @FXML
    void charEntered(KeyEvent event) {
        String name = userName.getText();
        if (name != null && name == UserSignedInData.user.getUserName()) {
            userTable.setItems(FXCollections.observableArrayList(UserDAO.getUser(name)));
        }
        tmpTableData.setAll(userTable.getItems());
    }

    @FXML
    void deleteUser(MouseEvent event) {
        boolean finished = true;
        boolean showMsg = false;
        ArrayList<String> msg = new ArrayList<String>();
        ObservableList<User> remaining = FXCollections.observableArrayList();
        for (int i = 0; i < userTable.getSelectionModel().getSelectedCells().size(); i++) {
            TablePosition pos = userTable.getSelectionModel().getSelectedCells().get(i);
            int row = pos.getRow();
            User user = userTable.getItems().get(row);
            finished = UserDAO.deleteUser(user.getUserName());
            if (!finished) {
                msg.add("لا يمكن مسح " + user.getUserName());
                remaining.add(user);
                showMsg = true;
            } else {
                userTable.getItems().set(row, null);
            }
        }
        for (int i = 0; i < userTable.getItems().size(); i++) {
            User user = userTable.getItems().get(i);
            if (user != null) {
                remaining.add(user);
            }
        }
        if (showMsg) {
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
            userTable.setItems(remaining);
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
            tmpTableData.set(pos, user);
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            user.setUserName(value);
            save(user);
            userTable.refresh();
        });

        clinicNumber.setCellValueFactory(new PropertyValueFactory<>("clinic"));
        clinicNumber.setCellFactory(userNameColumn -> EditCell.createStringEditCell());

        clinicNumber.setOnEditCommit(event -> {
            //TODO:: test failed edit.
            int pos = event.getTablePosition().getRow();
            User user = event.getTableView().getItems().get(pos);
            tmpTableData.set(pos, user);
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            user.setClinic(Integer.valueOf(value));
            save(user);
            userTable.refresh();
        });

        ObservableList<User> alreadyLoggedIn = FXCollections.observableArrayList();
        alreadyLoggedIn.add(UserSignedInData.user);
        userTable.setItems(alreadyLoggedIn);
        tmpTableData = userTable.getItems();
    }

    private void save(User user){
        if(!UserDAO.updateUser(user.getUserID(), user)){
            String msg = "لا يمكن تعديل " + user.getUserID() + ": " + user.getUserName();
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
            userTable.setItems(tmpTableData);
        }

    }
}
