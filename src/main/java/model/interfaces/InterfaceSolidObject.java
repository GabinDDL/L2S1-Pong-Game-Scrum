package model.interfaces;

import model.Vector2;
import javafx.scene.paint.Color;

public interface InterfaceSolidObject {
    void setCoord(Vector2 c);

    void setCoordX(double Xdir);

    void setCoordY(double Ydir);

    Vector2 getCoord();

    double getCoordX();

    double getCoordY();

    void setSpeed(double s);

    double getSpeed();

    void setSize(double s);

    double getSize();

    void reset(double height);

    void reset(double width, double height);

    Vector2 update(double deltaT, double height, double width);

    void update(double deltaT, double height, RacketController player);

    void changeImageObject(boolean image, String imageTitle, Color color);

    void updateDisplay(double scale);

    void updateDisplay(double scale, double xMargin);

    void init(double scale, double xMargin);

    void init(double scale, double xMargin, double thickness);
}