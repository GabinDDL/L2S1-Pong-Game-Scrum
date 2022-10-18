package gui;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import model.Court;
import model.SceneDisplayController;
import model.Objects.*;

public class GameView {
    // class parameters
    private final Court court;
    private final Pane gameRoot; // main node of the game
    private final double scale;
    private SceneDisplayController sceneDisplayModifier;

    private double xMargin = 50.0;
    private double racketThickness = 10.0; // pixels

    // Constructeur
    /**
     * @param court le "modèle" de cette vue (le terrain de jeu de raquettes et tout
     *              ce qu'il y a dessus)
     * @param root  le nœud racine dans la scène JavaFX dans lequel le jeu sera
     *              affiché
     * @param scale le facteur d'échelle entre les distances du modèle et le nombre
     *              de pixels correspondants dans la vue
     */

    public GameView(Court court, Pane root, double scale, SceneDisplayController sceneDisplayModifier) {
        this.court = court;
        this.gameRoot = root;
        this.scale = scale;
        this.sceneDisplayModifier = sceneDisplayModifier;

        root.setMinWidth(court.getWidth() * scale + 2 * xMargin);
        root.setMinHeight(court.getHeight() * scale);

        this.changeImageBackground("terrain.jpg"); // edit wallpaper

        for (SolidObject object : court.getListObjects()) {
            if (object instanceof Racket) {
                object.init(scale, xMargin, racketThickness);
            } else if (object instanceof Ball) {
                object.init(scale, xMargin);
            }
        }

        // initialisation affichage Scores
        court.getScoreA().initDisplay(Color.WHITE, court.getWidth());
        court.getScoreB().initDisplay(Color.WHITE, court.getWidth());

        gameRoot.getChildren().addAll(court.getScoreA().getTextScore(), court.getScoreB().getTextScore());
        for (SolidObject object : court.getListObjects()) {
            if (object instanceof Ball) {
                gameRoot.getChildren().add(((Ball) object).getCircle());
            } else if (object instanceof Racket) {
                gameRoot.getChildren().add(((Racket) object).getRectangle());
            }
        }
    }

    // accesseurs
    public void setRacketThickness(double thickness) {
        racketThickness = thickness;
    }

    public void setMarginX(double margin) {
        xMargin = margin;
    }

    /**
     * Change the background with an Image file
     * 
     * @param imageTitle
     */
    public void changeImageBackground(String imageTitle) {
        gameRoot.setStyle("-fx-background-image: url('file:./Images/" + imageTitle
                + "'); -fx-background-position: center center; -fx-background-repeat:no-repeat; -fx-background-size:100% 100%;");
    }

    public void animate() {
        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {

                if (last == 0) { // ignore the first tick, just compute the first deltaT
                    last = now;
                    return;
                }

                if (sceneDisplayModifier.isInGame()) {
                    court.update((now - last) * 1.0e-9); // convert nanoseconds to seconds
                    // Updates graphical part of the elements
                    for (SolidObject object : court.getListObjects()) {
                        if (object instanceof Racket) {
                            ((Racket) object).updateDisplay(scale);
                        } else if (object instanceof Ball) {
                            ((Ball) object).updateDisplay(scale, xMargin);
                        }
                    }
                    court.getScoreA().updateDisplay();// met à jour affichage de la valeur du score de racketA
                    court.getScoreB().updateDisplay();// met à jour affichage de la valeur du score de racketB
                }
                last = now;
            }
        }.start();
    }
}
