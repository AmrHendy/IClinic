package application.ui.handler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowHandlers {

    public void loadWindow(String windowPath, String title, boolean maximized, boolean hide, MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(windowPath));
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setMaximized(maximized);
        stage.show();
        if(hide){
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }

}
