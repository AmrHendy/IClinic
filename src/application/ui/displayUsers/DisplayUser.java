package application.ui.displayUsers;

import application.ui.handler.MessagesController;
import application.ui.handler.UserSingedInData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @FXML
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
    }

    @FXML
    void charEntered(KeyEvent event) {
        String name = userName.getText();
        if (name != null && name == UserSingedInData.user.getUserName()) {
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
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        clinicNumber.setCellValueFactory(new PropertyValueFactory<>("clinic"));

        ObservableList<User> alreadyLoggedIn = FXCollections.observableArrayList();
        alreadyLoggedIn.add(UserSingedInData.user);
        userTable.setItems(alreadyLoggedIn);
    }
}
