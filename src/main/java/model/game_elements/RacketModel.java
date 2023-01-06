package model.game_elements;

import model.IntersectionCalculator2D;
import model.Vector2;
import model.interfaces.InterfaceRacketModel;
import model.interfaces.InterfaceRacketController.State;

/**
 * This class represents the model of the racket. It doesn't include the GUI of
 * the racket.
 */
public class RacketModel extends SolidObject implements InterfaceRacketModel {

    private double racketWidth;
    private State state = State.IDLE;
    private boolean ballJustTouchLeftRacket;
    private boolean ballJustTouchRightRacket;

    // Constructor
    public RacketModel(Vector2 coord, double speed, double size, double racketWidth) {
        super(coord, speed, size);
        this.racketWidth = racketWidth;
        this.setDeceleration(0.90);
        this.setInitialSpeed(50);
        ballJustTouchLeftRacket = false;
        ballJustTouchRightRacket = false;
    }

    public State getState() {
        return state;
    }

    public double getRacketWidth() {
        return racketWidth;
    }

    public double getRacketHeight() {
        return this.getSize();
    }

    // Setters

    public void setState(State state) {
        this.state = state;
    }

    // Methods

    /**
     * Returns the position of the racket taking into account the screen limits.
     * 
     * @param height       height of the screen
     * @param nextPosition next position of the racket
     * @return the position of the racket taking into account the screen limits
     */
    private double reactionWithLimits(double height, double nextPosition) {
        if (nextPosition < 0) {
            setSpeed(0);
            return 0;
        }
        if (nextPosition + getSize() > height) {
            setSpeed(0);
            return height - getSize();
        }
        return nextPosition;
    }

    /**
     * Returns {@code true} if the racket is the left racket.
     * 
     * @return {@code true} if the racket is the left racket
     */
    public boolean isRacketLeft() {
        return getCoordX() == 0;
    }

    // Overrides

    // From SolidObject

    /**
     * Updates the position of the racket according to the state of the racket. It
     * includes the acceleration, deceleration and the reaction with the limits of
     * the screen.
     * 
     * @param deltaT time between two updates
     * @param height height of the screen
     * @param state  state of the racket
     * @see model.interfaces.InterfaceRacketController.State
     */
    @Override
    public void update(double deltaT, double height, State state) {
        switch (state) {
            case GOING_UP:
                if (getSpeed() > 0) // opposite direction
                    setSpeed(getSpeed() * getDeceleration()); // deceleration
                if (Math.abs(getSpeed() - deltaT * getAcceleration()) <= getMajorSpeed()) // acceleration
                    setSpeed(getSpeed() - deltaT * getAcceleration());
                setCoordY(reactionWithLimits(height, getCoordY() + getSpeed() * deltaT));
                break;
            case IDLE:
                if ((Math.abs(getSpeed()) < getInitialSpeed() || getCoordY() == 0 || getCoordY() == height + getSize())
                        && getSpeed() != 0)
                    setSpeed(0);
                else { // slide
                    setSpeed(getSpeed() * getDeceleration());
                    setCoordY(reactionWithLimits(height, getCoordY() + getSpeed() * deltaT));
                }
                break;
            case GOING_DOWN:
                if (getSpeed() < 0) // opposite direction
                    setSpeed(getSpeed() * getDeceleration()); // deceleration
                if (Math.abs(getSpeed() + deltaT * getAcceleration()) <= getMajorSpeed()) // acceleration
                    setSpeed(getSpeed() + deltaT * getAcceleration());
                setCoordY(reactionWithLimits(height, getCoordY() + getSpeed() * deltaT));
                break;
        }
    }

    /**
     * Resets the racket to the initial position (the top of the racket is at the
     * middle of the height of the Court).
     * 
     * @param height height of the screen
     */
    @Override
    public void reset(double height) {
        setCoordY((height - getRacketHeight()) / 2);
        setSpeed(0);
    }

    /**
     * Method that checks if the ball will pass through a horizontal side of the
     * racket during its journey between its current position and the next position.
     * 
     * @param intersection                     intersection between the ball's
     *                                         trajectory and a line passing through
     *                                         a horizontal side of the racket
     * @param distanceIntersection             the distance between the ball and the
     *                                         intersection
     * @param distanceBallPositionNextPosition the distance between the ball and the
     *                                         next position
     * @return {@code true} if the ball's trajectory will pass through the racket,
     *         {@code false} otherwise
     */
    private boolean ballCrossRacketX(Vector2 intersection, double distanceIntersection,
            double distanceBallPositionNextPosition) {
        return distanceIntersection <= distanceBallPositionNextPosition
                // the ball is closer to the intersection than its next position
                && ((isRacketLeft() && !ballJustTouchLeftRacket)
                        || (!isRacketLeft() && !ballJustTouchRightRacket))
                // the ball has not touched the racket on the same side recently
                && intersection.getXdir() <= this.getCoordX() + this.getRacketWidth()
                && intersection.getXdir() >= this.getCoordX();
        // the intersection is within the boundaries of the racket
    }

