package application.ui.addUsers;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUsers implements Initializable {

    @FXML
    private JFXComboBox<String> clinicNumber;

    @FXML
    public void cancel(MouseEvent event) {

    }

    @FXML
    public void save(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO:: add Values from database.
        ObservableList<String> options = FXCollections.observableArrayList(
                        "1",
                        "2"
                );
        clinicNumber.getItems().addAll(options);
    }
}
