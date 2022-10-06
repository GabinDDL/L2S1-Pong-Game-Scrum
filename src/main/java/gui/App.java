package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Court;
import model.Player;
import model.interfaces.RacketController;

public class App extends Application {
    @Override // definit une fonction de la class héréditaire
    public void start(Stage primaryStage) {
        var root = new Pane(); // ecran

        var gameScene = new Scene(root); // scene qui apparait dans l'écran
        
        var playerA = new Player();
        var playerB = new Player();

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
        var gameView = new GameView(court, root, 1.0);
        primaryStage.setScene(gameScene);
        primaryStage.show();
        gameView.animate();
    }
}
