package application.ui.handler;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;

public class MessagesController {

    public static Alert getAlert(ArrayList<String> messages, Alert.AlertType type){
        String msg = getLines(messages);
        Alert alert = new Alert(type, "" + msg + "", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        return alert;
    }


    public static Alert getAlert (String message, Alert.AlertType type){
        Alert alert = new Alert(type, ""+ message +"", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        return alert;
    }

    private static String getLines(ArrayList<String> messages){
        String ret = "";
        for(String s : messages){
            ret += s + "\\n";
        }
        return ret;
    }
}
