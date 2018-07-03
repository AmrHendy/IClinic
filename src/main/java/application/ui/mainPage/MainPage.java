package application.ui.mainPage;

import application.ui.application.ui.handler.WindowHandlers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainPage {

    private WindowHandlers windowHandlers;
    public MainPage(){
        this.windowHandlers = new WindowHandlers();
    }


    public void searchPatient(MouseEvent mouseEvent) {
        try {
            windowHandlers.loadWindow("/application/ui/displayPatients/displayPatient.fxml",
                    "Search Patients", true,true, mouseEvent);
        }
        catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }

    public void searchUser(MouseEvent mouseEvent) {
        try {
            windowHandlers.loadWindow("/application/ui/displayUsers/displayUsers.fxml", "Search Users", true, true, mouseEvent);
        } catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }


    public void addPatient(MouseEvent mouseEvent) {
        try {
            windowHandlers.loadWindow("/application/ui/addPatient/addPatient.fxml", "Add User", false, true, mouseEvent);
        } catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }

    public void addUser(MouseEvent mouseEvent){
        try {
            windowHandlers.loadWindow("/application/ui/addUsers/addUsers.fxml", "Add User", false, true, mouseEvent);
        } catch (Exception e) {
            //TODO:: put log4j jar for error logging
            e.printStackTrace();
        }
    }
}
