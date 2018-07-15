package application.ui.displayUsers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.beans.User;
import main.java.model.PatientDAO;
import main.java.model.UserDAO;
import main.java.util.UiUtil;

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
        for (int i = 0; i < userTable.getSelectionModel().getSelectedCells().size(); i++) {
            TablePosition pos = userTable.getSelectionModel().getSelectedCells().get(i);
            int row = pos.getRow();
            User user = userTable.getItems().get(row);
            //TODO:: add delete user function.
            UserDAO.delete(user.getUserName());
        }
    }
}
