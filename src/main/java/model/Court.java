package model;

import gui.interfaces.UpdatableGui;

import model.game_elements.*;
import model.interfaces.InterfaceCourt;

import java.util.ArrayList;
import java.util.List;

public class Court implements InterfaceCourt {

    // instance parameters
    private final double width, height; // m

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

    public Court(Player playerA, Player playerB, double width, double height) {

        this.width = width;
        this.height = height;

        var racketA = new Racket(new Vector2(0, 0), 0, initialRacketWidth, initialRacketHeight);
        playerA.setRacket(racketA);
        this.playerA = playerA;

        var racketB = new Racket(new Vector2(width, 0), 0, initialRacketWidth, initialRacketHeight);
        playerB.setRacket(racketB);
        this.playerB = playerB;

        ball = new Ball(new Vector2(0, 0), initialBallSpeed, initialBallRadius);

        for (PlayerModel p : getPlayersModel())
            p.reset(height);

        ball.reset(width, height);

        soundPerdu = new Sound("Sound Perdu.wav"); // à chaque point marqué
        soundBallRacket = new Sound("Bruitage ball racket.wav"); // impact avec une raquette
        soundBallMur = new Sound("Bruitage ball mur.wav"); // impact avec un mur
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
        return new PlayerModel[] { playerA, playerB };
    }

    // Methods

    /**
     * Updates the positions of the objects
     * If a player wins, it resets the objects
     * 
     * @param deltaT time passed
     */

    public void update(double deltaT) {
        playerA.update(deltaT, height);
        playerB.update(deltaT, height);

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

        if (isBallOutside(deltaT))
            reset();

    }

    /**
     * @return true if a player lost
     */
    private boolean isBallOutside(double deltaT) {

        // Check if someone wins (if the ball exits the Court)

        // si la balle va sortir à gauche
        if (ball.getCoordX() < -70) {
            // le joueur A perd : met à jour le score du joueur B
            playerB.incrementScore();
            soundPerdu.play();
            return true;
        }
        // si la balle va sortir à droite
        else if (ball.getCoordX() > width + 70) {
            // le joueur B perd : met à jour le score du joueur A
            playerA.incrementScore();
            soundPerdu.play();
            return true;
        }
        return false;
    }

    /**
     * It resets the ball position
     */
    public void reset() {
        ball.reset(width, height);
    }
}