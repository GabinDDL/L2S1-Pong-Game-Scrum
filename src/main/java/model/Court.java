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
    private Racket A;
    private Racket B;
    private Ball ball;
    private Sound soundPerdu;

    public Court(RacketController playerA, RacketController playerB, double width, double height) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.width = width;
        this.height = height;
        A = new Racket(0, 0, playerA, 500.0, 100.0);
        B = new Racket(width, 0, playerB, 500.0, 100.0);
        ball = new Ball(new Vector2(0, 0), 200.0, 10.0);
        reset();
        soundPerdu = new Sound("Sound Perdu.wav"); // son défaite
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Player getPlayerA() {
        return (Player) playerA;
    }

    public Player getPlayerB() {
        return (Player) playerB;
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
        updateBall(deltaT);
        if (isBallOutside())
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
     */
    private void updateBall(double deltaT) {
        Vector2 nextPosition = ball.update(deltaT, height, width);

        // Check racket
        if (A.hitBall(true, ball.getCoord(), nextPosition, ball.getSize())) {
            computeRacketBounce(nextPosition, deltaT, A);
        } else if (B.hitBall(false, ball.getCoord(), nextPosition, ball.getSize())) {
            computeRacketBounce(nextPosition, deltaT, B);
        }

        // Check other obstacles if needed

        // Updates position to the correct new position
        ball.setCoord(nextPosition);
    }

    /**
     * @return true if a player lost
     */
    private boolean isBallOutside() {

        // Check if someone wins (if the ball exits the Court)
        if (ball.getCoordX() < -70) { // si la balle va sortir à gauche e
            ((Player) playerB).getScore().incrementScore();
            // le joueur A perd : met à jour le score du joueur B
            soundPerdu.play();
            return true;
        } else if (ball.getCoordX() > width + 70) { // si la balle va sortir à droite
            // le joueur B perd : met à jour le score du joueur A
            ((Player) playerA).getScore().incrementScore();
            soundPerdu.play();
            return true;

        }

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