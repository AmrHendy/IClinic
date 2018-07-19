package application.ui.handler;

import application.ui.patientProfile.PatientProfile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowHandlers extends Application {

    private WindowObj prevWindow;
    private static WindowHandlers windowHandlers;
    private Stage stage;
    private FXMLLoader loader;

    private WindowHandlers(){
        this.prevWindow = new WindowObj();
    }

    public void loadWindow(String windowPath, String title, boolean maximized, boolean hide, boolean onclose, MouseEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(windowPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setMaximized(maximized);
        stage.setResizable(maximized);
        stage.show();
        this.stage = stage;
        this.loader = loader;
        if(hide){
            ((Node)(event.getSource())).getScene().getWindow().hide();
            this.prevWindow.setMaximized(true);
            this.prevWindow.setScene(((Node)(event.getSource())).getScene());
            this.prevWindow.setTitle("Main Page");
        }
        if(onclose){
            stage.setOnCloseRequest(e -> closeWindow());
        }
    }

    @Override
    public void start(Stage stage){

    }

    public void closeWindow() {
        try{
            Stage stage = new Stage();
            stage.setTitle(prevWindow.getTitle());
            stage.setScene(this.prevWindow.getScene());
            stage.setMaximized(prevWindow.getMaximized());
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public FXMLLoader getLoader() {return this.loader;}
    public Stage getStage(){
        return this.stage;
    }

    public static WindowHandlers getInstance(){
        if(windowHandlers == null){
            windowHandlers = new WindowHandlers();
            return windowHandlers;
        }
        return windowHandlers;
    }

}
