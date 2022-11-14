package model.controllers;

import java.net.URL;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ControllerLabel implements Initializable {

    @FXML
    private Label labelA;

    @FXML
    private Label labelB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Label getLabelA() {
        return labelA;
    }

    public Label getLabelB() {
        return labelB;
    }

}
