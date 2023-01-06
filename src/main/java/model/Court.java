package model;

import gui.interfaces.UpdatableGui;

import gui.App;

import model.interfaces.InterfaceCourt;

import java.util.ArrayList;
import java.util.List;

import gui.Sound;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.game_elements.Alea;
import model.game_elements.Ball;
import model.game_elements.Bot;
import model.game_elements.Player;
import model.game_elements.PlayerModel;
import model.game_elements.Racket;
import model.interfaces.InterfaceAlea.TypeAlea;

public class Court implements InterfaceCourt {

    private Pane gameRoot;
    private double xMargin;
    private double scale;

    // instance parameters
    private final double width, height; // m

    // Score limit. When reached, the game starts over.
    private final int pointsLimit = App.getScoreLimit();

    // Default racket parameters
    private static final double INITIAL_RACKET_HEIGHT = 100.0;
    private static final double INITIAL_RACKET_WIDTH = 10.0;
    private static final double INITIAL_RACKET_ACCELERATION = 600;
    private static final double INITIAL_RACKET_MAJOR_SPEED = 600;
    private static final double INITIAL_RACKET_DECELERATION = 0.90;

    // Default ball parameters
    private static final double INITIAL_BALL_SPEED = 400.0;
    private static final double INITIAL_BALL_RADIUS = 10.0;
    private static final double INITIAL_MAJOR_SPEED = 850.0;

    // Instance state
    private final Player playerA, playerB;
    private ArrayList<Ball> ballList; // list of all balls of the game
    private Alea aleaGame;

    private Sound soundLosing = new Sound("Sound Perdu.wav"); // everytime a player scores
    private Sound soundBallRacket = new Sound("Bruitage ball racket.wav"); // everytime a ball hits a racket
    private Sound soundBallMur = new Sound("Bruitage ball mur.wav"); // everytime a ball hits a wall

    private boolean isSoundsActive = true;

    // Methods to stop sounds to be produced when court isn't displayed anymore
    public void stopSounds() {
        isSoundsActive = false;
    }

    public boolean canSoundsBePlayed() {
        return isSoundsActive && App.soundsButton;
    }

    // Constructor

    public Court(Player playerA, Player playerB, double width, double height, double scale,
            boolean alea) {
        this.scale = scale;

        this.width = width;
        this.height = height;

        var racketA = new Racket(new Vector2(0, 0), 0, INITIAL_RACKET_WIDTH, INITIAL_RACKET_HEIGHT);
        playerA.setRacket(racketA);
        this.playerA = playerA;

        var racketB = new Racket(new Vector2(width, 0), 0, INITIAL_RACKET_WIDTH, INITIAL_RACKET_HEIGHT);
        playerB.setRacket(racketB);
        this.playerB = playerB;

        ballList = new ArrayList<>();
        Ball ball = new Ball(new Vector2(0, 0), INITIAL_BALL_SPEED, INITIAL_MAJOR_SPEED, INITIAL_BALL_RADIUS);
        ballList.add(ball);

        for (PlayerModel p : getPlayersModel())
            p.reset(height);

        serve(0);

        soundLosing = new Sound("Sound Perdu.wav"); // à chaque point marqué
        soundBallRacket = new Sound("Bruitage ball racket.wav"); // impact avec une raquette
        soundBallMur = new Sound("Bruitage ball mur.wav"); // impact avec un mur

        if (alea) {
            aleaGame = new Alea();
        }
    }

    Court(Court c) {
        this(c.playerA, c.playerB, c.width, c.height, c.scale, c.aleaGame != null);
    }

    // Getters

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getInitialRacketAcceleration() {
        return INITIAL_RACKET_ACCELERATION;
    }

    public double getInitialRacketDeceleration() {
        return INITIAL_RACKET_DECELERATION;
    }

    public double getInitialRacketMajorSpeed() {
        return INITIAL_RACKET_MAJOR_SPEED;
    }

    public double getInitialBallRadius() {
        return INITIAL_BALL_RADIUS;
    }

    public double getInitialBallSpeed() {
        return INITIAL_BALL_SPEED;
    }

    public double getInitialMajorSpeed() {
        return INITIAL_MAJOR_SPEED;
    }

    public double getScale() {
        return scale;
    }

    public double getxMargin() {
        return xMargin;
    }

    /**
     * @return every object of the court (that needs to be updated) in a List : players, balls, etc.
     */
    public List<UpdatableGui> getListObjects() {
        List<UpdatableGui> list = new ArrayList<UpdatableGui>();
        list.add(playerA);
        list.add(playerB);
        for (Ball ball : ballList) {
            list.add(ball);
        }
        return list;
    }

    public PlayerModel[] getPlayersModel() {
        return new PlayerModel[] { playerA.getPlayerModel(), playerB.getPlayerModel() };
    }

    public int getCountBall() {
        return ballList.size();
    }

    // Setters

    public void setGameRoot(Pane gameRoot) {
        this.gameRoot = gameRoot;
    }

    public void setxMargin(double xMargin) {
        this.xMargin = xMargin;
    }

    /**
     * add a ball in ballList and in the gameRoot
     * 
     * @param o
     */
    public void addBall(Ball ball) {
        ballList.add(ball);
        gameRoot.getChildren().add(ball.getCircle());
        serve(ballList.size() - 1);
    }

