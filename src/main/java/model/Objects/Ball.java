package model.Objects;

import javafx.scene.shape.Circle;
import model.Vector2;
import model.interfaces.InterfaceBall;
import model.interfaces.RacketController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color;

public class Ball extends SolidObject implements InterfaceBall {
    private Vector2 speed;
    private Vector2 speedDirection;
    private Circle ball;
    private int countbounce;

    // constructeur
    public Ball(Vector2 coord, double speed, double size) {
        super(coord, speed, size); // speed = 200, size = 10.0 (radius)
        countbounce = 0;
        ball = new Circle();
        ball.setRadius(super.getSize());
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
        countbounce = 0;
        super.setSpeed(200.0);
        super.setCoord(new Vector2(width / 2, Math.random() * (2 * height / 3) + height / 6));

        // Generation a random direction vector of norm this.ballAbsoluteSpeed
        double angle = 2 * Math.random() - 1; // Angle entre -1 et 1 rad pour éviter les départs lents
        if (Math.random() > 0.5)
            angle += Math.PI; // Random side selector
        speedDirection = new Vector2(Math.cos(angle), Math.sin(angle));
        speedDirection.scalarMultiplication(super.getSpeed());
    }

    // b : si true, playerA, si false, playerB
    public void computeRacketBounce(Vector2 nextPosition, double deltaT, RacketController racket, boolean playerA) {
        speedDirection.setDirection(-speedDirection.getXdir(), speedDirection.getYdir());
        Vector2 newDirection = new Vector2(speedDirection);

        switch (racket.getState()) {
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
                break;
            default:
                break;
        }

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

            countbounce++; // augmente à chaque impact de la balle contre un mur

            if (countbounce < 49 && countbounce % 3 == 0) { // majoration + augmentation
                speedDirection.scalarMultiplication(1.10);
            }
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
        Image img = new Image("file:./Images/" + imageTitle); // crée une image à partir du fichier
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