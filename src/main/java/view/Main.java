package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage){
        Group group = new Group();
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/MainForm.fxml"));
        }catch(IOException e){
            e.printStackTrace();
        }
        BorderPane root = new BorderPane();
        root.setCenter(parent);

        group.getChildren().add(root);

        Scene scene = new Scene(group, 600, 400);
        primaryStage.setTitle("Rating");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}