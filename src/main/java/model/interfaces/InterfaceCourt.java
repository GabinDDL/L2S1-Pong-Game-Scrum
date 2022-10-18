package model.interfaces;

import model.Score;

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

    /**
     * @return the Court's Score A
     */
    public Score getScoreA();

    /**
     * @return the Court's Score B
     */
    public Score getScoreB();

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