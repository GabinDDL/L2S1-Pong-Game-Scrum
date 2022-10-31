package model.Objects;

import static constants.Constants.*;

import javafx.scene.shape.Circle;
import model.Sound;
import model.Vector2;
import model.interfaces.InterfaceBall;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color;

public class Ball extends SolidObject implements InterfaceBall {
    private Vector2 speedDirection;
    private Circle ball;
    private Sound soundBallRacket;
    private Sound soundBallMur;

    // constructeur
    public Ball(Vector2 coord, double InitialSpeed, double size) {
        super(coord, size); // speed = 200, size = 10.0 (radius)
        setInitialSpeed(InitialSpeed);
        ball = new Circle();
        ball.setRadius(super.getSize());
        this.setInitialSpeed(300);
        this.setMajorSpeed(800);
        soundBallRacket = new Sound("Bruitage ball racket.wav"); //son touche une raquette
        soundBallMur = new Sound("Bruitage ball mur.wav"); //son touche une raquette
    }

    // accesseurs
    public Circle getCircle() {
        return ball;
    }

    // méthodes
    /**
     * resets the position and the direction of the ball
     * 
     * @param double width of the court
     * @param double height of the court
     */
    @Override
    public void reset(double width, double height) {
        super.setCoord(new Vector2(width / 2, Math.random() * (2 * height / 3) + height / 6));

        // Generation a random direction vector of norm this.ballAbsoluteSpeed
        double angle = 2 * Math.random() - 1; // Angle entre -1 et 1 rad pour éviter les départs lents
        if (Math.random() > 0.5)
            angle += Math.PI; // Random side selector
        speedDirection = new Vector2(Math.cos(angle), Math.sin(angle));
        speedDirection.scalarMultiplication(getInitialSpeed());
    }

    /*
     * Produit une acceleration de la balle lorsque celle-ci va dans la même
     * direction que la raquette
     * Ne se produit que lorsque qu'il y a un rebond de la balle avec la raquette
     * isUp si la raquette va vers le haut
     */
    public void accelerationRacketBounce(boolean isUp, Vector2 newDirection, Racket racket) {
        int correctionValue = 3; // permet de diminuer l'acceleration / la deceleration lors du bounce

        if ((newDirection.getYdir() < 0 && isUp) || (newDirection.getYdir() > 0 && !isUp)) {
            newDirection.scalarMultiplication(1 + ((Math.abs(racket.getSpeed()) / racket.getMajorSpeed()) / correctionValue));
            if (newDirection.norm() > getMajorSpeed()) {
                newDirection.normalise();
                newDirection.scalarMultiplication(getMajorSpeed());
            }
        } else {
            newDirection
                    .scalarMultiplication(1 - ((Math.abs(racket.getSpeed()) / racket.getMajorSpeed()) / correctionValue));
            if (newDirection.norm() < getInitialSpeed()) {
                newDirection.normalise();
                newDirection.scalarMultiplication(getInitialSpeed());
            }
        }
    }

    // b : si true, playerA, si false, playerB
    public void computeRacketBounce(Vector2 nextPosition, double deltaT, Racket racket, boolean playerA) {
        speedDirection.setDirection(-speedDirection.getXdir(), speedDirection.getYdir());
        Vector2 newDirection = new Vector2(speedDirection);

        switch (racket.getPlayer().getState()) {
            case GOING_UP:
                if (playerA) {
                    newDirection.addAngle(23 * Math.PI / 12); // pourquoi *23 ?
                    if (newDirection.getXdir() <= 0)
                        newDirection = speedDirection;
                } else {
                    newDirection.addAngle(Math.PI / 12);
                    if (newDirection.getXdir() >= 0)
                        newDirection = speedDirection;
                }
                accelerationRacketBounce(true, newDirection, racket);
                break;
            case GOING_DOWN:
                if (playerA) {
                    newDirection.addAngle(Math.PI / 12);
                    if (newDirection.getXdir() <= 0)
                        newDirection = speedDirection;
                } else {
                    newDirection.addAngle(23 * Math.PI / 12);
                    if (newDirection.getXdir() >= 0)
                        newDirection = speedDirection;
                }
                accelerationRacketBounce(false, newDirection, racket);
                break;
            default:
                break;
        }
        soundBallRacket.play(); // sound play
        speedDirection.setDirection(newDirection);
        nextPosition.updateDistanceVector(speedDirection, deltaT);
    }

    /**
     * Updates the position of the ball
     * 
     * @param deltaT
     * @return
     */
    @Override
    public Vector2 update(double deltaT, double height, double width) { // return countbounce
        // first, compute possible next position if nothing stands in the way
        Vector2 nextPosition = new Vector2(getCoord());
        nextPosition.updateDistanceVector(speedDirection, deltaT);

        // Check if the ball hits the wall
        if (nextPosition.getYdir() < 0 || nextPosition.getYdir() > height) {

            speedDirection.setDirection(speedDirection.getXdir(), -speedDirection.getYdir());
            nextPosition.updateDistanceVector(speedDirection, deltaT);
            soundBallMur.play(); // sound play
        }
        return nextPosition;
    }

    /**
     * Change appearance to a color or an image
     * 
     * @param image      true if change to Image, false if change to color
     * @param imageTitle empty if change of color
     * @param color      this.getColor() if change of image
     */
    public void changeImageObject(boolean image, String imageTitle, Color color) {
        Image img = new Image("file:./" + DIR_IMAGES + imageTitle); // crée une image à partir du fichier
        if (image) {
            ball.setFill(new ImagePattern(img));
        } else {
            ball.setFill(color);
        }
    }

    /**
     * Initialise l'affichage de la balle dans GameView
     * 
     * @param double scale
     * @param double xMargin
     */
    public void init(double scale, double xMargin) {
        ball.setCenterX(getCoordX() * scale + xMargin);
        ball.setCenterY(getCoordY() * scale);
        this.changeImageObject(false, "", Color.PINK);
    }

    /**
     * Met à jour l'affichage de la balle dans GameView
     * 
     * @param double scale
     * @param double xMargin
     */
    public void updateDisplay(double scale, double xMargin) {
        ball.setCenterX(getCoordX() * scale + xMargin);
        ball.setCenterY(getCoordY() * scale);
    }
}