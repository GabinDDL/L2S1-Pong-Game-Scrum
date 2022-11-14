package model.game_elements;

import model.interfaces.InterfaceHasDifficulty.Difficulty;
import model.interfaces.InterfaceRacketController.State;

public class BotModel extends PlayerModel {

    private double predictedBallPosition = 0; // to keep the ball position
    private boolean hasPredictedPosition = false;

    public BotModel(int points) {
        super(points);
    }

    public BotModel() {
        super();
    }

    public BotModel(RacketModel racket, int points) {
        super(racket, points);
    }

    /**
     * Update the state of the bot in order to follow the ball
     * 
     * @param height
     * @param ballModel
     * @param isPlayerLeft
     */
    public void updateStateFollow(double height, BallModel ballModel, boolean isPlayerLeft) {
        if ((isPlayerLeft && ballModel.getSpeedDirectionX() < 0) ||
                (!isPlayerLeft && ballModel.getSpeedDirectionX() > 0)) {
            // Check if the ball is going to the player's side

            if (ballModel.getCoordY() > getRacket().getCoordY()
                    + (getRacket().getRacketHeight() / 2))
                this.setState(State.GOING_DOWN);

            else if (ballModel.getCoordY() < getRacket().getCoordY()
                    + (getRacket().getRacketHeight() / 2.))
                this.setState(State.GOING_UP);

            else if (this.getState() != State.IDLE) {
                this.setState(State.IDLE);
            }

        } else {
            // the bot goes to the middle of the screen

            if (getRacket().getCoordY() > height / 2.) {
                this.setState(State.GOING_UP);
            } else if (getRacket().getCoordY() < height / 2.) {
                this.setState(State.GOING_DOWN);
            } else if (this.getState() != State.IDLE)
                this.setState(State.IDLE);
        }
    }

    public void resetPredict() {
        setState(State.IDLE);
        hasPredictedPosition = false;
    }

    /**
     * @param ballModel
     * @param isPlayerLeft
     * @return true if the bot needs a position
     */
    public boolean needPredictPosition(BallModel ballModel, boolean isPlayerLeft) {
        if (!hasPredictedPosition) {
            if ((isPlayerLeft && ballModel.getSpeedDirectionX() < 0) ||
                    (!isPlayerLeft && ballModel.getSpeedDirectionX() > 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Update the next predictedBallPosition if the bot needs it
     * 
     * @param height
     * @param ballModel
     * @param isPlayerLeft
     */
    public void updatePredictedBallPosition(double height, BallModel ballModel, boolean isPlayerLeft) {
        if (!needPredictPosition(ballModel, isPlayerLeft))
            return;

        // coords of the ball
        double x = ballModel.getCoordX();
        double y = ballModel.getCoordY();
        // if the ball is going up or not
        boolean isUp = ballModel.getSpeedDirectionY() < 0;

        while (!hasPredictedPosition) {
            double distanceY = 0; // distance y between the ball and the racket / wall
            double distanceX = Math.abs(x - getRacket().getCoordX()); // distance x between the ball and the racket

            // time that the ball takes to go to the racket without limits
            double time = distanceX / Math.abs(ballModel.getSpeedDirectionX());

            distanceY = Math.abs(ballModel.getSpeedDirectionY()) * time;
            if (isUp) {
                if (y - distanceY < 0) {
                    // if y is out of the limits of the screen

                    // time that the ball takes to go to the racket with limits
                    double timeTemp = Math.abs(y) / Math.abs(ballModel.getSpeedDirectionY());

                    // new distance x between the wall and the ball
                    double distanceTemp = Math.abs(ballModel.getSpeedDirectionX()) * timeTemp;

                    y = 0;
                    if (isPlayerLeft) {
                        x = x - distanceTemp;
                    } else {
                        x = x + distanceTemp;
                    }
                    isUp = false;

                } else {
                    predictedBallPosition = y - distanceY;
                    hasPredictedPosition = true;
                }
            } else {

                if (y + distanceY > height) {
                    // time that the ball takes to go to the racket with limit
                    double timeTemp = Math.abs(y - height) / Math.abs(ballModel.getSpeedDirectionY());

                    // new distance x between the wall and the ball
                    double distanceTemp = Math.abs(ballModel.getSpeedDirectionX()) * timeTemp;

                    y = height;
                    if (isPlayerLeft) {
                        x = x - distanceTemp;
                    } else {
                        x = x + distanceTemp;
                    }
                    isUp = true;
                } else {
                    predictedBallPosition = y + distanceY;
                    hasPredictedPosition = true;
                }
            }
        }
    }

    /**
     * Update the state of the ball with the next position of the ball
     * 
     * @param ballModel
     * @param isPlayerLeft
     */
    public void updateStatePredict(double height, BallModel ballModel, boolean isPlayerLeft) {
        if (hasPredictedPosition) {
            if ((isPlayerLeft && ballModel.getSpeedDirectionX() > 0)
                    || (!isPlayerLeft && ballModel.getSpeedDirectionX() < 0)) {
                resetPredict();
                return;
            }
        }

        updatePredictedBallPosition(height, ballModel, isPlayerLeft);

        if (hasPredictedPosition) {
            if (getRacket().getCoordY() + getRacket().getRacketHeight() / 2 < predictedBallPosition) {
                setState(State.GOING_DOWN);
            } else if (getRacket().getCoordY() + getRacket().getRacketHeight() / 2 > predictedBallPosition) {
                setState(State.GOING_UP);
            } else {
                setState(State.IDLE);
            }
        }
    }

    /**
     * Set the new State with the difficulty
     * <ul>
     * <li>EASY : Follow the ball</li>
     * <li>NORMAL : Follow the ball then predict the position when the midfield is
     * reached</li>
     * <li>HARD : Predict the position of the ball</li>
     * </ul>
     * 
     * @param height
     * @param width
     * @param ballModel
     * @param isPlayerLeft
     * @param difficulty
     */
    public void setState(double height, double width, BallModel ballModel, boolean isPlayerLeft,
            Difficulty difficulty) {
        switch (difficulty) {

            case EASY:
                updateStateFollow(height, ballModel, isPlayerLeft);
                break;

            case NORMAL:
                if (isPlayerLeft) {
                    if (ballModel.getCoordX() < width / 2 && ballModel.getSpeedDirectionX() < 0) {
                        updateStatePredict(height, ballModel, isPlayerLeft);
                    } else {
                        if (hasPredictedPosition)
                            hasPredictedPosition = false;
                        updateStateFollow(height, ballModel, isPlayerLeft);
                    }
                }
                if (!isPlayerLeft) {
                    if (ballModel.getCoordX() >= width / 2 && ballModel.getSpeedDirectionX() > 0) {
                        updateStatePredict(height, ballModel, isPlayerLeft);
                    } else {
                        if (hasPredictedPosition)
                            hasPredictedPosition = false;
                        updateStateFollow(height, ballModel, isPlayerLeft);
                    }
                }
                break;

            case HARD:
                updateStatePredict(height, ballModel, isPlayerLeft);
                break;
            default:
                return;

        }
    }

    public void update(double deltaT, double height, double width, BallModel ballModel, Difficulty difficulty) {
        setState(height, width, ballModel, isPlayerLeft(), difficulty);
        update(deltaT, height, getState());
    }

    @Override
    public void reset(double height) {
        getRacket().reset(height);
        hasPredictedPosition = false;
    }
}