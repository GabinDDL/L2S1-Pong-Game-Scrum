package model.Objects;

import javafx.scene.paint.Color;
//import javafx.scene.shape;
import model.Vector2;
import model.interfaces.InterfaceSolidObject;
import model.interfaces.RacketController;

public class SolidObject implements InterfaceSolidObject {
    private Vector2 coord;
    private double speed; // m/s
    private double size;

    // constructeur
    SolidObject(Vector2 coord, double speed, double size) {
        this.coord = coord;
        this.speed = speed;
        this.size = size;
    }

    // accesseurs
    public void setCoord(Vector2 c) {
        coord = c;
    }

    public void setCoordX(double Xdir) {
        coord.setDirection(Xdir, coord.getYdir());
    }

    public void setCoordY(double Ydir) { // dansla classe racket seulement ?
        coord.setDirection(coord.getXdir(), Ydir);
    }

    public Vector2 getCoord() {
        return coord;
    }

    public double getCoordX() { // getXdir ?
        return coord.getXdir();
    }

    public double getCoordY() { // getYdir ?
        return coord.getYdir();
    }

    public void setSpeed(double s) {
        speed = s;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSize(double s) {
        size = s;
    }

    public double getSize() {
        return size;
    }

    // méthodes
    public void reset(double height) {
    }

    public void reset(double width, double height) {
    }

    public Vector2 update(double deltaT, double height, double width) {
        return null;
    }

    public void update(double deltaT, double height, RacketController player) {
    }

    public void changeImageObject(boolean image, String imageTitle, Color color) {
    }

    public void updateDisplay(double scale) {
    }

    public void updateDisplay(double scale, double xMargin) {
    }

    public void init(double scale, double xMargin) {
    }

    public void init(double scale, double xMargin, double thickness) {
    }
}