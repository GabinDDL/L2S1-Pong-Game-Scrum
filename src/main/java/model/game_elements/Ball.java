package model.game_elements;

import gui.game_elements.BallGui;

import model.Vector2;
import model.interfaces.InterfaceBall;
import model.interfaces.InterfaceBallModel.LastAction;
import javafx.scene.shape.Shape;

/**
 * This class represents the ball of the game. It includes the graphical part of
 * the ball and the model of the ball.
 */
public class Ball implements InterfaceBall {
    private BallGui ballGui;
    private BallModel ballModel;

    // Constructor
    public Ball(Vector2 coord, double initialSpeed, double initialMajorSpeed, double size) {
        ballModel = new BallModel(coord, initialSpeed, initialMajorSpeed, size);
        ballGui = new BallGui(coord, size);
    }

    // Getters

    public BallModel getBallModel() {
        return ballModel;
    }

    public double getSize() {
        return ballModel.getSize();
    }

    @Override
    public Shape getShape() {
        return ballGui.getShape();
    }

    @Override
    public double getCoordX() {
        return ballModel.getCoordX();
    }

    @Override
    public double getCoordY() {
        return ballModel.getCoordY();
    }

    public double getSpeedDirectionX() {
        return ballModel.getSpeedDirectionX();
    }

    public double getSpeedDirectionY() {
        return ballModel.getSpeedDirectionY();
    }

    // Setters
    @Override
    public void setRadius(double radius) {
        ballGui.setRadius(radius);
    }

    @Override
    public void setCoord(Vector2 coords) {
        ballGui.setCoord(coords);
    }

    public void setSpeedDirection(Vector2 speedDirection) {
        ballModel.setSpeedDirection(speedDirection);
    }

    // Methods

    /**
     * @return true if the ball is outside
     */
    public boolean isOutside(double width) {
        return ballModel.isOutside(width);
    }

    // Overrides from InterfaceBall

    @Override
    public LastAction update(double deltaT, double height, PlayerModel[] players) {
        return ballModel.update(deltaT, height, players);
    }

    @Override
    public void reset(double width, double height) {
        ballModel.reset(width, height);
    }

}
