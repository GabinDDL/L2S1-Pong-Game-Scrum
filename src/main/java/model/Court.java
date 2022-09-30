package model;

public class Court {
    // instance parameters
    private final RacketController playerA, playerB;
    private final double width, height; // m
    private final double racketSpeed = 500.0; // m/s
    private final double racketSize = 100.0; // m
    private final double ballRadius = 10.0; // m
    // instance state
    private double racketA; // m
    private double racketB; // m
    private double ballAbsoluteSpeed; // m
    private Vector2 ballPosition; // m
    private Vector2 ballSpeedDirection; // m
    private int countBounce=0;

    private Score scores;

    public Court(RacketController playerA, RacketController playerB, double width, double height) {
        this.playerA = playerA;
        this.playerB = playerB;
        scores = new Score(); //initialisation des scores
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

    public Score getScores() {
        return scores;
    }

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

    private void computeRacketBouce(Vector2 nextBallPosition, double deltaT, RacketController player) {
        /*
         * Computes the next position of the ball once it hits the racket.
         * It adds an angle bonus of pi/6 or -pi/6 to the direction of the ball
         * if the racket is either GOING_UP(pi/6) of GOING_DOWN(-pi/6)
         */

        ballSpeedDirection.setDirection(-ballSpeedDirection.getXdir(), ballSpeedDirection.getYdir());
        Vector2 newDirection = new Vector2(ballSpeedDirection);


        switch (player.getState()) {

            case GOING_UP:
                if (player == playerA){
                    newDirection.addAngle(-0.26);
                    if (newDirection.getXdir() <= 0) newDirection = ballSpeedDirection;
                }
                else{
                    newDirection.addAngle(0.26);
                    if (newDirection.getXdir() >= 0) newDirection = ballSpeedDirection;
                }
                break;

            case GOING_DOWN:
                if (player == playerA){
                    newDirection.addAngle(0.26);
                    if (newDirection.getXdir() <= 0) newDirection = ballSpeedDirection;
                }
                else{
                    newDirection.addAngle(-0.26);
                    if (newDirection.getXdir() >= 0) newDirection = ballSpeedDirection;
                }
                    break;
            default:
                break;
        }

        ballSpeedDirection.coppyVector(newDirection);
        nextBallPosition.updateVector(ballSpeedDirection, deltaT);

    }

    /**
     * @return true if a player lost
     */
    private boolean updateBall(double deltaT) {
        // first, compute possible next position if nothing stands in the way
        Vector2 nextBallPosition = new Vector2(ballPosition);
        nextBallPosition.updateVector(ballSpeedDirection, deltaT);
        // next, see if the ball would meet some obstacle
        // Check height
        if (nextBallPosition.getYdir() < 0 || nextBallPosition.getYdir() > height) {
            ballSpeedDirection.setDirection(ballSpeedDirection.getXdir(), -ballSpeedDirection.getYdir());
            nextBallPosition.updateVector(ballSpeedDirection, deltaT);
            
            countBounce++; // augmente à chaque fois que la balle touche un mur
            if(countBounce < 15 && countBounce % 1 == 0) { // majoration + augmentation à modulo
                ballSpeedDirection.scalarMultiplication(1.10);
            }
        }
        // Check racket
        if (nextBallPosition.getXdir() < 0 && nextBallPosition.getXdir() > -10 &&
                nextBallPosition.getYdir() > racketA && nextBallPosition.getYdir() < racketA + racketSize) {
            computeRacketBouce(nextBallPosition, deltaT, playerA);
        } else if (nextBallPosition.getXdir() > width && nextBallPosition.getXdir() < width + 10 &&
                nextBallPosition.getYdir() > racketB && nextBallPosition.getYdir() < racketB + racketSize) {
            computeRacketBouce(nextBallPosition, deltaT, playerB);
        } else if (nextBallPosition.getXdir()  < -50) { // si la balle sort à gauche
            scores.bGagne();; // le joueur A perd : met à jour le score du joueur B
            return true;
        } else if (nextBallPosition.getXdir()  > width +50) { // si la balle sort à droite
            scores.aGagne();; // le joueur B perd : met à jour le score du joueur A 
            return true;
        }

        // Update position to the correct new position
        ballPosition.coppyVector(nextBallPosition);

        return false;
    }

    public double getBallRadius() {
        return ballRadius;
    }

    void reset() {
        this.racketA = height / 2;
        this.racketB = height / 2;
        this.ballAbsoluteSpeed = 200; // norm of the speed vector
        this.ballPosition = new Vector2(width / 2, Math.random() * (2 * height / 3) + height / 6);

        this.countBounce = 0; // bounce of the ball

        // Generation a random direction vector of norm this.ballAbsoluteSpeed

        double angle = 2 * Math.random() - 1; // Angle between -1 and 1 rad to void slow starts
        if (Math.random() > 0.5) angle += 3.14; // Random side selector

        this.ballSpeedDirection = new Vector2(Math.cos(angle), Math.sin(angle));
        this.ballSpeedDirection.scalarMultiplication(ballAbsoluteSpeed);

    }
}
