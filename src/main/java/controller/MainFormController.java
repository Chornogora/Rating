package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    @FXML
    private volatile Label status;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkForConnection();
    }

    @FXML
    private void checkForConnection(){
        if (dao.Dao.isConnection()) {
            status.setText("підключено");
            status.setTextFill(Paint.valueOf("#00FF00"));
        } else {
            status.setText("немає з'єднання");
            status.setTextFill(Paint.valueOf("#FF0000"));
        }
    }
}