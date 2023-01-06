package model.game_elements;

import model.Vector2;
import model.interfaces.InterfaceSolidObject;
import model.interfaces.InterfaceRacketController.State;

/**
 * Abstract class representing an object with a hitbox in the game.
 */
public abstract class SolidObject implements InterfaceSolidObject {
    private Vector2 coord;

    private double size;

    private double speed;
    private double initialSpeed;
    private double majorSpeed;

    private double acceleration;
    private double deceleration;

    // Constructors

    SolidObject(Vector2 coord, double speed, double size) {
        this.coord = coord;
        this.speed = speed;
        this.size = size;
    }

    SolidObject(Vector2 coord, double size) {
        this.coord = coord;
        this.speed = 0;
        this.size = size;
    }

    // Getters

    // Coordinates

    public Vector2 getCoord() {
        return coord;
    }

    public double getCoordX() {
        return coord.getXdir();
    }

    public double getCoordY() {
        return coord.getYdir();
    }

    // Size

    public double getSize() {
        return size;
    }

    // Speed

    public double getSpeed() {
        return speed;
    }

    public double getInitialSpeed() {
        return initialSpeed;
    }

    public double getMajorSpeed() {
        return majorSpeed;
    }

    // Acceleration

    public double getAcceleration() {
        return acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    // Setters

    // Coordinates

    public void setCoord(Vector2 c) {
        coord = c;
    }

    public void setCoordX(double xDir) {
        coord.setDirection(xDir, coord.getYdir());
    }

    public void setCoordY(double yDir) {
        coord.setDirection(coord.getXdir(), yDir);
    }

    // Size

    public void setSize(double s) {
        size = s;
    }

    // Speed

    public void setSpeed(double s) {
        speed = s;
    }

    public void setInitialSpeed(double iniSpeed) {
        initialSpeed = iniSpeed;
    }

    public void setMajorSpeed(double majSpeed) {
        majorSpeed = majSpeed;
    }

    // Acceleration

    public void setAcceleration(double acc) {
        acceleration = acc;
    }

    public void setDeceleration(double dec) {
        deceleration = dec;
    }

    // Methods

    // Reset

    public void reset(double height) {
    }

    public void reset(double width, double height) {
    }

    // Update

    public void update(double deltaT, double height) {
    }

    public void update(double deltaT, double height, State state) {
    }
}