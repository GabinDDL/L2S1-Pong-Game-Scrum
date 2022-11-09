package model.game_elements;

import model.Vector2;
import model.interfaces.InterfaceBallModel;
import model.interfaces.InterfaceRacketController.State;

/**
 * This class represents the model of the ball of the game.
 */
public class BallModel extends SolidObject implements InterfaceBallModel {
    private Vector2 speedDirection;

    // constructeur
    public BallModel(Vector2 coord, double InitialSpeed, double size) {
        super(coord, size); // speed = 200, size = 10.0 (radius)
        setInitialSpeed(InitialSpeed);
        this.setMajorSpeed(800);
    }

    // méthodes

    /**
     * Produces an acceleration on the ball if it hits the racket while they are
     * moving in the same vertical direction. If they are moving in opposite
     * vertical direction a deceleration is produced instead
     * 
     * @param newDirection the new direction of the ball
     * @param player       the player that owns the racket
     */
    private void accelerationRacketBounce(Vector2 newDirection, PlayerModel player) {
        int correctionValueDecceleration = 12; // permet de diminuer l'acceleration / la deceleration lors du bounce
        int correctionValueAcceleration = 8;

        boolean isUp = player.getState() == State.GOING_UP;

        if ((newDirection.getYdir() < 0 && isUp) || (newDirection.getYdir() > 0 && !isUp)) {
            newDirection.scalarMultiplication(
                    1 + ((Math.abs(player.getSpeed()) / player.getMajorSpeed()) / correctionValueAcceleration));
            if (newDirection.getNorm() > getMajorSpeed()) {
                newDirection.normalise();
                newDirection.scalarMultiplication(getMajorSpeed());
            }
        } else {
            newDirection.scalarMultiplication(
                    1 - ((Math.abs(player.getSpeed()) / player.getMajorSpeed()) / correctionValueDecceleration));
            if (newDirection.getNorm() < getInitialSpeed()) {
                newDirection.normalise();
                newDirection.scalarMultiplication(getInitialSpeed());
            }
        }
    }

    // Overrides

    // From InterfaceBallModel

    @Override
    public void computeRacketBounce(Vector2 nextPosition, double deltaT, PlayerModel player) {
        speedDirection.setDirection(-speedDirection.getXdir(), speedDirection.getYdir());
        Vector2 newDirection = new Vector2(speedDirection);

        switch (player.getState()) {
            case GOING_UP:
                if (player.isPlayerLeft()) {
                    newDirection.addAngle(23 * Math.PI / 12);
                    if (newDirection.getXdir() <= 0) {
                        newDirection = speedDirection;
                    }
                } else {
                    newDirection.addAngle(Math.PI / 12);
                    if (newDirection.getXdir() >= 0) { // si la balle est dans la surface de jeu
                        newDirection = speedDirection;
                    }
                }
                accelerationRacketBounce(newDirection, player);
                break;
            case GOING_DOWN:
                if (player.isPlayerLeft()) {
                    newDirection.addAngle(Math.PI / 12);
                    if (newDirection.getXdir() <= 0) {
                        newDirection = speedDirection;
                    }
                } else {
                    newDirection.addAngle(23 * Math.PI / 12);
                    if (newDirection.getXdir() >= 0) {
                        newDirection = speedDirection;
                    }
                }
                accelerationRacketBounce(newDirection, player);
                break;
            default:
                break;
        }
        speedDirection.setDirection(newDirection);

        nextPosition.updateDistanceVector(speedDirection, deltaT);
    }

    @Override
    public LastAction update(double deltaT, double height, PlayerModel[] players) {
        LastAction lastAction = LastAction.NONE;
        // first, compute possible next position if nothing stands in the way
        Vector2 nextPosition = new Vector2(getCoord());
        nextPosition.updateDistanceVector(speedDirection, deltaT);

        // Check if the ball hits the wall
        if (nextPosition.getYdir() < 0 || nextPosition.getYdir() > height) {

            speedDirection.setDirection(speedDirection.getXdir(), -speedDirection.getYdir());
            nextPosition.updateDistanceVector(speedDirection, deltaT);
            lastAction = LastAction.WALL_HIT;
        }
        // Check players

        for (PlayerModel p : players) {
            if (p.hitBall(getCoord(), nextPosition, getSize())) {
                computeRacketBounce(nextPosition, deltaT, p);
                lastAction = LastAction.RACKET_HIT;
            }

        }

        setCoord(nextPosition);

        return lastAction;
    }

    // From SolidObject

    /**
     * Resets the position and the direction of the ball
     * 
     * @param double width of the court
     * @param double height of the court
     */
    @Override
    public void reset(double width, double height) {
        super.setCoord(new Vector2(width / 2, Math.random() * (2 * height / 3) + height / 6));

        // Generation a random direction vector of norm this.ballAbsoluteSpeed
        double angle = 2 * Math.random() - 1; // Angle entre -1 et 1 rad pour éviter les départs lents
        if (Math.random() > 0.5)
            angle += Math.PI; // Random side selector

        speedDirection = new Vector2(Math.cos(angle), Math.sin(angle));
        speedDirection.scalarMultiplication(getInitialSpeed());
    }

}
