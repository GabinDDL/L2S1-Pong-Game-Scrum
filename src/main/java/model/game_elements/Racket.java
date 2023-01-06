package model.game_elements;

import gui.game_elements.RacketGui;

import model.Vector2;
import model.interfaces.InterfaceRacket;
import model.interfaces.InterfaceRacketController.State;

import javafx.scene.shape.Shape;

public class Racket implements InterfaceRacket {
    private RacketGui racketGui;
    private RacketModel racketModel;

    // Constructor
    public Racket(Vector2 coord, double speed, double racketWidth, double racketHeight) {
        racketModel = new RacketModel(coord, speed, racketHeight, racketWidth);
        racketGui = new RacketGui(coord, racketWidth, racketHeight);
    }

    // Getters
    @Override
    public Shape getShape() {
        return racketGui.getShape();
    }

    @Override
    public double getCoordX() {
        return racketModel.getCoordX();
    }

    @Override
    public double getCoordY() {
        return racketModel.getCoordY();
    }

    @Override
    public double getSize() {
        return racketModel.getSize();
    }

    @Override
    public State getState() {
        return racketGui.getState();
    }

    public RacketGui getRacketGui() {
        return racketGui;
    }

    public RacketModel getRacketModel() {
        return racketModel;
    }

    public Vector2 getCoord() {
        return racketModel.getCoord();
    }

    public double getRacketWidth() {
        return racketModel.getRacketWidth();
    }

    public double getRacketHeight() {
        return racketModel.getRacketHeight();
    }

    @Override
    public double getWidth() {
        return getRacketWidth();
    }

    // Setters

    @Override
    public void setState(State state) {
        racketGui.setState(state);
        racketModel.setState(state);
    }

    @Override
    public void setCoord(Vector2 coords) {
        racketGui.setCoord(coords);
        racketModel.setCoord(coords);
    }

    // Methods

    @Override
    public void update(double deltaT, double height, State state) {
        racketModel.update(deltaT, height, state);
    }

    @Override
    public void reset(double height) {
        racketModel.reset(height);
    }
}