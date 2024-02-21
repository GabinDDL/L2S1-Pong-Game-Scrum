package model.interfaces;

import model.Vector2;
import model.interfaces.InterfaceRacketController.State;

/**
 * Interface for the SolidObject abstract class.
 */
public interface InterfaceSolidObject {

    // Getters

    // Coordinates
    Vector2 getCoord();

    double getCoordX();

    double getCoordY();

    // Size

    double getSize();

    // Speed
    double getSpeed();

    double getInitialSpeed();

    double getMajorSpeed();

    // Acceleration

    double getAcceleration();

    double getDeceleration();

    // Setters

    // Coordinates
    void setCoord(Vector2 c);

    void setCoordX(double xDir);

    void setCoordY(double yDir);

    // Size

    void setSize(double s);

    // Speed

    void setSpeed(double s);

    void setInitialSpeed(double iniSpeed);

    void setMajorSpeed(double majSpeed);

    // Acceleration
    void setAcceleration(double acc);

    void setDeceleration(double dec);

    // Methods

    void reset(double height);

    void reset(double width, double height);

    void update(double deltaT, double height);

    void update(double deltaT, double height, State state);
}