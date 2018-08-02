package application.ui.handler;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;

public class MessagesController {

    public static Alert getAlert(ArrayList<String> messages, Alert.AlertType type){
        String msg = getLines(messages);
        Alert alert = new Alert(type, "" + msg + "", ButtonType.OK);
        //alert.getGraphic().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.showAndWait();
        return alert;
    }


    public static Alert getAlert (String message, Alert.AlertType type){
        Alert alert = new Alert(type, ""+ message +"", ButtonType.OK);
        //alert.getGraphic().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
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
