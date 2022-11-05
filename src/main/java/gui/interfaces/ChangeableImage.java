package gui.interfaces;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;

/**
 * This interface represents an object that can change its image.
 */
public interface ChangeableImage {

    Shape getShape();

    /**
     * Change the image of the object.
     * 
     * @param path the path of the image
     */
    default void changeImage(String path) {
        Image image = new Image(path);
        getShape().setFill(new ImagePattern(image));

    }

    /**
     * Change the color of the object.
     * 
     * @param color the color of the object
     */
    default void changeColor(Color color) {
        getShape().setFill(color);
    }
}
