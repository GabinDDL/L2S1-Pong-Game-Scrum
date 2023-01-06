package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gui.App;
import gui.GameView;
import gui.game_elements.Score;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.util.Duration;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;

import model.Court;
import model.MediaHandler;
import model.game_elements.Player;
import model.interfaces.InterfaceRacketController.State;

/** 
 * Class which allows to play the main gameplay
*/
public class ClassicPongController implements Initializable {
    
    // Elements created and described in classicPongMenu.fxml
    @FXML
    BorderPane classicPongMenuContainer;
    @FXML
    ImageView leftArrow;
    @FXML
    ImageView rightArrow;
    @FXML
    Text title;
    @FXML
    AnchorPane informations;

    private Pane gameplay;
    private Court court;
    private GameView gameView;

    private static SceneDisplayController sceneDisplayModifier = new SceneDisplayModifier();

    private BorderPane buttons;
    private Button quitButton;
    private Button restartButton;
    private boolean inReset = false;

    /**
     * Class controling what's, and more importantly when, being displayed on the screen
     */
    private static class SceneDisplayModifier implements SceneDisplayController {

        // Scene that is being displayed
        SceneDisplay actualView = SceneDisplay.GAME;

        @Override
        public boolean isInGame() {
            return actualView == SceneDisplay.GAME;
        }

        @Override
        public boolean isOnPause() {
            return actualView == SceneDisplay.PAUSE;
        }

        @Override
        public void setScene(SceneDisplay sD) {
            actualView = sD;
        }

        /**
         * Altern between Game and Pause in a gameplay
         */
        @Override
        public void pauseUnpause() {
            switch (actualView) {
                case GAME:
                    setScene(SceneDisplay.PAUSE);
                    break;
                case PAUSE:
                    setScene(SceneDisplay.GAME);
                    break;
                default:
                    break;
            }
        }
    }

    public void displayPauseButtons(boolean display) {
        buttons.setVisible(display);
        buttons.setDisable(!display);
    }
    
    @Override
	public void initialize(URL url, ResourceBundle rb) {

        quitButton = new Button("MENU");

        restartButton = new Button("RESTART") {
            @Override
            protected double computeMaxWidth(double height)
            {
                return this.prefWidth(height);
            }
        };

        restartButton.setPrefSize(150, 50);
        restartButton.setFont(new Font("Brandish", 20.0));

        quitButton.setPrefSize(150, 50);
        quitButton.setFont(new Font("Brandish", 20.0));


        buttons = new BorderPane(null, null, restartButton, null, quitButton);
        buttons.setPadding(new Insets(300, 300, 0, 300));
        buttons.setMinWidth(1100.0);

        restartButton.addEventHandler(ActionEvent.ACTION, (ActionEvent e) -> {
            
            
            court.reset();
            gameView.updateDisplays();
            
            displayPauseButtons(false);

            launchTimer(gameplay, court, gameView, 3, new EndFunction() {
                public void end() {
                    sceneDisplayModifier.pauseUnpause();
                }
            });

        });

        quitButton.addEventHandler(ActionEvent.ACTION, (ActionEvent e) -> {
            try {
                loadMenu(null);
            } catch (IOException error) {
                error.printStackTrace();
            }
        });
    }

