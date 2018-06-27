package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Search Patient");
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);

        Scene scene = new Scene(root);
        TableView tb = (TableView) scene.lookup("#patientTable");

        /* initialize two CustomImage objects and add them to the observable list */
        ObservableList<CustomImage> imgList = FXCollections.observableArrayList();
        CustomImage item_1 = new CustomImage(new ImageView(new Image("index.png")));
        CustomImage item_2 = new CustomImage(new ImageView(new Image("index.png")));
        imgList.add(item_1);
        imgList.add(item_2);


        /* initialize and specify table column */
        TableColumn<CustomImage, ImageView> firstColumn = new TableColumn<CustomImage, ImageView>("Images");
        firstColumn.setCellValueFactory(new PropertyValueFactory<CustomImage, ImageView>("image"));
        firstColumn.setPrefWidth(60);


        /* add column to the tableview and set its items */
        tb.getColumns().add(firstColumn);
        tb.setItems(imgList);


        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
