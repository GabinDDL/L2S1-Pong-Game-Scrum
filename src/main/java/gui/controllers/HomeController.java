package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gui.App;

import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.MediaHandler;

public class HomeController implements Initializable {
	
    @FXML
    private Button playButton;

    @FXML
    private ImageView optionsScene;

    @FXML
    private ImageView projectPage;

    @FXML
    private BorderPane homeContainer;

    @FXML
    private ImageView music;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
        changeMusicImage();
    }

    /**
     * Launches transition and load Options Page when mouse clicked
     * @param event
     * @throws IOException
     */
    @FXML
    public void loadOptions(MouseEvent event) throws IOException {
        TransitionClass.transition(true, true, "options.fxml", Interpolator.EASE_IN, homeContainer, null);
    }

    /**
     * Launches transition to classic (and first) Pong game menu when mouse clicked
     * @param event
     * @throws IOException
     */
    @FXML
    public void loadClassicPongMenuSpace(ActionEvent event) throws IOException {
        TransitionClass.transition(false, true,"classicPongMenu.fxml", Interpolator.EASE_IN, homeContainer, null);
    }

    private void switchMusicValue() {
        App.musicState = !App.musicState;
    }

    private void changeMusicImage() {
        music.setImage(new Image(MediaHandler.getImageInputStream(App.musicState ? "music.png" : "nomusic.png")));
    }

    @FXML
    public void changeStateSound(){
        switchMusicValue();

        changeMusicImage();
    }

    @FXML
    public void openProjectPage() {
        new App().openProjectPage();
    }
}

