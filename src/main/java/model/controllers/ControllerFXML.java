package model.controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import model.MediaHandler;

/* 
 * Class representing 
 * the JavaFX Controllers contained in the init.fxml file
 * this is not a MVC Controller
 */
public class ControllerFXML implements Initializable {

    @FXML
    private Label labelA;

    @FXML
    private Label labelB;

    @FXML
    private Pane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // applyFont(labelA);
        // applyFont(labelB);
        // applyBackgroundImage(rootPane, "terrain.png");
    }

    /**
     * Method to apply font style using Java
     * 
     * @param label on which we'll apply the styles
     */
    private static void applyFont(Label label) {
        InputStream fontStream = MediaHandler.getFontInputStream("Brandish.ttf");
        Font font = Font.loadFont(fontStream, 96);
        label.setFont(font);
        label.setTextFill(Color.WHITE);
    }

    /**
     * 
     * Method to apply background image using Java
     * 
     * @param pane
     * @param file
     */
    private static void applyBackgroundImage(Pane pane, String file) {
        InputStream in = MediaHandler.getImageInputStream(file);
        Image img = new Image(in);
        BackgroundImage bgImage = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background bg = new Background(bgImage);
        pane.setBackground(bg);

    }

    public Label getLabelA() {
        return labelA;
    }

    public Label getLabelB() {
        return labelB;
    }

    public Pane getRootPane() {
        return rootPane;
    }

}
