package gui;

import static constants.Constants.*;

import java.net.MalformedURLException;

import javafx.animation.AnimationTimer;

import javafx.scene.layout.BorderPane;
import model.Court;
import model.SceneDisplayController;
import model.Objects.Ball;
import model.Objects.Racket;
import model.Objects.SolidObject;

public class GameView {
    // class parameters
    private final Court court;
    private final BorderPane gameRoot; // main node of the game
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
     * @throws MalformedURLException l'url du chemin vers le fichier est corrompu
     */

    public GameView(Court court, BorderPane root, double scale, SceneDisplayController sceneDisplayModifier)
            throws MalformedURLException {
        this.court = court;
        this.gameRoot = root;
        this.scale = scale;
        this.sceneDisplayModifier = sceneDisplayModifier;

        root.setMinWidth(court.getWidth() * scale + 2 * xMargin);
        root.setMinHeight(court.getHeight() * scale);

        // this.changeImageBackground("terrain.jpg"); // edit wallpaper

        for (SolidObject object : court.getListObjects()) {
            if (object instanceof Racket) {
                object.init(scale, xMargin, racketThickness);
            } else if (object instanceof Ball) {
                object.init(scale, xMargin);
            }
        }

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
        gameRoot.setStyle("-fx-background-image: url('file:" + DIR_IMAGES + imageTitle +
                "'); -fx-background-position: center center; -fx-background-repeat:no-repeat; -fx-background-size:100% 100%;");
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
                }
                last = now;
            }
        }.start();
    }
}
