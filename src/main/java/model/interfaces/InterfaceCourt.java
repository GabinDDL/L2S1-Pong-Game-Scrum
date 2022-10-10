package model.interfaces;

import model.Score;

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
     * @return the Court's Racket A
     */
    public double getRacketA();

    /**
     * @return the Court's Racket B
     */
    public double getRacketB();

    /**
     * @return the Court's Score A
     */
    public Score getScoreA();

    /**
     * @return the Court's Score B
     */
    public Score getScoreB();

    /**
     * Updates the Court
     * @param deltaT
     */
    public void update(double deltaT);

    /**
     * Resets the Court
     */
    public void reset();
    
}