package model.game_elements;

import model.Vector2;
import model.interfaces.InterfaceBallModel;
import model.interfaces.InterfaceRacketController.State;
import model.interfaces.InterfaceRacketModel.HitType;

/**
 * This class represents the model of the ball of the game.
 */
public class BallModel extends SolidObject implements InterfaceBallModel {
    private Vector2 speedDirection;

    // constructeur
    public BallModel(Vector2 coord, double initialSpeed, double initialMajorSpeed, double size) {
        super(coord, size); // speed = 200, size = 10.0 (radius)
        setInitialSpeed(initialSpeed);
        this.setMajorSpeed(initialMajorSpeed);
        speedDirection = new Vector2(0, 0);
    }

    // Getters

    public double getSpeedDirectionX() {
        return speedDirection.getXdir();
    }

    public double getSpeedDirectionY() {
        return speedDirection.getYdir();
    }

    public double getSpeedAngle() {
        return speedDirection.getAngle();
    }

    // Setter

    public void setSpeedDirection(Vector2 speedDirection) {
        this.speedDirection = speedDirection;
    }

    // méthodes

    /**
     * @return true if the ball is outside
     */
    public boolean isOutside(double width) {
        return getCoordX() < -70 || getCoordX() > width + 70;
    }

    /**
     * Produces an acceleration on the ball if it hits the racket while they are
     * moving in the same vertical direction. If they are moving in opposite
     * vertical direction, a deceleration is produced instead
     * 
     * @param newDirection the new direction of the ball
     * @param player       the player that owns the racket
     */
    private void accelerationRacketBounce(Vector2 newDirection, PlayerModel player) {
        int correctionValueDeceleration = 12; // permet de diminuer l'acceleration / la deceleration lors du bounce
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
                    1 - ((Math.abs(player.getSpeed()) / player.getMajorSpeed()) / correctionValueDeceleration));
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
                    newDirection.addAngleRestricted(-Math.PI / 12);
                    if (newDirection.getXdir() <= 0) {
                        newDirection = speedDirection;
                    }
                } else {
                    newDirection.addAngleRestricted(Math.PI / 12);
                    if (newDirection.getXdir() >= 0) { // if the ball is still on the court
                        newDirection = speedDirection;
                    }
                }
                accelerationRacketBounce(newDirection, player);
                break;
            case GOING_DOWN:
                if (player.isPlayerLeft()) {
                    newDirection.addAngleRestricted(Math.PI / 12);
                    if (newDirection.getXdir() <= 0) {
                        newDirection = speedDirection;
                    }
                } else {
                    newDirection.addAngleRestricted(-Math.PI / 12);
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
        // first, computes possible next position if nothing stands in the way
        Vector2 nextPosition = new Vector2(getCoord());
        nextPosition.updateDistanceVector(speedDirection, deltaT);

        // Checks if the ball hits the wall
        if (nextPosition.getYdir() < 0 || nextPosition.getYdir() > height) {

            speedDirection.setDirection(speedDirection.getXdir(), -speedDirection.getYdir());
            nextPosition.updateDistanceVector(speedDirection, deltaT);
            lastAction = LastAction.WALL_HIT;
        }

        // Checks players
        for (PlayerModel p : players) {
            HitType touche = p.hitBall(getCoord(), nextPosition, getSize(), speedDirection);
            if (touche == HitType.BALL_HIT_HORIZONTAL) {
                speedDirection.setDirection(speedDirection.getXdir(), -speedDirection.getYdir());
                lastAction = LastAction.RACKET_HIT;
            }
            if (touche == HitType.BALL_HIT_VERTICAL) {
                speedDirection.setDirection(-speedDirection.getXdir(), speedDirection.getYdir());
                lastAction = LastAction.RACKET_HIT;
            }
            if (touche == HitType.BALL_HIT_HORIZONTAL_AND_VERTICAL) {
                speedDirection.setDirection(-speedDirection.getXdir(), -speedDirection.getYdir());
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

        // Generates a random direction vector of norm this.ballAbsoluteSpeed
        double angle = 2 * Math.random() - 1; // Angle between -1 and 1 rad to avoid slow starts
        if (Math.random() > 0.5)
            angle += Math.PI; // Random side selector

        speedDirection = new Vector2(Math.cos(angle), Math.sin(angle));
        speedDirection.scalarMultiplication(getInitialSpeed());
    }

}
