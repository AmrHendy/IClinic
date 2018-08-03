package application.ui.mainPage;

import application.ui.handler.WindowHandlers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        WindowHandlers windowHandlers = WindowHandlers.getInstance();
        windowHandlers.loadWindow("/application/ui/mainPage/mainPage.fxml", "الصفحة الرئيسية", true, false, false, null);
        /*Parent root = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
        primaryStage.setTitle("IClinic");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
