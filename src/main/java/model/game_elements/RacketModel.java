package model.game_elements;

import model.Vector2;
import model.interfaces.InterfaceRacketModel;
import model.interfaces.InterfaceRacketController.State;

/**
 * This class represents the model of the racket. It doesn't include the gui of
 * the racket.
 */
public class RacketModel extends SolidObject implements InterfaceRacketModel {

    private double racketWidth;
    private State state = State.IDLE;

    // constructeur
    public RacketModel(Vector2 coord, double speed, double size, double racketWidth) {
        super(coord, speed, size);
        this.racketWidth = racketWidth;
        this.setAcceleration(600);
        this.setDeceleration(0.90);
        this.setInitialSpeed(50);
        this.setMajorSpeed(600);
    }

    // Getters

    public State getState() {
        return state;
    }

    public double getRacketWidth() {
        return racketWidth;
    }

    public double getRacketHeight() {
        return this.getSize();
    }

    // Setters

    public void setState(State state) {
        this.state = state;
    }

    // Methods

    /**
     * Retourne la position de la raquette en prenant en compte les limites d'écran
     */
    private double reactionWithLimits(double height, double nextPosition) {
        if (nextPosition < 0) {
            setSpeed(0);
            return 0;
        }
        if (nextPosition + getSize() > height) {
            setSpeed(0);
            return height - getSize();
        }
        return nextPosition;
    }

    public boolean isRacketLeft() {
        return getCoordX() == 0;
    }

    // Overrides

    // From SolidObject

    /**
     * Updates the position and the speed of the racket
     */
    @Override
    public void update(double deltaT, double height, State state) {
        switch (state) {
            case GOING_UP:
                if (getSpeed() > 0) // direction oppose
                    setSpeed(getSpeed() * getDeceleration()); // décélération
                if (Math.abs(getSpeed()) < getMajorSpeed()) // acceleration
                    setSpeed(getSpeed() - deltaT * getAcceleration());
                setCoordY(reactionWithLimits(height, getCoordY() + getSpeed() * deltaT));
                break;
            case IDLE:
                if ((Math.abs(getSpeed()) < getInitialSpeed() || getCoordY() == 0 || getCoordY() == height + getSize())
                        && getSpeed() != 0)
                    setSpeed(0);
                else { // glissade
                    setSpeed(getSpeed() * getDeceleration());
                    setCoordY(reactionWithLimits(height, getCoordY() + getSpeed() * deltaT));
                }
                break;
            case GOING_DOWN:
                if (getSpeed() < 0) // direction oppose
                    setSpeed(getSpeed() * getDeceleration()); // deceleration
                if (Math.abs(getSpeed()) < getMajorSpeed()) // acceleration
                    setSpeed(getSpeed() + deltaT * getAcceleration());
                setCoordY(reactionWithLimits(height, getCoordY() + getSpeed() * deltaT));
                break;
        }
    }

    /*
     * Place la raquette en position initiale (le haut de la raquette est à la
     * moitié de la hauteur du Court)
     */
    @Override
    public void reset(double height) { // setCoord(height/2)
        setCoordY((height - getRacketHeight()) / 2);
        setSpeed(0);
    }

    // From InterfaceRacketModel

    @Override
    public boolean hitBall(Vector2 ballPosition, Vector2 nextPosition, double ballRadius) {
        boolean b = false;

        // si la balle arrive dans l'emplacement de la raquette
        if (nextPosition.getYdir() > getCoordY() - ballRadius / 2
                && nextPosition.getYdir() < getCoordY() + getSize() + ballRadius / 2) { // vérifier le sens du 5
                                                                                        // original, faire un schéma
            if (isRacketLeft()) { // racketA
                if (nextPosition.getXdir() <= 0 && nextPosition.getXdir() >= -racketWidth) {
                    b = true;
                }
            } else { // racketB
                if (nextPosition.getXdir() >= getCoordX()
                        && nextPosition.getXdir() <= getCoordX() + racketWidth) {
                    b = true;
                }
            }
        }

        // si la balle arrive après la raquette mais était bien en jeu et non déjà
        // sortie ("la prochaine position la ferait traverser la raquette")
        if (ballPosition.getYdir() < getCoordY() - ballRadius / 2
                && nextPosition.getYdir() > getCoordY() + getSize() + ballRadius / 2) {
            if (isRacketLeft()) { // racketA
                if (ballPosition.getXdir() > 0 && nextPosition.getXdir() < 0) {
                    b = true;
                }
            } else { // racketB
                if (ballPosition.getXdir() < getCoordX() && nextPosition.getXdir() > getCoordX()) {
                    b = true;
                }
            }
        }
        return b;
    }
}