    /**
     * Method that checks if the ball will pass through a vertical side of the
     * racket during its journey between its current position and the next position.
     * 
     * @param intersection                     intersection between the ball's
     *                                         trajectory and a line passing through
     *                                         a vertical side of the racket
     * @param distanceIntersection             the distance between the ball and the
     *                                         intersection
     * @param distanceBallPositionNextPosition the distance between the ball and the
     *                                         next position
     * @return {@code true} if the ball's trajectory will pass through the racket,
     *         {@code false} otherwise
     */
    private boolean ballCrossRacketY(Vector2 intersection, double distanceIntersection,
            double distanceBallPositionNextPosition) {
        return distanceIntersection <= distanceBallPositionNextPosition
                // the ball is closer to the intersection than its next position
                && ((isRacketLeft() && !ballJustTouchLeftRacket)
                        || (!isRacketLeft() && !ballJustTouchRightRacket))
                // the ball has not touched the racket on the same side recently
                && intersection.getYdir() <= this.getCoordY() + this.getRacketHeight()
                && intersection.getYdir() >= this.getCoordY();
        // the intersection is within the boundaries of the racket
    }

    /**
     * Sets the {@code ballJustTouchLeftRacket} attribute to {@code true} and
     * updates the {@code nextPosition} of the ball.
     * 
     * <p>
     * Useful to avoid bugs like the ball hitting the racket twice. Moreover, when
     * the ball is going to go through the racket, it is possible that it is not
     * yet in contact with it. Consequently, we move it in such a way that it is
     * visually seen to touch the racket.
     * 
     * @param nextPosition                 the next position of the ball
     * 
     * @param intersectionCenterBallRacket the intersection between the ball's
     *                                     trajectory and the racket
     */
    private void ballTouchRacket(Vector2 nextPosition, Vector2 intersectionCenterBallRacket) {
        if (isRacketLeft()) {
            ballJustTouchLeftRacket = true;
        } else {
            ballJustTouchRightRacket = true;
        }
        // update the ball's movement to the intersection between its trajectory and the
        // racket
        nextPosition.setDirection(intersectionCenterBallRacket);
    }

    /**
     * This function checks if the ball no longer touches the racket. This avoids
     * issues like the ball touching twice the racket.
     * 
     * @param distanceBallPositionIntersectionX1 distance between the ball and the x
     *                                           of the
     *                                           intersection between the ball's
     *                                           trajectory and the racket.
     * 
     * @param distanceBallPositionIntersectionX2 distance between the ball and the x
     *                                           of the
     *                                           intersection between the ball's
     *                                           trajectory and the racket.
     * @param distanceBallPositionIntersectionY1 distance between the ball and the y
     *                                           of the
     *                                           intersection between the ball's
     *                                           trajectory and the racket.
     * @param distanceBallPositionIntersectionY2 distance between the ball and the y
     *                                           of the
     *                                           intersection between the ball's
     *                                           trajectory and the racket.
     * @param distanceBallPositionNextPosition   distance between the ball and its
     *                                           next position.
     * 
     * @param ballPosition                       position of the ball.
     * @param nextPosition                       next position of the ball.
     * @param ballRadius                         radius of the ball.
     * @return {@code true} if the ball is no longer in contact with the ball,
     *         {@code false} otherwise
     */
    private boolean isNoLongerInContactWithBall(double distanceBallPositionIntersectionX1,
            double distanceBallPositionIntersectionX2, double distanceBallPositionIntersectionY1,
            double distanceBallPositionIntersectionY2, double distanceBallPositionNextPosition, Vector2 ballPosition,
            Vector2 nextPosition, double ballRadius) {

        return distanceBallPositionIntersectionX1 > distanceBallPositionNextPosition + 10 * ballRadius
                && distanceBallPositionIntersectionX2 > distanceBallPositionNextPosition + 10 * ballRadius
                && distanceBallPositionIntersectionY1 > distanceBallPositionNextPosition + 10 * ballRadius
                && distanceBallPositionIntersectionY2 > distanceBallPositionNextPosition + 10 * ballRadius

                // Make sure that the ball is far enough away from each intersection. For this,
                // we multiply the ballRadius by 10 just to have a confidence interval and be
                // sure that the ball is not in contact with the racket
                && (ballPosition.getXdir() < this.getCoordX()
                        || ballPosition.getXdir() > this.getCoordX() + this.getRacketWidth()
                        || ballPosition.getYdir() < this.getCoordY()
                        || ballPosition.getYdir() > this.getCoordY() + this.getRacketHeight())
                // The ball is no longer inside the racket
                && (nextPosition.getXdir() < this.getCoordX()
                        || nextPosition.getXdir() > this.getCoordX() + this.getRacketWidth()
                        || nextPosition.getYdir() < this.getCoordY()
                        || nextPosition.getYdir() > this.getCoordY() + this.getRacketHeight()
                // The ball is not inside the racket in the next position
                );
    }

