package model.game_elements;

import java.util.ArrayList;

import model.interfaces.InterfaceHasDifficulty.Difficulty;
import model.interfaces.InterfaceRacketController.State;

public class BotModel extends PlayerModel {

    private double predictedBallPosition = 0; // to keep the ball position
    private boolean hasPredictedPosition = false;
    private BallModel ballPredicted = null;

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
     * Getting the closest ball to the Bot is needed to handle the case where
     * multiple balls are present on the Court.
     * 
     * @param isPlayerLeft
     * @param listBall
     * @return the closest ball to the racket
     */
    public BallModel getClosestBall(boolean isPlayerLeft, ArrayList<Ball> listBall) {
        if (listBall.isEmpty())
            return null;

        BallModel acc = null;
        for (Ball ball : listBall) {
            if (isPlayerLeft && ball.getSpeedDirectionX() < 0 ||
                    !isPlayerLeft && ball.getSpeedDirectionX() > 0) {
                if (acc == null) {
                    acc = ball.getBallModel();
                } else {
                    if (isPlayerLeft) {
                        acc = ball.getCoordX() < acc.getCoordX() ? ball.getBallModel() : acc;
                    } else {
                        acc = ball.getCoordX() > acc.getCoordX() ? ball.getBallModel() : acc;
                    }
                }
            }
        }
        return acc;
    }

    /**
     * Update the state of the bot in order to follow the ball
     * 
     * @param height
     * @param ballModel
     * @param isPlayerLeft
     */
    public void updateStateFollow(double height, ArrayList<Ball> listBall, boolean isPlayerLeft) {
        ballPredicted = getClosestBall(isPlayerLeft, listBall);

        if (ballPredicted != null) {
            // Check if the ball is going to the player's side

            if (ballPredicted.getCoordY() > getRacket().getCoordY()
                    + (getRacket().getRacketHeight() / 2))
                this.setState(State.GOING_DOWN);

            else if (ballPredicted.getCoordY() < getRacket().getCoordY()
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
     * @return true if the bot needs a position
     */
    public boolean needPredictPosition() {
        if (!hasPredictedPosition) {
            return ballPredicted != null;
        }
        return false;
    }

    /**
     * Updates the next predictedBallPosition if the bot needs it
     * 
     * @param height
     * @param ballPredicted
     * @param isPlayerLeft
     */
    public void updatePredictedBallPosition(double height, boolean isPlayerLeft) {
        if (!needPredictPosition())
            return;

        // coords of the ball
        double x = ballPredicted.getCoordX();
        double y = ballPredicted.getCoordY();
        // if the ball is going up or not
        boolean isUp = ballPredicted.getSpeedDirectionY() < 0;

        while (!hasPredictedPosition) {
            double distanceY = 0; // distance y between the ball and the racket / wall
            double distanceX = Math.abs(x - getRacket().getCoordX()); // distance x between the ball and the racket

            // time that the ball takes to go to the racket without limits
            double time = distanceX / Math.abs(ballPredicted.getSpeedDirectionX());

            distanceY = Math.abs(ballPredicted.getSpeedDirectionY()) * time;
            if (isUp) {
                if (y - distanceY < 0) {
                    // if y is out of the limits of the screen

                    // time that the ball takes to go to the racket with limits
                    double timeTemp = Math.abs(y) / Math.abs(ballPredicted.getSpeedDirectionY());

                    // new distance x between the wall and the ball
                    double distanceTemp = Math.abs(ballPredicted.getSpeedDirectionX()) * timeTemp;

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
                    double timeTemp = Math.abs(y - height) / Math.abs(ballPredicted.getSpeedDirectionY());

                    // new distance x between the wall and the ball
                    double distanceTemp = Math.abs(ballPredicted.getSpeedDirectionX()) * timeTemp;

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
     * Updates the state of the ball with the next position of the ball
     * 
     * @param height
     * @param listBall
     * @param isPlayerLeft
     */
    public void updateStatePredict(double height, ArrayList<Ball> listBall, boolean isPlayerLeft) {
        BallModel ballModel = getClosestBall(isPlayerLeft, listBall);

        if (ballModel != ballPredicted) {
            resetPredict();
            ballPredicted = ballModel;
            return;
        }

        updatePredictedBallPosition(height, isPlayerLeft);

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
     * Sets the new State with the difficulty
     * <ul>
     * <li>EASY : Follows the ball</li>
     * <li>NORMAL : Follows the ball then predicts the position when the midfield is
     * reached</li>
     * <li>HARD : Predicts the position of the ball</li>
     * </ul>
     * 
     * @param height
     * @param width
     * @param ballList
     * @param isPlayerLeft
     * @param difficulty
     */
    public void setState(double height, double width, ArrayList<Ball> ballList, boolean isPlayerLeft,
            Difficulty difficulty) {
        switch (difficulty) {

            case EASY:
                updateStateFollow(height, ballList, isPlayerLeft);
                break;

            case NORMAL:
                BallModel acc = getClosestBall(isPlayerLeft, ballList);

                if (acc == null || (!isPlayerLeft && acc.getCoordX() <= width / 2)
                        || (isPlayerLeft && acc.getCoordX() >= width / 2)) {
                    if (hasPredictedPosition)
                        hasPredictedPosition = false;
                    updateStateFollow(height, ballList, isPlayerLeft);

                } else {
                    updateStatePredict(height, ballList, isPlayerLeft);
                }

                break;

            case HARD:
                updateStatePredict(height, ballList, isPlayerLeft);
                break;
            default:
                return;

        }

    }

    public void update(double deltaT, double height, double width, ArrayList<Ball> ballList, Difficulty difficulty) {
        setState(height, width, ballList, isPlayerLeft(), difficulty);
        update(deltaT, height, getState());
    }

    @Override
    public void reset(double height) {
        getRacket().reset(height);
        hasPredictedPosition = false;
    }
}