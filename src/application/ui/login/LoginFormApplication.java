package application.ui.login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.model.UserDAO;

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
        UserDAO.registerAdmin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}