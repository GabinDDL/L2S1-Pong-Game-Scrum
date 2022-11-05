package model;

/**
 * Class that represents a 2D vector.
 * It has some built-in applications to compute
 * basic vector operations and some distance/velocity
 * specific features.
 */
public class Vector2 {

    private double xDir, yDir;

    // Constructors
    public Vector2(double Xdir, double Ydir) {
        this.xDir = Xdir;
        this.yDir = Ydir;
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
     * This function adds the values of i and j to the Xdir and Ydir variables.
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
     * It adds an angle to the direction of the vector, but only allows the vector
     * to point
     * in certain angles, in order to avoid sharp vertical motions.
     * 
     * @param angle the angle to add to the current direction
     */
    public void addAngle(double angle) {
        double n = this.getNorm();
        angle += Math.atan2(this.yDir, this.xDir);
        angle = angle >= 2 * Math.PI ? angle - 2 * Math.PI : angle;

        if (angle > Math.PI / 3 && angle <= Math.PI / 2)
            angle = Math.PI / 3;
        else if (angle > Math.PI / 2 && angle < 2 * Math.PI / 3)
            angle = 2 * Math.PI / 3;
        else if (angle > 4 * Math.PI / 3 && angle <= 3 * Math.PI / 2)
            angle = 4 * Math.PI / 3;
        else if (angle > 3 * Math.PI / 2 && angle <= 5 * Math.PI / 3)
            angle = 5 * Math.PI / 3;

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

    @Override
    public String toString() {
        return "(" + this.xDir + "," + this.yDir + ")";
    }

}