    /**
     * remove a ball in ballList and in the gameRoot
     * 
     * @param o
     */
    public void removeBall(Ball ball) {
        ballList.remove(ball);
        gameRoot.getChildren().remove(ball.getCircle());
    }

    /**
     * add a text in the gameRoot
     * 
     * @param text
     */
    public void addText(Text text) {
        gameRoot.getChildren().add(text);
    }

    /**
     * remove a text in the gameRoot
     * 
     * @param o
     */
    public void removeText(Text text) {
        gameRoot.getChildren().remove(text);
    }

    // Methods

    /**
     * Checks if the ball is outside, increments score and checks if there is win or a
     * service
     * 
     * @param deltaT time passed
     */
    public void handleBallOutside(double deltaT) {
        // flag to check if the ball is outside and if it's the only ball present in the
        // game
        boolean isOutsideAndAlone = false;

        for (int i = 0; i < ballList.size(); i++) {
            if (ballList.get(i).isOutside(width)) {

                if (canSoundsBePlayed()) soundLosing.play();

                if (ballList.get(i).getCoordX() < 0) {
                    if (aleaGame != null && aleaGame.getTypeAlea() == TypeAlea.doublePoint) {
                        aleaGame.reset(this, playerB);
                    }
                    playerB.incrementScore();

                } else {

                    if (aleaGame != null && aleaGame.getTypeAlea() == TypeAlea.doublePoint) {
                        aleaGame.reset(this, playerA);
                    }
                    playerA.incrementScore();
                }

                if (playerA instanceof Bot) {
                    ((Bot) playerA).resetPredict();
                }
                if (playerB instanceof Bot) {
                    ((Bot) playerB).resetPredict();
                }

                if (gameWon()) {
                    reset();
                } else {
                    if (aleaGame != null && aleaGame.getTypeAlea() == TypeAlea.newBall && aleaGame.isResetAble()) {
                        // Remove ball if there is more than 1 ball in the list and this is an alea
                        aleaGame.reset(this, ballList.get(i));
                        i--;
                    } else {
                        isOutsideAndAlone = true;
                        serve(i);
                    }
                }
            }
        }
        if (isOutsideAndAlone && aleaGame != null) {
            if (aleaGame.isResetAble()) {
                aleaGame.reset(this);
            }
            aleaGame.newAlea(this);
        }
    }

    /**
     * Updates the positions of the objects
     * If a player wins, it resets the objects
     * 
     * @param deltaT time passed
     */

    public void update(double deltaT) {
        if (playerA instanceof Bot) {
            ((Bot) playerA).update(deltaT, height, width, ballList);
        } else {
            playerA.update(deltaT, height);
        }
        if (playerB instanceof Bot) {
            ((Bot) playerB).update(deltaT, height, width, ballList);
        } else {
            playerB.update(deltaT, height);
        }

        // Sounds from impact with balls
        for (Ball ball : ballList) {
            switch (ball.update(deltaT, height, getPlayersModel())) {
                case RACKET_HIT:
                if (canSoundsBePlayed()) soundBallRacket.play();
                    break;
                case WALL_HIT:
                if (canSoundsBePlayed()) soundBallMur.play();
                    break;
                case NONE:
                    break;
            }
        }
        handleBallOutside(deltaT);
    }

    /**
     * Resets the ball position
     */
    public void serve(int i) {
        if (i < ballList.size() && i >= 0)
            ballList.get(i).reset(width, height);
    }

    /**
     * Resets the Court and its elements
     */
    public void reset() {
        playerA.resetRacket(height);
        playerB.resetRacket(height);

        playerA.resetScore();
        playerB.resetScore();

        for (int i = 1; i < ballList.size(); i++) {
            if (aleaGame != null && aleaGame.getTypeAlea() == TypeAlea.newBall && aleaGame.isResetAble()) {
                // Remove ball if there is more than 1 ball in the list and this is an alea
                aleaGame.reset(this, ballList.get(i));
                // Remove all the ball except of the first
                i--;
            }
        }
        for (Ball ball : ballList) {
            ball.reset(width, height);
        }

        if (aleaGame != null && aleaGame.isResetAble()) {
            aleaGame.reset(this);
        }
    }

    /**
     * Checks if the Court has a winner
     * 
     * @return true if there's a winner;
     *         otherwise false
     */
    public boolean gameWon() {

        // Infinite game
        if (pointsLimit <= 0)
            return false;

        // Please mind the division's rounding down behavior in Java
        int upperLimit = App.whichScore ? (int) Math.ceil((double) 10 / (double) 7 * pointsLimit) : App.getScoreLimit();

        // NOTE: We don't use pointsEqualTo because we have a power-up
        // that can make the amounts of points go over the limit

        // If a Player gets to or gets past the last limit of amount of points
        if (playerA.pointsBiggerThan(upperLimit - 1) ||
                playerB.pointsBiggerThan(upperLimit - 1)) {
            return true;
        }

        // If a Player gets a lead of 2 points or more
        if (playerA.pointsBiggerThan(pointsLimit - 1) ||
                playerB.pointsBiggerThan(pointsLimit - 1)) {
            if (Math.abs(playerA.getPoints() - playerB.getPoints()) >= 2)
                return true;
        }
        return false;
    }

    /**
     * Returns the winner of the game
     * 
     * @return the winner if there is one;
     *         null otherwise
     */
    public Player getWinner() {
        if (!gameWon())
            return null;

        return (playerA.getPoints() > playerB.getPoints()) ? playerA : playerB;

    }

}