package application.ui.login;
import javafx.application.Application;
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
        primaryStage.setScene(new Scene(root, 500, 500));
        //primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        primaryStage.show();
        //automaticly register admin acconut if not exist
    }

    public static void main(String[] args) {
        UserDAO.registerAdmin();
        launch(args);
        /*
        try {
            byte[] salt = {91, 66, 64, 54, 52, 49, 102, 102, 52, 56, 52};
            PasswordEncryptionService pw = new PasswordEncryptionService();
            byte[] encr = pw.getEncryptedPassword("admin", salt);
            System.out.println(Arrays.toString(encr));
            System.out.println(pw.authenticate("admin", encr, salt));
        } catch (Exception e) {

        }
        */
    }
}