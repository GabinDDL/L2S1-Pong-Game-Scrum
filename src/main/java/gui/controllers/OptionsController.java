package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gui.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import model.MediaHandler;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;

import javafx.animation.Interpolator;
import javafx.event.ActionEvent;

/**
 * Controller of options page
 */
public class OptionsController implements Initializable {

    @FXML
    private ImageView homeBack;
    @FXML
    private BorderPane optionsContainer;

    // The four next attributes represents FXML elements displayed on the screen
    @FXML
    private ImageView col1Row1;
    private boolean c1r1State = App.soundsButton;

    @FXML
    private ImageView col1Row2;
    private boolean c1r2State = App.aleas;

    @FXML
    private ImageView col1Row3;
    private boolean c1r3State = App.whichScore;

    @FXML
    private ImageView col1Row4;
    private boolean c1r4State = App.infiniteGame;

    // A Spinner is an InputTextZone, in this case limitated to Integer for the score
    @FXML
    private Spinner<Integer> scoreLimit;
    private int minScore = 3;
    private int maxScore = 20;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

        // Defines scoreLimit and his event handler
        scoreLimit.setValueFactory(new IntegerSpinnerValueFactory(0, maxScore, App.getScoreLimit(), 1));
        scoreLimit.addEventHandler(ActionEvent.ACTION, e -> {
            updateScore();
        });
        scoreLimit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            updateScore();
        });

        // Change image of each switch in order of their previous state
        // If code start for the first time, initiated to false (GS standing for GreenSwitch, and RS for RedSwitch)
        changeSwitchState(col1Row1, c1r1State? "128x61_GS.png" : "128x61_RS.png");
        changeSwitchState(col1Row2, c1r2State? "128x61_GS.png" : "128x61_RS.png");
        changeSwitchState(col1Row3, c1r3State? "128x61_GS.png" : "128x61_RS.png");
        changeSwitchState(col1Row4, c1r4State? "128x61_GS.png" : "128x61_RS.png");
    }

    /**
     * Verify the integrity of the value inserted in the Spinner which represents maximum score.
     */
    private void updateScore() {
        int value = scoreLimit.getValue();
        
        // We make sure that Score can only be between 3 and 20, or equals to 0
        if (value < minScore) {

            if (App.getScoreLimit() == 0 && value != 0) {
                App.setScoreLimit(3);
                scoreLimit.getValueFactory().setValue(3);
                App.infiniteGame = false;
            } else {

                // If Score equals 0, than that is an infinite party
                App.setScoreLimit(0);
                scoreLimit.getValueFactory().setValue(0);
                App.infiniteGame = true;
            }

        } else {
            App.infiniteGame = false;

            if (value >= maxScore) {
                App.setScoreLimit(20);
            } else {
                App.setScoreLimit(value);
            }
        }

        updateSwitchs();
    }

    /**
     * Update switchs in order of their value in {@link App} 
     */
    private void updateSwitchs() {
        c1r1State = App.soundsButton;
        c1r2State = App.aleas;
        c1r3State = App.whichScore;
        c1r4State = App.infiniteGame;

        changeSwitchState(col1Row1, c1r1State? "128x61_GS.png" : "128x61_RS.png");
        changeSwitchState(col1Row2, c1r2State? "128x61_GS.png" : "128x61_RS.png");
        changeSwitchState(col1Row3, c1r3State? "128x61_GS.png" : "128x61_RS.png");
        changeSwitchState(col1Row4, c1r4State? "128x61_GS.png" : "128x61_RS.png");
    }
    
    /**
     * Launches transition and loads Home Page when mouse clicked
     * @param event
     * @throws IOException
     */
    @FXML
    public void loadHomePage(MouseEvent event) throws IOException {
        TransitionClass.transition(true, false, "home.fxml", Interpolator.EASE_IN, optionsContainer, null);
    }

    /**
     * Change state of first switch
     */
    @FXML
    public void changeStateC1R1() {
        App.soundsButton = !App.soundsButton;
        updateSwitchs();
        changeSwitchState(col1Row1, c1r1State? "128x61_GS.png" : "128x61_RS.png");
    }

    /**
     * Change state of second switch
     */
    @FXML
    public void changeStateC1R2() {
        App.aleas = !App.aleas;
        updateSwitchs();
        changeSwitchState(col1Row2, c1r2State? "128x61_GS.png" : "128x61_RS.png");
    }

    /**
     * Change state of third switch
     */
    @FXML
    public void changeStateC1R3() {
        App.whichScore = !App.whichScore;
        updateSwitchs();
        changeSwitchState(col1Row3, c1r3State? "128x61_GS.png" : "128x61_RS.png");
    }

    /**
     * Change state of fourth switch
     */
    @FXML
    public void changeStateC1R4() {
        App.infiniteGame = !App.infiniteGame;
        updateSwitchs();
        scoreLimit.getValueFactory().setValue(c1r4State ? 0 : 3);
        changeSwitchState(col1Row4, c1r4State? "128x61_GS.png" : "128x61_RS.png");
    }

    /**
     * Set ImageView's image to new Image File 
     * @param image
     * @param pathNewImage
     */
    @FXML
    public void changeSwitchState(ImageView image, String pathNewImage) {

            image.setImage(new Image(MediaHandler.getImageInputStream(pathNewImage)));
    }
}
