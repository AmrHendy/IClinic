package application.ui.login;
import application.ui.handler.Configuration;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.model.UserDAO;
import main.java.services.PasswordEncryptionService;

import java.util.Arrays;

public class LoginFormApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login_form.fxml"));
        primaryStage.setTitle("IClinic");
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        root.styleProperty().bind(Bindings.concat("-fx-font-size: ", Configuration.FONT_SIZE));
        //primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //automaticly register admin acconut if not exist
        UserDAO.registerAdmin();
        launch(args);
    }
}