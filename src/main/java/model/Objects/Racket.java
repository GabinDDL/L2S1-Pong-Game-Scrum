package model.Objects;

import static constants.Constants.*;

import javafx.scene.shape.Rectangle;
import model.Vector2;
import model.interfaces.InterfaceRacket;
import model.interfaces.RacketController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color;

public class Racket extends SolidObject implements InterfaceRacket {
    private Rectangle racket;
    private RacketController player;

    // constructeur
    public Racket(double x, double y, RacketController player, double speed, double size) {
        super(new Vector2(x, y), speed, size);
        racket = new Rectangle(100.0, 10.0);
        this.player = player;
        this.setAcceleration(600);
        this.setDeceleration(0.90);
        this.setInitialSpeed(50);
        this.setMajorSpeed(600);
    }

    // accesseurs

    public RacketController getPlayer() {
        return player;
    }

    public Rectangle getRectangle() {
        return racket;
    }

    // méthodes

    /*
     * Place la raquette en position initiale (le haut de la raquette est à la
     * moitié de la hauteur du Court)
     */
    @Override
    public void reset(double height) { // setCoord(height/2)
        setCoordY(height / 2);
        setSpeed(0);
    }

    /**
     * Retourne la position de la raquette en prenant en compte les limites d'ecran
     */
    public double reactionWithLimits(double height, double nextPosition) {
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

    /**
     * Updates the position and the speed of the racket
     */
    @Override
    public void update(double deltaT, double height, RacketController player) {
        switch (player.getState()) {
            case GOING_UP:
                if (getSpeed() > 0) // direction oppose
                    setSpeed(getSpeed() * getDeceleration()); // decceleration
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

    /**
     * Returns true if the ball hits the racket
     * 
     * @param boolean left : true si la racket est à gauche du Court, false à droite
     * @param Vector2 ballPosition
     * @param Vector2 nextPosition
     * @param double  width of the screen
     * @param double  ballRadius
     */
    public boolean hitBall(boolean left, Vector2 ballPosition, Vector2 nextPosition, double ballRadius) {
        boolean b = false;
        // si la balle arrive dans l'emplacement de la raquette
        if (nextPosition.getYdir() > getCoordY() - ballRadius / 2
                && nextPosition.getYdir() < getCoordY() + getSize() + ballRadius / 2) { // vérifier le sens du 5
                                                                                        // original, faire un schéma
            if (left) { // racketA
                if (nextPosition.getXdir() <= 0 && nextPosition.getXdir() >= -racket.getWidth()) {
                    b = true;
                }
            } else { // racketB
                if (nextPosition.getXdir() >= getCoordX()
                        && nextPosition.getXdir() <= getCoordX() + racket.getWidth()) {
                    b = true;
                }
            }
        }
        // si la balle arrive après la raquette mais était bien en jeu et non déjà
        // sortie ("la prochaine position la ferait traverser la raquette")
        if (ballPosition.getYdir() < getCoordY() - ballRadius / 2
                && nextPosition.getYdir() > getCoordY() + getSize() + ballRadius / 2) {
            if (left) { // racketA
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

    /**
     * Change appearance to a color or an image
     * 
     * @param image      true if change to Image, false if change to color
     * @param imageTitle empty if change of color
     * @param color      this.getColor() if change of image
     */
    @Override
    public void changeImageObject(boolean image, String imageTitle, Color color) {
        // le type est soit "image" soit "color"
        Image img = new Image("file:./" + DIR_IMAGES + imageTitle); // crée une image à partir du fichier
        if (image) {
            racket.setFill(new ImagePattern(img));
        } else {
            racket.setFill(color);
        }
    }

    /**
     * Initialise l'affichage de la raquette dans Gameview
     * 
     * @param double scale
     * @param double xMargin
     * @param double thickness
     */
    @Override
    public void init(double scale, double xMargin, double thickness) {
        racket.setHeight(getSize() * scale);
        racket.setWidth(thickness);
        this.changeImageObject(false, "", Color.RED);
        if (getCoordX() == 0) {
            racket.setX(xMargin - thickness);
        } else {
            racket.setX(getCoordX() * scale + xMargin);
        }
        racket.setY(getCoordY() * scale);
    }

    /**
     * Met à jour l'affichage de la raquette dans Gameview
     * 
     * @param double scale
     */
    @Override
    public void updateDisplay(double scale) {
        racket.setY(getCoordY() * scale);
    }

}