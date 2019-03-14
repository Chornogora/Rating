package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
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

        group.getChildren().add(parent);

        Scene scene = new Scene(group, 600, 400);
        primaryStage.setTitle("Rating");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Отображение окна с данными о пользователе

        Button authorBut = (Button)parent.lookup("#AuthorButton");
        authorBut.setOnMouseClicked(this::showAuthorForm);

        Button tableBut = (Button)parent.lookup("#TableButton");
        tableBut.setOnMouseClicked(this::showTableForm);
    }

    private void showAuthorForm(MouseEvent event){

        Stage AuthorStage = new Stage();

        Parent AuthorParent = null;
        try {
            AuthorParent = FXMLLoader.load(getClass().getResource("/AuthorForm.fxml"));
        }catch(IOException e){
            e.printStackTrace();
        }

        Scene AuthorScene = null;
        if(AuthorParent != null)
            AuthorScene = new Scene(AuthorParent, 330, 230);

        AuthorStage.setTitle("Про програму");
        AuthorStage.setScene(AuthorScene);
        AuthorStage.initModality(Modality.APPLICATION_MODAL);


        Button authorBut = (Button) AuthorParent.lookup("#Close");
        authorBut.setOnMouseClicked(ev -> AuthorStage.close());

        AuthorStage.showAndWait();
    }

    private void showTableForm(MouseEvent event){
        Stage TableStage = new Stage();

        Parent TableParent = null;
        try {
            TableParent = FXMLLoader.load(getClass().getResource("/TableForm.fxml"));
        }catch(IOException e){
            e.printStackTrace();
        }

        Scene AuthorScene = null;
        if(TableParent != null)
            AuthorScene = new Scene(TableParent, 600, 415);

        TableStage.setTitle("База даних");
        TableStage.setScene(AuthorScene);
        TableStage.initModality(Modality.APPLICATION_MODAL);

        TableStage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}