    // From InterfaceRacketModel

    /**
     * Determines whether the ball has hit this racket and returns a {@code HitType}
     * representing the way it has touched it.
     *
     * @param ballPosition       the current position of the ball
     * @param nextPosition       the next position of the ball
     * @param ballRadius         the radius of the ball
     * @param speedDirectionBall the speed and direction of the ball
     * @return a {@code HitType} representing the type of hit that occurred
     * @see model.interfaces.InterfaceRacketModel.HitType
     */
    @Override
    public HitType hitBall(Vector2 ballPosition, Vector2 nextPosition, double ballRadius, Vector2 speedDirectionBall) {

        // We retrieve 2 points that belong to the perimeter of the ball, such that, if
        // a line is drawn through these two points, this
        // line will be perpendicular to the line passing through the point at the
        // center of the ball and the direction vector of speed.

        // We retrieve a vector perpendicular to the speed of the ball.
        Vector2 normalVitesse = new Vector2(speedDirectionBall);
        normalVitesse.normalise();
        normalVitesse.addAngle(Math.PI / 2);
        normalVitesse.scalarMultiplication(ballRadius);
        // Point1 belonging to the ball
        Vector2 point1 = new Vector2(ballPosition);
        point1.add(normalVitesse);
        // Point2 belonging to the ball
        Vector2 point2 = new Vector2(ballPosition);
        normalVitesse.addAngle(Math.PI);
        point2.add(normalVitesse);

        // Initialize an orthonormal axis
        Vector2 abscissa = new Vector2(1, 0);
        Vector2 ordinate = new Vector2(0, 1);

        // Initialize a point on the sides of the racket that can come into contact
        // with the ball. Allows to define which side of the racket can still come into
        // contact with the ball.

        // For example if the ball is on the right and above
        // the racket, it can only come into contact with the right side and the
        // top of the racket.

        Vector2 verticalPoint = new Vector2(getCoord()); // the left side of the racket
        if (isRacketLeft()) {
            abscissa.scalarMultiplication(getRacketWidth());
            verticalPoint.add(abscissa);// the right side of the racket
        } else {
            abscissa.scalarMultiplication(-ballRadius / 2);
            verticalPoint.add(abscissa);// the left side of the racket
        }

        Vector2 horizontalPoint = new Vector2(getCoord()); // the top of the racket
        if (ballPosition.getYdir() > this.getCoordY() + getRacketHeight() / 2) {
            ordinate.scalarMultiplication(getRacketHeight());
            horizontalPoint.add(ordinate);// the bottom of the racket
        }

        // Compute intersection points between:
        // - Point1 and Point2 (belonging to the ball)
        // - The racket

        Vector2 intersectionX1 = IntersectionCalculator2D.computeLineIntersection(speedDirectionBall, point1, abscissa,
                horizontalPoint);
        Vector2 intersectionX2 = IntersectionCalculator2D.computeLineIntersection(speedDirectionBall, point2, abscissa,
                horizontalPoint);
        Vector2 intersectionY1 = IntersectionCalculator2D.computeLineIntersection(speedDirectionBall, point1, ordinate,
                verticalPoint);
        Vector2 intersectionY2 = IntersectionCalculator2D.computeLineIntersection(speedDirectionBall, point2, ordinate,
                verticalPoint);

        // Compute the intersection points between:
        // - The line passing through the center of the ball and the direction vector of
        // velocity
        // - The racket
        Vector2 intersectionCenterBallRacket;

        // Compute the distances between:
        // - Point1 and Point2 (belonging to the ball)
        // - The racket.
        double distanceBallPositionIntersectionX1 = point1.distance(intersectionX1);
        double distanceBallPositionIntersectionY1 = point1.distance(intersectionY1);
        double distanceBallPositionIntersectionX2 = point2.distance(intersectionX2);
        double distanceBallPositionIntersectionY2 = point2.distance(intersectionY2);

        // Compute the distance between the center of the ball and the racket.
        double distanceBallPositionNextPosition = ballPosition.distance(nextPosition);

        // Test if the ball is no longer in contact with the racket
        if (isNoLongerInContactWithBall(distanceBallPositionIntersectionX1, distanceBallPositionIntersectionX2,
                distanceBallPositionIntersectionY1, distanceBallPositionIntersectionY2,
                distanceBallPositionNextPosition, ballPosition, nextPosition, ballRadius)) {
            if (isRacketLeft()) {
                ballJustTouchLeftRacket = false;
            } else {
                ballJustTouchRightRacket = false;
            }
        }

        // If the ball has just hit the racket, it cannot touch it again twice in a row.
        if (ballJustTouchRightRacket || ballJustTouchLeftRacket) {
            return HitType.NONE;
        }

        intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(speedDirectionBall,
                ballPosition, ordinate,
                verticalPoint);

        // The two lines passing through Point1 and Point2 and the vector direction of
        // speed of the ball intersect the same vertical side
        if (ballCrossRacketY(intersectionY1, distanceBallPositionIntersectionY1, distanceBallPositionNextPosition)
                && ballCrossRacketY(intersectionY2, distanceBallPositionIntersectionY2,
                        distanceBallPositionNextPosition)) {

            this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
            return HitType.BALL_HIT_VERTICAL;
        }

        // The line passing through point1 and the vector direction of speed intersects
        // the racket vertically
        if (ballCrossRacketY(intersectionY1, distanceBallPositionIntersectionY1, distanceBallPositionNextPosition)) {

            // If the ball touches the racket while moving down but is
            // too high to touch the right racket vertically
            if (intersectionY1.getYdir() < this.getCoordY() + ballRadius / 4
                    && ballPosition.getYdir() < nextPosition.getYdir() && !isRacketLeft()) {

                intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(
                        speedDirectionBall, ballPosition,
                        abscissa, horizontalPoint);
                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL;
            }

            // If the ball touches the racket while moving down but is
            // almost too high to touch the right racket vertically
            if (intersectionY1.getYdir() < this.getCoordY() + ballRadius / 2
                    && ballPosition.getYdir() < nextPosition.getYdir() && !isRacketLeft()) {

                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL_AND_VERTICAL;
            }

            // If the ball touches the racket while moving up but is
            // too low to touch the left racket vertically
            if (intersectionY1.getYdir() > this.getCoordY() + this.getRacketHeight() -
                    ballRadius / 4
                    && ballPosition.getYdir() > nextPosition.getYdir() && isRacketLeft()) {

                intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(
                        speedDirectionBall, ballPosition,
                        abscissa, horizontalPoint);
                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL;
            }

            // If the ball touches the racket while moving up but is
            // almost too low to touch the left racket vertically
            if (intersectionY1.getYdir() > this.getCoordY() + this.getRacketHeight() - ballRadius / 2
                    && ballPosition.getYdir() > nextPosition.getYdir() && isRacketLeft()) {

                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL_AND_VERTICAL;
            }

            // The ball touches the racket vertically
            this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
            return HitType.BALL_HIT_VERTICAL;
        }

        // The line passing through point2 and the speed direction vector intersects the
        // racket vertically
        if (ballCrossRacketY(intersectionY2, distanceBallPositionIntersectionY2, distanceBallPositionNextPosition)) {

            // If the ball touches the racket while moving up but is
            // too low to touch the right racket vertically
            if (intersectionY2.getYdir() > this.getCoordY() + this.getRacketHeight() - ballRadius / 4
                    && ballPosition.getYdir() > nextPosition.getYdir() && !isRacketLeft()) {

                intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(
                        speedDirectionBall, ballPosition,
                        abscissa, horizontalPoint);
                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL;
            }

            // If the ball touches the racket while moving up but is
            // almost too low to touch the right racket vertically
            if (intersectionY2.getYdir() > this.getCoordY() + this.getRacketHeight() - ballRadius / 2
                    && ballPosition.getYdir() > nextPosition.getYdir() && !isRacketLeft()) {

                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL_AND_VERTICAL;
            }

            // If the ball touches the racket while moving down but is
            // too high to touch the left racket vertically
            if (intersectionY2.getYdir() < this.getCoordY() + ballRadius / 4
                    && ballPosition.getYdir() < nextPosition.getYdir() && isRacketLeft()) {

                intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(
                        speedDirectionBall, ballPosition,
                        abscissa, horizontalPoint);
                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL;
            }

            // If the ball touches the racket while moving downward but is
            // almost too high to touch the left racket vertically
            if (intersectionY2.getYdir() < this.getCoordY() + ballRadius / 2
                    && ballPosition.getYdir() < nextPosition.getYdir() && isRacketLeft()) {

                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL_AND_VERTICAL;
            }

            // The ball touches the racket vertically
            this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
            return HitType.BALL_HIT_VERTICAL;
        }

        intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(speedDirectionBall,
                ballPosition, abscissa,
                horizontalPoint);

        // The line passing through point1 and vector direction speed intersects the
        // racket horizontally
        if (ballCrossRacketX(intersectionX1, distanceBallPositionIntersectionX1, distanceBallPositionNextPosition)) {

            // If the ball touches the racket while moving upward but is
            // too close to the edge to touch the right racket horizontally
            if (intersectionX1.getXdir() < this.getCoordX() + ballRadius / 4
                    && ballPosition.getYdir() > nextPosition.getYdir() && !isRacketLeft()) {

                intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(
                        speedDirectionBall, ballPosition,
                        ordinate, verticalPoint);
                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_VERTICAL;
            }

            // If the ball touches the racket while moving upward but is
            // almost to close to the edge to touch the right racket horizontally
            if (intersectionX1.getXdir() < this.getCoordX() + ballRadius / 2
                    && ballPosition.getYdir() > nextPosition.getYdir() && !isRacketLeft()) {

                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL_AND_VERTICAL;
            }

            // If the ball touches the racket while moving downward but is
            // too close to the edge to touch the left racket horizontally
            if (intersectionX1.getXdir() > this.getCoordX() + this.getRacketWidth() - ballRadius / 4
                    && ballPosition.getYdir() < nextPosition.getYdir() && isRacketLeft()) {

                intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(
                        speedDirectionBall, ballPosition,
                        ordinate, verticalPoint);
                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_VERTICAL;
            }

            // If the ball touches the racket while moving downward but is
            // almost too close to the edge to touch the left racket horizontally
            if (intersectionX1.getXdir() > this.getCoordX() + this.getRacketWidth() - ballRadius / 2
                    && ballPosition.getYdir() < nextPosition.getYdir() && isRacketLeft()) {

                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL_AND_VERTICAL;
            }

            // The ball touches the racket horizontally
            this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
            return HitType.BALL_HIT_HORIZONTAL;

        }

        // The line passing through point2 and vector direction speed intersects the
        // racket horizontally
        if (ballCrossRacketX(intersectionX2, distanceBallPositionIntersectionX2, distanceBallPositionNextPosition)) {

            // If the ball touches the racket while moving upward but is
            // too close to the edge to touch the right racket horizontally
            if (intersectionX2.getXdir() > this.getCoordX() + this.getRacketWidth() - ballRadius / 4
                    && ballPosition.getYdir() > nextPosition.getYdir() && isRacketLeft()) {

                intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(
                        speedDirectionBall, ballPosition,
                        ordinate, verticalPoint);
                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_VERTICAL;
            }

            // If the ball touches the racket while moving upward but is
            // almost too close to the edge to touch the right racket horizontally
            if (intersectionX2.getXdir() > this.getCoordX() + this.getRacketWidth() - ballRadius / 2
                    && ballPosition.getYdir() > nextPosition.getYdir() && isRacketLeft()) {

                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL_AND_VERTICAL;
            }

            // If the ball touches the racket while moving downward but is
            // too close to the edge to touch the right racket horizontally
            if (intersectionX2.getXdir() < this.getCoordX() + ballRadius / 4
                    && ballPosition.getYdir() < nextPosition.getYdir() && !isRacketLeft()) {

                intersectionCenterBallRacket = IntersectionCalculator2D.computeLineIntersection(
                        speedDirectionBall, ballPosition,
                        ordinate, verticalPoint);
                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_VERTICAL;
            }

            // If the ball touches the racket while moving downward but is
            // very close to the edge to touch the right racket horizontally
            if (intersectionX2.getXdir() < this.getCoordX() + ballRadius / 2
                    && ballPosition.getYdir() < nextPosition.getYdir() && !isRacketLeft()) {

                this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
                return HitType.BALL_HIT_HORIZONTAL_AND_VERTICAL;
            }

            // The ball touches the racket horizontally
            this.ballTouchRacket(nextPosition, intersectionCenterBallRacket);
            return HitType.BALL_HIT_HORIZONTAL;
        }

        return HitType.NONE;
    }
}
