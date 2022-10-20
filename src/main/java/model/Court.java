package model;

import model.Objects.*;
import model.interfaces.InterfaceCourt;
import model.interfaces.RacketController;
import java.util.ArrayList;
import java.util.List;

public class Court implements InterfaceCourt {
    // instance parameters
    private final RacketController playerA, playerB;
    private final double width, height; // m

    // instance state
    private Score scoreA;
    private Score scoreB;
    private Racket A;
    private Racket B;
    private Ball ball;

    public Court(RacketController playerA, RacketController playerB, double width, double height) {
        this.playerA = playerA; // Player A = new Player(new Score(300.50), playerA); //constructeur(Score,
                                // RacketController)
        this.playerB = playerB;
        scoreA = new Score(300, 50);
        scoreB = new Score((int) width - 200, 50);
        this.width = width;
        this.height = height;
        A = new Racket(0, 0, playerA, 500.0, 100.0);
        B = new Racket(width, 0, playerB, 500.0, 100.0);
        ball = new Ball(new Vector2(0, 0), 200.0, 10.0);
        reset();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Score getScoreA() {
        return scoreA;
    }

    public Score getScoreB() {
        return scoreB;
    }

    public List<SolidObject> getListObjects() {
        List<SolidObject> list = new ArrayList<SolidObject>();
        list.add(A);
        list.add(B);
        list.add(ball);
        return list;
    }

    /**
     * Updates the positions of the objects
     * Calls updateBall and if a player wins, reset the objects
     * 
     * @param deltaT time passed
     */
    public void update(double deltaT) { // racket.update(List<Racket>) ?
        A.update(deltaT, height, playerA);
        B.update(deltaT, height, playerB);
        if (updateBall(deltaT))
            reset();
    }

    /**
     * Computes the next position of the ball once it hits the racket.
     * It adds an angle bonus of pi/12 or -pi/12 to the direction of the ball
     * if the racket is either GOING_UP(pi/12) of GOING_DOWN(-pi/12)
     * 
     * @param nextBallPosition Future position of the ball
     * @param deltaT           Time passed
     * @param racket           Player that collides with the ball
     */
    private void computeRacketBounce(Vector2 nextPosition, double deltaT, Racket racket) {
        ball.computeRacketBounce(nextPosition, deltaT, racket, racket.getPlayer() == A.getPlayer());
    }

    /**
     * Updates the position and velocity of the ball
     * 
     * @return true if a player lost
     */
    private boolean updateBall(double deltaT) {
        Vector2 nextPosition = ball.update(deltaT, height, width);

        // Check racket
        if (A.hitBall(true, ball.getCoord(), nextPosition, ball.getSize())) {
            computeRacketBounce(nextPosition, deltaT, A);
        } else if (B.hitBall(false, ball.getCoord(), nextPosition, ball.getSize())) {
            computeRacketBounce(nextPosition, deltaT, B);
        }

        // Check if someone wins (if the ball exits the Court)
        else if (nextPosition.getXdir() < -70 && ball.getCoordX() < -50) { // si la balle va sortir à gauche et est déjà
                                                                           // hors jeu
            scoreB.win();
            ; // le joueur A perd : met à jour le score du joueur B
            return true;
        } else if (nextPosition.getXdir() > width + 70 && ball.getCoordX() > width + 50) { // si la balle va sortir à
                                                                                           // droite et est déjà hors
                                                                                           // jeu
            scoreA.win();
            ; // le joueur B perd : met à jour le score du joueur A
            return true;
        }

        // Check other obstacles if needed

        // Updates position to the correct new position
        ball.setCoord(nextPosition);

        return false;
    }

    /**
     * It resets the game to its initial state
     */
    public void reset() {
        A.reset(height);
        B.reset(height);
        ball.reset(width, height);
    }
}