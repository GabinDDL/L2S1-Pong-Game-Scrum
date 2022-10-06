package model;

import model.interfaces.InterfaceCourt;
import model.interfaces.RacketController;

public class Court implements InterfaceCourt {
    // instance parameters
    private final RacketController playerA, playerB;
    private final double width, height; // m
    private final double racketSpeed = 500.0; // m/s
    private final double racketSize = 100.0; // m
    private final double ballRadius = 10.0; // m
    // instance state
    private double racketA; // m
    private double racketB; // m
    private Score scoreA;
    private Score scoreB;
    private double ballAbsoluteSpeed; // m
    private Vector2 ballPosition; // m
    private Vector2 ballSpeedDirection; // m
    private int countBounce=0;

    public Court(RacketController playerA, RacketController playerB, double width, double height) {
        this.playerA = playerA;
        this.playerB = playerB;
        scoreA = new Score(300,50); //initialisation score racketA
        scoreB = new Score((int)width-200,50); //initialisation score racketB
        this.width = width;
        this.height = height;
        reset();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getRacketSize() {
        return racketSize;
    }

    public double getRacketA() {
        return racketA;
    }

    public double getRacketB() {
        return racketB;
    }

    public double getBallX() {
        return ballPosition.getXdir();
    }

    public double getBallY() {
        return ballPosition.getYdir();
    }

    public Score getScoreA() {
        return scoreA;
    }

    public Score getScoreB() {
        return scoreB;
    }

    /**
     * Updates the positions of the rackets
     * and calls the ball update
     * 
     * @param deltaT time passed
     */
    public void update(double deltaT) {

        switch (playerA.getState()) {
            case GOING_UP:
                racketA -= racketSpeed * deltaT;
                if (racketA < 0.0)
                    racketA = 0.0;
                break;
            case IDLE:
                break;
            case GOING_DOWN:
                racketA += racketSpeed * deltaT;
                if (racketA + racketSize > height)
                    racketA = height - racketSize;
                break;
        }
        switch (playerB.getState()) {
            case GOING_UP:
                racketB -= racketSpeed * deltaT;
                if (racketB < 0.0)
                    racketB = 0.0;
                break;
            case IDLE:
                break;
            case GOING_DOWN:
                racketB += racketSpeed * deltaT;
                if (racketB + racketSize > height)
                    racketB = height - racketSize;
                break;
        }
        if (updateBall(deltaT))
            reset();
    }

    /**
     * Computes the next position of the ball once it hits the racket.
     * It adds an angle bonus of pi/12 or -pi/12 to the direction of the ball
     * if the racket is either GOING_UP(pi/12) of GOING_DOWN(-pi/12)
     * 
     * @param nextBallPosition Future position of the ball
     * @param deltaT Time passed
     * @param racket Player that collides with the ball
     */
    private void computeRacketBouce(Vector2 nextBallPosition, double deltaT, RacketController racket) {

        ballSpeedDirection.setDirection(-ballSpeedDirection.getXdir(), ballSpeedDirection.getYdir());
        Vector2 newDirection = new Vector2(ballSpeedDirection);


        switch (racket.getState()) {
            case GOING_UP:
                if (racket == playerA){
                    newDirection.addAngle(23* Math.PI/12);
                    if (newDirection.getXdir() <= 0) newDirection = ballSpeedDirection;
                }
                else{
                    newDirection.addAngle(Math.PI/12);
                    if (newDirection.getXdir() >= 0) newDirection = ballSpeedDirection;
                }
                break;

            case GOING_DOWN:
                if (racket == playerA){
                    newDirection.addAngle(Math.PI/12);
                    if (newDirection.getXdir() <= 0) newDirection = ballSpeedDirection;
                }
                else{
                    newDirection.addAngle(23* Math.PI/12);
                    if (newDirection.getXdir() >= 0) newDirection = ballSpeedDirection;
                }
                    break;
            default:
                break;
        }

        ballSpeedDirection.setDirection(newDirection);
        nextBallPosition.updateDistanceVector(ballSpeedDirection, deltaT);

    }

    /**
     * Updates the position and velocity of the ball
     * 
     * @return true if a player lost
     */
    private boolean updateBall(double deltaT) {
        // first, compute possible next position if nothing stands in the way
        Vector2 nextBallPosition = new Vector2(ballPosition);
        nextBallPosition.updateDistanceVector(ballSpeedDirection, deltaT);
        
        // next, see if the ball would meet some obstacle
        // Check height
        if (nextBallPosition.getYdir() < 0 || nextBallPosition.getYdir() > height) {
            ballSpeedDirection.setDirection(ballSpeedDirection.getXdir(), -ballSpeedDirection.getYdir());
            nextBallPosition.updateDistanceVector(ballSpeedDirection, deltaT);
            
            countBounce++; // augmente à chaque fois que la balle touche un mur
            if(countBounce < 49 && countBounce % 3 == 0) { // majoration + augmentation à modulo
                ballSpeedDirection.scalarMultiplication(1.10);
            }
        }
        // Check racket
        if ((nextBallPosition.getXdir() < 0 && nextBallPosition.getXdir() >= -10 &&
                nextBallPosition.getYdir() > racketA -5 && nextBallPosition.getYdir() < racketA + racketSize + 5) ||
                (ballPosition.getXdir() > 0 && nextBallPosition.getXdir() < 0 &&
                ballPosition.getYdir() < racketA - 5 && nextBallPosition.getYdir() > racketA + racketSize + 5)) {
            computeRacketBouce(nextBallPosition, deltaT, playerA);

        } else if ((nextBallPosition.getXdir() > width && nextBallPosition.getXdir() <= width + 10 &&
                nextBallPosition.getYdir() > racketB - 5 && nextBallPosition.getYdir() < racketB + racketSize + 5) ||
                (ballPosition.getXdir() < width && nextBallPosition.getXdir() > width &&
                ballPosition.getYdir() < racketB - 5 && nextBallPosition.getYdir() > racketB + racketSize + 5)) {
            computeRacketBouce(nextBallPosition, deltaT, playerB);

        } else if (nextBallPosition.getXdir()  < -70 && ballPosition.getXdir() < -50 ) { // si la balle sort à gauche
            scoreB.win();; // le joueur A perd : met à jour le score du joueur B
            return true;
        } else if (nextBallPosition.getXdir()  > width + 70 && ballPosition.getXdir() > width + 50) { // si la balle sort à droite
            scoreA.win();; // le joueur B perd : met à jour le score du joueur A 
            return true;
        }

        // Update position to the correct new position
        ballPosition.setDirection(nextBallPosition);

        return false;
    }

    public double getBallRadius() {
        return ballRadius;
    }
   
    /**
     * It resets the game to its initial state
     */
    public void reset() {
        this.racketA = height / 2;
        this.racketB = height / 2;
        this.ballAbsoluteSpeed = 200; // norm of the speed vector
        this.ballPosition = new Vector2(width / 2,  Math.random() * (2 * height / 3) + height / 6);

        this.countBounce = 0; // bounce of the ball

        // Generation a random direction vector of norm this.ballAbsoluteSpeed

        double angle = 2 * Math.random() - 1; // Angle between -1 and 1 rad to void slow starts
        if (Math.random() > 0.5) angle += Math.PI; // Random side selector

        this.ballSpeedDirection = new Vector2( Math.cos(angle), Math.sin(angle));
        this.ballSpeedDirection.scalarMultiplication(ballAbsoluteSpeed);

    }
}