    /**
     *  Loads the animation translating to the HomePage's Pane (above)
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    public void loadHomePage(MouseEvent event) throws IOException {
        
        TransitionClass.transition(false, false, "home.fxml", Interpolator.EASE_IN, classicPongMenuContainer, null);
    }

    public void loadMenu(KeyEvent event) throws IOException {
        
        gameView.stopMusicAndSounds();

        TransitionClass.transition(false, false, "classicPongMenu.fxml", Interpolator.EASE_IN, (Parent) gameplay, new EndFunction() {
            public void end() {
                sceneDisplayModifier.pauseUnpause();
                inReset = true;
            }
        });
    }

    /**
     *  Loads the animation translating to the BotPage's Pane (above)
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    public void loadBotPage(MouseEvent event) throws IOException {
        
        TransitionClass.transition(true, true, "botPongMenu.fxml", Interpolator.EASE_IN, classicPongMenuContainer, null);
    }


    /**
     * Launches the gameplay. Starts with an animation.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    public void playGame(MouseEvent event) throws IOException {
        
        // Refers to gameScene initialized in App.java
        Scene gameScene = classicPongMenuContainer.getScene();

        // Getting .fxml file's URL to load the Score display style.
        URL fxmlUrl = MediaHandler.getFXMLURL("init.fxml");
        FXMLLoader loader =  new FXMLLoader(fxmlUrl);
        gameplay = loader.load();

        // Adding StyleClass, useful in `background.css`
        gameplay.getStyleClass().add("root");

        gameplay.getChildren().addAll(buttons);
        buttons.setDisable(true);
        buttons.setVisible(false);
        
        // Associate Labels to Players
        // Initialize player
        ControllerFXML labels = loader.getController();

        Score scoreA = new Score(labels.getLabelA());
        Player playerA = new Player();
        playerA.setScore(scoreA);

        Score scoreB = new Score(labels.getLabelB());
        Player playerB = new Player();
        playerB.setScore(scoreB);

        gameScene.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            // System.out.println("X" + e.getX());
            // System.out.println("Y" + e.getY());
        });

        // We bind the pressing of the keys to the movement of the rackets and the desire to put the gameplay in pause
        gameScene.setOnKeyPressed((KeyEvent ev) -> {
            switch (ev.getCode()) {
                case SHIFT:
                    playerA.setState(State.GOING_UP);
                    break;
                case CONTROL:
                    playerA.setState(State.GOING_DOWN);
                    break;
                case UP:
                    playerB.setState(State.GOING_UP);
                    break;
                case DOWN:
                    playerB.setState(State.GOING_DOWN);
                    break;
                case ESCAPE:
                    if (!inReset) {
                        sceneDisplayModifier.pauseUnpause();
                        
                        if (sceneDisplayModifier.isInGame()) {
                            displayPauseButtons(false);
                        } else {
                            displayPauseButtons(true);
                        }
                    }
                    break;
                case Q:
                    if (sceneDisplayModifier.isOnPause()) {
                        try {
                            loadMenu(ev);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        });

        // We bind the release of the keys to the IDLE state
        gameScene.setOnKeyReleased(ev -> {
            switch (ev.getCode()) {
                case SHIFT:
                    if (playerA.getState() == State.GOING_UP)
                        playerA.setState(State.IDLE);
                    break;
                case CONTROL:
                    if (playerA.getState() == State.GOING_DOWN)
                        playerA.setState(State.IDLE);
                    break;
                case UP:
                    if (playerB.getState() == State.GOING_UP)
                        playerB.setState(State.IDLE);
                    break;
                case DOWN:
                    if (playerB.getState() == State.GOING_DOWN)
                        playerB.setState(State.IDLE);
                    break;
                default:
                    break;
            }
        });

        court = new Court(playerA, playerB, 1000, 600, 1.0, App.aleas);
        court.setGameRoot(gameplay);
    
        this.gameView = new GameView(court, gameplay, 1.0, sceneDisplayModifier);
        court.setxMargin(gameView.getxMargin());
        
        // Starts the transition to the gameplay, starts a decount of 3 at the end of the animation.
        TransitionClass.classicGameTransition(false, true, Interpolator.EASE_IN, classicPongMenuContainer, gameplay, new EndFunction() {
            public void end() {
                launchTimer(gameplay, court, gameView, 3, new EndFunction() {
                    public void end() {
                        gameView.animate();
                    }
                });
            }
        });

        court.update(0);
    }

    /**
     * Add a counter of 'duration' in the center of court
     * When finished, if the counter has reached 0, the gameplay is launched, else the function is once again called with duration - 1
     */
    private void launchTimer(Pane gameplay, Court court, GameView gameView, int duration, EndFunction endfunc) {

        inReset = true;

        // Creates a new timer, sets it visible, sets its color
        Text timer = new Text("" + duration);
        timer.setFill(Color.GOLDENROD);

        // Defines a new StackPane containing the timer just created, sets its height, width and alignment
        StackPane compteur = new StackPane(timer);

        compteur.setLayoutX((court.getWidth() * gameView.getScale()) / 2 + gameView.getMarginX());
        compteur.setLayoutY(court.getHeight() / 2);

        // Adds the counter to the pane
        gameplay.getChildren().addAll(compteur);

        // Creates a new ScaleTransition that will increase the scale size of the timer's text
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), timer);
        
        st.setByX(10f);
        st.setByY(10f);
        timer.setFont(Font.font(timer.getFont().getFamily(), 15));
        
        // At the end of the animation, if the timer > 1, the timer is launched again with duration minus one
        // Else the gameplay is animated
        st.setOnFinished(e -> {
            gameplay.getChildren().remove(compteur);
            inReset = false;

            if (duration > 1) {
                launchTimer(gameplay, court, gameView, duration-1, endfunc);
            } else {
                endfunc.end();
            }
        });
        
        // Launches the animation
        st.play();
    }
}


