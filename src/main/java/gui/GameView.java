package gui;

import java.io.InputStream;
import java.net.MalformedURLException;

import gui.controllers.SceneDisplayController;
import gui.interfaces.UpdatableGui;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Pane;

import model.Court;
import model.MediaHandler;
import model.game_elements.Ball;
import model.game_elements.Player;

public class GameView {

    // class parameters
    private final Court court;
    private final Pane gameRoot; // main node of the game
    private final double scale;
    private SceneDisplayController sceneDisplayModifier;

    private double xMargin = 50.0;
    private double racketThickness = 10.0; // pixels

    private Sound sound = new Sound("GameMusicLoop.wav");

    // Constructor
    /**
     * @param court "model" of this view (the court of the game and everything on
     *              it)
     * @param game  the root node in the JavaFX scene in which the game will be
     *              displayed
     * @param scale the scale between values in the model and the pixels
     *              corresponding in the view
     * @throws MalformedURLException the url of the path towards the file is
     *                               corrupted
     */

    public void launchMusic() {
        sound.loop(); // play sound background
    }

    public void stopMusicAndSounds() {
        sound.stop(); // stop sound background
        court.stopSounds();
    }

    public GameView(Court court, Pane game, double scale, SceneDisplayController sceneDisplayModifier)
            throws MalformedURLException {
        this.court = court;
        this.gameRoot = game;
        this.scale = scale;
        this.sceneDisplayModifier = sceneDisplayModifier;

        game.setMinWidth(court.getWidth() * scale + 2 * xMargin);
        game.setMinHeight(court.getHeight() * scale);

        // If player did not desactivate music in HomePage or in options
        if (App.musicState) {
            launchMusic();
        }

        for (UpdatableGui object : court.getListObjects()) {
            if (object instanceof Player)
                ((Player) object).initDisplayRacket(scale, xMargin, racketThickness);
            else if (object instanceof Ball)
                ((Ball) object).initDisplay(scale, xMargin);

        }

        for (Object object : court.getListObjects()) {
            if (object instanceof Ball)
                gameRoot.getChildren().add(((Ball) object).getCircle());
            else if (object instanceof Player)
                gameRoot.getChildren().add(((Player) object).getShape());

        }
    }
    // Getters

    public Pane getGameRoot() {
        return gameRoot;
    }

    public double getxMargin() {
        return xMargin;
    }

    // Setters
    public void setRacketThickness(double thickness) {
        racketThickness = thickness;
    }

    public void setMarginX(double margin) {
        xMargin = margin;
    }

    // Getters
    public double getMarginX() {
        return xMargin;
    }

    public double getScale() {
        return scale;
    }

    // Methods

    /**
     * Change the background with an Image file
     * 
     * @param imageTitle
     */
    public void changeImageBackground(String imageTitle) {
        // gameRoot.setStyle("-fx-background-image: url('file:" + DIR_IMAGES +
        // imageTitle +
        // "'); -fx-background-position: center center; -fx-background-repeat:no-repeat;
        // -fx-background-size:100% 100%;");
        InputStream in = MediaHandler.getImageInputStream(imageTitle);
        Image img = new Image(in);
        BackgroundImage bgImage = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                null, null);
        Background bg = new Background(bgImage);
        gameRoot.setBackground(bg);
    }

    /**
     * Updates graphical part of the elements on court
     */
    public void updateDisplays() {
        for (UpdatableGui object : court.getListObjects()) {
            if (object instanceof Player)
                ((Player) object).updateDisplay(scale);
            else if (object instanceof Ball)
                ((Ball) object).updateDisplay(scale, xMargin, ((Ball) object).getSize());
        }
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

                // If the game is not set on pause, then displays elements that need to be
                // displayed and stops calculating next positions
                if (sceneDisplayModifier.isInGame()) {
                    court.update((now - last) * 1.0e-9); // convert nanoseconds to seconds
                    updateDisplays();
                }
                last = now;
            }
        }.start();
    }
}
