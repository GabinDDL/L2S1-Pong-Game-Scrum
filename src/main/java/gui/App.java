package gui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Court;
import model.RacketController;

public class App extends Application {
    @Override // definit une fonction de la class héréditaire
    public void start(Stage primaryStage) {
        var root = new Pane(); // ecran

        var gameScene = new Scene(root); // scene qui apparait dans l'écran
        // Implementation of the RacketController interface on ../model/RacketController.java
        class Player implements RacketController {
            // Default state of the palyer's racket is IDLE
            State state = State.IDLE;

            // We define the State getter for the player.
            @Override
            public State getState() {
                return state;
            }
        }

        var playerA = new Player();
        var playerB = new Player();

        // We bind the pressing of the keys to the mouvement of the rackets
        gameScene.setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case SHIFT:
                    playerA.state = RacketController.State.GOING_UP;
                    break;
                case CONTROL:
                    playerA.state = RacketController.State.GOING_DOWN;
                    break;
                case UP:
                    playerB.state = RacketController.State.GOING_UP;
                    break;
                case DOWN:
                    playerB.state = RacketController.State.GOING_DOWN;
                    break;
            }
        });

        // We bind the release of the keys to the IDLE state
        gameScene.setOnKeyReleased(ev -> {
            // touche existante dans le jeu
            switch (ev.getCode()) {
                case SHIFT:
                    if (playerA.state == RacketController.State.GOING_UP) playerA.state = RacketController.State.IDLE;
                    break;
                case CONTROL:
                    if (playerA.state == RacketController.State.GOING_DOWN) playerA.state = RacketController.State.IDLE;
                    break;
                case UP:
                    if (playerB.state == RacketController.State.GOING_UP) playerB.state = RacketController.State.IDLE;
                    break;
                case DOWN:
                    if (playerB.state == RacketController.State.GOING_DOWN) playerB.state = RacketController.State.IDLE;
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
