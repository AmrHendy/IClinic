package application.ui.handler;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

import java.util.ArrayList;

public class MessagesController {

    public static Alert getAlert(ArrayList<String> messages, Alert.AlertType type){
        String msg = getLines(messages);
        Alert alert = new Alert(type, "" + msg + "", ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
        return alert;
    }


    public static Alert getAlert (String message, Alert.AlertType type){
        Alert alert = new Alert(type, ""+ message +"", ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
        return alert;
    }

    private static String getLines(ArrayList<String> messages){
        String ret = "";
        for(String s : messages){
            ret += s + ".  ";
        }
        return ret;
    }
}
