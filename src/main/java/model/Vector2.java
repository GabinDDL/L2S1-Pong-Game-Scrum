package model;

import java.util.Objects;

/**
 * Class that represents a 2D vector.
 * It has some built-in applications to compute
 * basic vector operations and some distance/velocity
 * specific features.
 */
public class Vector2 {

    private double xDir, yDir;
    private static double angleMax = Math.PI / 4;

    // Constructors
    public Vector2(double xDir, double yDir) {
        this.xDir = xDir;
        this.yDir = yDir;
    }

    public Vector2(Vector2 vector) {
        this(vector.xDir, vector.yDir);
    }

    // Getters

    // Direction

    public double getXdir() {
        return this.xDir;
    }

    public double getYdir() {
        return this.yDir;
    }

    // Norm

    /**
     * @return the norm of the vector.
     */
    public double getNorm() {
        return Math.sqrt(this.xDir * this.xDir + this.yDir * this.yDir);
    }

    // Angle

    /**
     * @return the angle of the vector.
     */
    public double getAngle() {
        return Math.atan2(this.yDir, this.xDir);
    }

    /**
     * @return the maximum angle
     */
    public static double getAngleMax() {
        return angleMax;
    }

    // Setters

    // Direction

    /**
     * This function sets the direction of the ball to the given values.
     * 
     * @param i the x direction of the ball
     * @param j The y-coordinate of the direction vector
     */
    public void setDirection(double i, double j) {
        this.xDir = i;
        this.yDir = j;
    }

    /**
     * This function sets the direction of the vector to the direction of the vector
     * passed in.
     * 
     * @param vector The vector to set the direction to.
     */
    public void setDirection(Vector2 vector) {
        this.xDir = vector.xDir;
        this.yDir = vector.yDir;
    }

    // Norm

    /**
     * This function sets the norm of the vector to the given value.
     * 
     * @param norm The norm to set the vector to.
     */

    public void setNorm(double n) {
        this.normalise();
        this.scalarMultiplication(n);
    }

    // Methods

    /**
     * This function adds the X and Y components of the vector passed in to the X
     * and Y components of the vector that called the function.
     * 
     * @param vector The vector to add to this vector.
     */
    public void add(Vector2 vector) {
        this.xDir += vector.xDir;
        this.yDir += vector.yDir;
    }

    /**
     * This function adds the values of i and j to the xDir and yDir variables.
     * 
     * @param i the x-direction of the vector
     * @param j The y-coordinate of the vector
     */
    public void add(double i, double j) {
        this.xDir += i;
        this.yDir += j;
    }

    /**
     * Vector scalar multiplication.
     * 
     * @param x scalar
     */
    public void scalarMultiplication(double x) {
        this.xDir *= x;
        this.yDir *= x;
    }

    /**
     * Normalises a vector.
     */
    public void normalise() {
        this.scalarMultiplication(1 / this.getNorm());
    }

    /**
     * It adds an angle to the direction of the vector.
     * 
     * @param angle the angle to add to the current direction
     */
    public void addAngle(double angle) {
        double n = this.getNorm();
        angle += Math.atan2(this.yDir, this.xDir);

        this.xDir = n * Math.cos(angle);
        this.yDir = n * Math.sin(angle);
    }

    /**
     * Adds an angle to the direction of the vector,
     * but only allows the vector to point in certain angles,
     * in order to avoid sharp vertical motions.
     * 
     * @param angle the angle to add to the current direction
     */
    public void addAngleRestricted(double angle) {
        double n = this.getNorm();
        angle += Math.atan2(this.yDir, this.xDir); // angle added + original angle (between -PI and PI)
        angle = angle % (2 * Math.PI); // adjust angle between -2PI and 2PI

        // adjust angle between -PI and PI
        if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        } else if (angle < -Math.PI) {
            angle += 2 * Math.PI;
        }

        // Trigonometric circle

        // In Top-Right Quadrant
        if (angle <= Math.PI / 2 && angle > Math.PI / 2 - angleMax)
            angle = Math.PI / 2 - angleMax;
        // In Top-Left Quadrant
        else if (angle > Math.PI / 2 && angle < Math.PI / 2 + angleMax)
            angle = Math.PI / 2 + angleMax;
        // In Bottom-Right Quadrant
        else if (angle >= -Math.PI / 2 && angle < -Math.PI / 2 + angleMax)
            angle = -Math.PI / 2 + angleMax;
        // In Bottom-Left Quadrant
        else if (angle < -Math.PI / 2 && angle > -Math.PI / 2 - angleMax)
            angle = -Math.PI / 2 - angleMax;

        this.xDir = n * Math.cos(angle);
        this.yDir = n * Math.sin(angle);
    }

    /**
     * This method updates a position vector given
     * the velocity vector and the time passed
     * 
     * @param velocity Velocity vector
     * @param deltaT   Time passed
     */
    public void updateDistanceVector(Vector2 velocity, double deltaT) {
        this.xDir += velocity.xDir * deltaT;
        this.yDir += velocity.yDir * deltaT;
    }

    /**
     * This methods computes the distance between two points.
     * 
     * @param point The point to compute the distance to.
     * @return The distance between the two points.
     */
    public double distance(Vector2 point) {
        double x = this.getXdir() - point.getXdir();
        double y = this.getYdir() - point.getYdir();
        return Math.sqrt(x * x + y * y);
    }

    /**
     * This method rounds the coordinates of the vector to the nearest integer.
     */
    public void roundCoords() {
        this.xDir = Math.round(this.xDir);
        this.yDir = Math.round(this.yDir);
    }

    @Override
    public String toString() {
        return "(" + this.xDir + "," + this.yDir + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Vector2))
            return false;

        Vector2 v = (Vector2) obj;

        return eq(xDir, v.getXdir()) && eq(yDir, v.getYdir());

    }

    private boolean eq(double a, double b) {
        return Math.abs(a - b) < 1E-9;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xDir, yDir);
    }

}
