package gui;

import static constants.Constants.DIR_FXML;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Court;
import model.Player;
import model.SceneDisplayController;
import model.Score;
import model.controllers.ControllerLabel;
import model.interfaces.RacketController;

public class App extends Application {

    @Override // definit une fonction de la class héréditaire
    public void start(Stage primaryStage) throws MalformedURLException, IOException {

        // Load .fxml
        URL url = new File(DIR_FXML + "initialScoreDisplay.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        BorderPane borderPaneRoot = loader.load(); // Ecran

        Scene gameScene = new Scene(borderPaneRoot); // scene qui apparait dans l'écran

        /**
         * Class controling what's being displayed on the screen
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
        // System.out.println(labels.getLabelA());
        Score scoreA = new Score(labels.getLabelA());
        Player playerA = new Player(null, scoreA);

        Score scoreB = new Score(labels.getLabelB());
        Player playerB = new Player(null, scoreB);

        var sceneDisplayModifier = new SceneDisplayModifier();

        // We bind the pressing of the keys to the mouvement of the rackets
        gameScene.setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case SHIFT:
                    playerA.setState(RacketController.State.GOING_UP);
                    break;
                case CONTROL:
                    playerA.setState(RacketController.State.GOING_DOWN);
                    break;
                case UP:
                    playerB.setState(RacketController.State.GOING_UP);
                    break;
                case DOWN:
                    playerB.setState(RacketController.State.GOING_DOWN);
                    break;
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
                    if (playerA.getState() == RacketController.State.GOING_UP)
                        playerA.setState(RacketController.State.IDLE);
                    break;
                case CONTROL:
                    if (playerA.getState() == RacketController.State.GOING_DOWN)
                        playerA.setState(RacketController.State.IDLE);
                    break;
                case UP:
                    if (playerB.getState() == RacketController.State.GOING_UP)
                        playerB.setState(RacketController.State.IDLE);
                    break;
                case DOWN:
                    if (playerB.getState() == RacketController.State.GOING_DOWN)
                        playerB.setState(RacketController.State.IDLE);
                    break;
                default:
                    break;
            }
        });

        var court = new Court(playerA, playerB, 1000, 600);
        var gameView = new GameView(court, borderPaneRoot, 1.0, sceneDisplayModifier);

        primaryStage.setTitle("Pong World");
        primaryStage.setScene(gameScene);
        primaryStage.show();

        gameView.animate();
    }

}
