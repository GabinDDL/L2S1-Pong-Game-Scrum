package gui;

import static constants.Constants.DIR_FXML;

import gui.game_elements.Score;

import model.Court;
import model.controllers.ControllerLabel;
import model.game_elements.Bot;
import model.interfaces.InterfaceHasDifficulty.Difficulty;
import model.game_elements.Player;
import model.interfaces.InterfaceRacketController.State;

import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override // definit une fonction de la class héréditaire
    public void start(Stage primaryStage) throws MalformedURLException, IOException {

        // Load .fxml
        URL url = new File(DIR_FXML + "initialScoreDisplay.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        BorderPane borderPaneRoot = loader.load(); // Ecran

        Scene gameScene = new Scene(borderPaneRoot); // scene qui apparaît dans l'écran

        /**
         * Class controlling what's being displayed on the screen
         */
        class SceneDisplayModifier implements SceneDisplayController {

            // Scene that beings displaying
            SceneDisplay actualView = SceneDisplay.GAME;

            @Override
            public boolean isInGame() {
                return actualView == SceneDisplay.GAME;
            }

            public void setScene(SceneDisplay sD) {
                actualView = sD;
            }

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

        // Associate Labels to Players
        // Init player

        ControllerLabel labels = loader.getController();

        Score scoreA = new Score(labels.getLabelA());
        Player playerA = new Player();
        playerA.setScore(scoreA);

        Score scoreB = new Score(labels.getLabelB());
        // Player playerB = new Player();
        Player playerB = new Bot(Difficulty.NORMAL);
        // Replace NORMAL by EASY or HARD as you want and the first playerB in
        // comments
        playerB.setScore(scoreB);

        var sceneDisplayModifier = new SceneDisplayModifier();

        // We bind the pressing of the keys to the mouvement of the rackets
        gameScene.setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case SHIFT:
                    playerA.setState(State.GOING_UP);
                    break;
                case CONTROL:
                    playerA.setState(State.GOING_DOWN);
                    break;
                /*
                 * case UP:
                 * playerB.setState(State.GOING_UP);
                 * break;
                 * case DOWN:
                 * playerB.setState(State.GOING_DOWN);
                 * break;
                 */
                case ESCAPE:
                    sceneDisplayModifier.pauseUnpause();
                    break;
                default:
                    break;
            }
        });

        // We bind the release of the keys to the IDLE state
        gameScene.setOnKeyReleased(ev -> {
            // touche existante dans le jeu
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

        int pointsLimit = 7;

        var court = new Court(playerA, playerB, 1000, 600, pointsLimit, 1.0, true);

        var gameView = new GameView(court, borderPaneRoot, 1.0, sceneDisplayModifier);
        court.setGameRoot(gameView.getGameRoot());
        court.setxMargin(gameView.getxMargin());

        primaryStage.setTitle("Pong World");
        primaryStage.setScene(gameScene);
        primaryStage.show();

        gameView.animate();
    }

}
