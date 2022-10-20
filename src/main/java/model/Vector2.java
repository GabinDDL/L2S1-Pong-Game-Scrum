package model;

public class Vector2 {
    /**
     * Class that represents a 2D vector.
     * It has some built-in applications to compute
     * basic vector operations and some distance/velocity
     * specific features.
     */

    private double Xdir, Ydir;

    public Vector2(double Xdir, double Ydir) {
        this.Xdir = Xdir;
        this.Ydir = Ydir;
    }

    public Vector2(Vector2 vector) {
        this(vector.Xdir, vector.Ydir);
    }

    public double getXdir() {
        return this.Xdir;
    }

    public double getYdir() {
        return this.Ydir;
    }

    /**
     * This function sets the direction of the ball to the given values.
     * 
     * @param i the x direction of the ball
     * @param j The y-coordinate of the direction vector
     */
    public void setDirection(double i, double j) {
        this.Xdir = i;
        this.Ydir = j;
    }

    /**
     * This function sets the direction of the vector to the direction of the vector
     * passed in.
     * 
     * @param vector The vector to set the direction to.
     */
    public void setDirection(Vector2 vector) {
        this.Xdir = vector.Xdir;
        this.Ydir = vector.Ydir;
    }

    /**
     * This function adds the X and Y components of the vector passed in to the X
     * and Y components of
     * the vector that called the function.
     * 
     * @param vector The vector to add to this vector.
     */
    public void add(Vector2 vector) {
        this.Xdir += vector.Xdir;
        this.Ydir += vector.Ydir;
    }

    /**
     * This function adds the values of i and j to the Xdir and Ydir variables.
     * 
     * @param i the x-direction of the vector
     * @param j The y-coordinate of the vector
     */
    public void add(double i, double j) {
        this.Xdir += i;
        this.Ydir += j;
    }

    /**
     * Vecotor scalar multiplication
     * 
     * @param x scalar
     */
    public void scalarMultiplication(double x) {
        this.Xdir *= x;
        this.Ydir *= x;
    }

    /**
     * @return the norm of the vector
     */
    public double norm() {
        return Math.sqrt(this.Xdir * this.Xdir + this.Ydir * this.Ydir);
    }

    /**
     * Nomalises a vector
     */
    public void normalise() {
        this.scalarMultiplication(1 / this.norm());
    }

    /**
     * It adds an angle to the direction of the vector, but only allows the vector
     * to point
     * in certain angles, in order to avoid sharp vertical motions.
     * 
     * @param angle the angle to add to the current direction
     */
    public void addAngle(double angle) {
        double n = this.norm();
        angle += Math.atan2(this.Ydir, this.Xdir);
        angle = angle >= 2 * Math.PI ? angle - 2 * Math.PI : angle;

        if (angle > Math.PI / 3 && angle <= Math.PI / 2)
            angle = Math.PI / 3;
        else if (angle > Math.PI / 2 && angle < 2 * Math.PI / 3)
            angle = 2 * Math.PI / 3;
        else if (angle > 4 * Math.PI / 3 && angle <= 3 * Math.PI / 2)
            angle = 4 * Math.PI / 3;
        else if (angle > 3 * Math.PI / 2 && angle <= 5 * Math.PI / 3)
            angle = 5 * Math.PI / 3;

        this.Xdir = n * Math.cos(angle);
        this.Ydir = n * Math.sin(angle);

    }

    /**
     * This method updates a position vector given
     * the velocity vector and the time passed
     * 
     * @param velocity Velocity vector
     * @param deltaT   Time passed
     */
    public void updateDistanceVector(Vector2 velocity, double deltaT) {
        this.Xdir += velocity.Xdir * deltaT;
        this.Ydir += velocity.Ydir * deltaT;
    }

    public String toString() {
        return "(" + this.Xdir + "," + this.Ydir + ")";
    }

}
