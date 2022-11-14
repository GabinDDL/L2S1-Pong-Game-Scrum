package model;

import gui.interfaces.UpdatableGui;

import model.game_elements.*;
import model.interfaces.InterfaceCourt;

import java.util.ArrayList;
import java.util.List;

public class Court implements InterfaceCourt {

    // instance parameters
    private final double width, height; // m
    private final int pointsLimit;
    private boolean needLead = false; // flag to check if we need a 2 points lead

    // Default racket parameters
    private final double initialRacketHeight = 100.0;
    private final double initialRacketWidth = 10.0;

    // Default ball parameters
    private final double initialBallSpeed = 300.0;
    private final double initialBallRadius = 10.0;

    // instance state
    private final Player playerA, playerB;
    private Ball ball;

    private Sound soundPerdu;
    private Sound soundBallRacket;
    private Sound soundBallMur;

    // Constructor

    public Court(Player playerA, Player playerB, double width, double height, int pointsLimit) {

        this.width = width;
        this.height = height;

        this.pointsLimit = pointsLimit;

        var racketA = new Racket(new Vector2(0, 0), 0, initialRacketWidth, initialRacketHeight);
        playerA.setRacket(racketA);
        this.playerA = playerA;

        var racketB = new Racket(new Vector2(width, 0), 0, initialRacketWidth, initialRacketHeight);
        playerB.setRacket(racketB);
        this.playerB = playerB;

        ball = new Ball(new Vector2(0, 0), initialBallSpeed, initialBallRadius);

        for (PlayerModel p : getPlayersModel())
            p.reset(height);

        serve();

        soundPerdu = new Sound("Sound Perdu.wav"); // à chaque point marqué
        soundBallRacket = new Sound("Bruitage ball racket.wav"); // impact avec une raquette
        soundBallMur = new Sound("Bruitage ball mur.wav"); // impact avec un mur
    }

    public Court(Player playerA, Player playerB, double width, double height) {
        this(playerA, playerB, width, height, 0);
    }

    // Getters

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public List<UpdatableGui> getListObjects() {
        List<UpdatableGui> list = new ArrayList<UpdatableGui>();
        list.add(playerA);
        list.add(playerB);
        list.add(ball);
        return list;
    }

    public PlayerModel[] getPlayersModel() {
        return new PlayerModel[] { playerA.getPlayerModel(), playerB.getPlayerModel() };
    }

    // Methods

    /**
     * check if the ball is outside, increment score and check if there is win or a
     * service
     * 
     * @param deltaT time passed
     */
    public void handleBallOutside(double deltaT) {
        if (ball.isOutside(deltaT, width)) {
            soundPerdu.play();

            if (ball.getCoordX() < 0) {
                playerB.incrementScore();
            } else {
                playerA.incrementScore();
            }

            if (playerA instanceof Bot botA) {
                botA.resetPredict();
            }
            if (playerB instanceof Bot botB) {
                botB.resetPredict();
            }

            if (gameWon()) {
                needLead = false; // Lower Flag
                reset();
            } else {
                serve();
            }
        }
    }

    /**
     * Updates the positions of the objects
     * If a player wins, it resets the objects
     * 
     * @param deltaT time passed
     */

    public void update(double deltaT) {
        if (playerA instanceof Bot botA) {
            botA.update(deltaT, height, width, ball.getBallModel());
        } else {
            playerA.update(deltaT, height);
        }
        if (playerB instanceof Bot botB) {
            botB.update(deltaT, height, width, ball.getBallModel());
        } else {
            playerB.update(deltaT, height);
        }

        switch (ball.update(deltaT, height, getPlayersModel())) {
            case RACKET_HIT:
                soundBallRacket.play();
                break;
            case WALL_HIT:
                soundBallMur.play();
                break;
            case NONE:
                break;
        }

        handleBallOutside(deltaT);
    }

    /**
     * Resets the ball position
     */
    public void serve() {
        ball.reset(width, height);
    }

    /**
     * Resets the Court and its elements
     */
    public void reset() {
        playerA.resetRacket(height);
        playerB.resetRacket(height);

        playerA.resetScore();
        playerB.resetScore();

        ball.reset(width, height);
    }

    /**
     * Checks if the Court has a winner
     * 
     * @return true if there's a winner; otherwise false
     */
    public boolean gameWon() {

        // Infinite game
        if (pointsLimit <= 0)
            return false;

        // Please mind the division's rounding down behavior in Java
        int upperLimit = (int) Math.ceil((double) 10 / (double) 7 * pointsLimit);

        if (!needLead) {

            // If there's a draw before the Match Point
            if (playerA.isDraw(playerB) &&
                    playerA.pointsEqualTo(pointsLimit - 1)) {
                needLead = true; // Raise Flag
                return false;
            }

            // If a Player gets to the first limit of amount of points
            if (playerA.pointsEqualTo(pointsLimit) ||
                    playerB.pointsEqualTo(pointsLimit))
                return true;

        } else {

            // If a Player gets to the last limit of amount of points
            if (playerA.pointsEqualTo(upperLimit) ||
                    playerB.pointsEqualTo(upperLimit)) {
                return true;
            }

            // If a Player gets a 2 points lead
            if (Math.abs(playerA.getPoints() - playerB.getPoints()) >= 2)
                return true;

        }
        return false;
    }

    /**
     * Returns the winner of the game
     * 
     * @return the winner if there is a winner;
     *         null otherwise
     */
    public Player getWinner() {
        if (!gameWon())
            return null;

        return (playerA.getPoints() > playerB.getPoints()) ? playerA : playerB;

    }

}