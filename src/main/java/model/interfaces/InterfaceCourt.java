package model.interfaces;

import model.Objects.*;
import java.util.List;

/**
 * InterfaceCourt
 */
public interface InterfaceCourt {

    /**
     * @return the Court's width
     */
    public double getWidth();

    /**
     * @return the Court's height
     */
    public double getHeight();

    public List<SolidObject> getListObjects();

    /**
     * Updates the Court
     * 
     * @param deltaT
     */
    public void update(double deltaT);

    /**
     * Resets the Court
     */
    public void reset();

}