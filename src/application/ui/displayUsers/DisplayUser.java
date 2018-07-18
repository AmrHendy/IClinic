package application.ui.displayUsers;

import application.ui.handler.MessagesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.beans.Patient;
import main.java.beans.User;
import main.java.model.PatientDAO;
import main.java.model.UserDAO;
import main.java.util.UiUtil;

import java.util.ArrayList;

public class DisplayUser {

    @FXML
    private TextField userName;

    @FXML
    private TableView<User> userTable;

    @FXML
    void SaveChanges(MouseEvent event) {
        for(int i = 0 ;i < userTable.getItems().size(); i++){
            User user = userTable.getItems().get(i);
            UserDAO.updateUser(user.getUserID(), user);
        }
    }

    @FXML
    void charEntered(KeyEvent event) {
        //TODO:: search must be with LIKE in SQL so the result may be many of Users.
        User user = UserDAO.getUser(userName.getText());
        userTable.getItems().add(user);
    }

    @FXML
    void deleteUser(MouseEvent event) {
        if(userTable.getSelectionModel().getSelectedCells().size() == 0){
            return;
        }
        boolean finished = true;
        boolean showMsg = false;
        ArrayList<String> msg = new ArrayList<String>();
        ObservableList<User> remaining = FXCollections.observableArrayList();
        //Note selectedCells may need to be selectedItems (type user).
        for (int i = 0; i < userTable.getSelectionModel().getSelectedCells().size(); i++) {
            TablePosition pos = userTable.getSelectionModel().getSelectedCells().get(i);
            int row = pos.getRow();
            User user = userTable.getItems().get(row);
            finished = UserDAO.deleteUser(user.getUserName());
            if(!finished){
                msg.add("لا يمكن مسح " + user.getUserName());
                remaining.add(user);
                showMsg = true;
            }else{
                userTable.getItems().set(row, null);
            }
        }
        for(int i = 0 ;i < userTable.getItems().size(); i++){
            User user = userTable.getItems().get(i);
            if(user != null){
                remaining.add(user);
            }
        }
        if(showMsg){
            MessagesController.getAlert(msg, Alert.AlertType.ERROR);
            userTable.setItems(remaining);
        }
    }
}
