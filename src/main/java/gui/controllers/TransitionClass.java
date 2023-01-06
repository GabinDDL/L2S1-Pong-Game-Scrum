package gui.controllers;

import java.io.IOException;
import java.net.URL;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import javafx.beans.property.DoubleProperty;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javafx.util.Duration;
import model.MediaHandler;

public class TransitionClass {

        // Boolean allowing only one animation at a time
        private static boolean currentlyPlaying = false;

        /**
         * Calculates the position of the scene's top left corner at the end of the
         * animation.
         * 
         * @param axis      True if horizontal, False if vertical
         * @param direction Horizontal :
         *                  - True : sliding effect right to left
         *                  - False : sliding effect left to right
         *                  Vertical :
         *                  - True : sliding effect bottom to top
         *                  - False : sliding effect top to bottom
         * @param scene     The scene containing elements to display
         */
        private static double endPosition(boolean axis, boolean direction, Scene scene) {
                return axis ? (direction ? -scene.getWidth() : scene.getWidth())
                                : (direction ? -scene.getHeight() : scene.getHeight());
        }

        /**
         * Designed to translate from classicHomePage to classicHomeGame
         * 
         * @param axis            True if horizontal, False if vertical
         * @param direction       Horizontal :
         *                        - True : sliding effect right to left
         *                        - False : sliding effect left to right
         *                        Vertical :
         *                        - True : sliding effect bottom to top
         *                        - False : sliding effect top to bottom
         * @param actualContainer The BorderPane containing the first section of
         *                        elements
         * @param nextContainer   Pane containing the next section of elements
         * @param endfonction     Anonymous function executed when the animation is
         *                        finished
         * @throws IOException
         */
        public static void classicGameTransition(boolean axis, boolean direction, Interpolator interpolator,
                        BorderPane actualContainer, Pane nextContainer, EndFunction endFunction) throws IOException {

                if (currentlyPlaying) {
                        return;
                }

                currentlyPlaying = true;

                Scene gameScene = actualContainer.getScene();

                // Parent container is the root (ABSOLUTE_ROOT)
                Pane parentContainer = (Pane) gameScene.getRoot();

                // Removes the actualContainer
                disappearTransition(axis, direction, interpolator, actualContainer, parentContainer, gameScene, true,
                                endFunction);

                // Adds the nextContainer
                appearTransition(axis, direction, interpolator, actualContainer, parentContainer, nextContainer,
                                gameScene, true);
        }

        /**
         * 
         * @param axis            True if horizontal, False if vertical
         * @param direction       Horizontal :
         *                        - True : sliding effect right to left
         *                        - False : sliding effect left to right
         *                        Vertical :
         *                        - True : sliding effect bottom to top
         *                        - False : sliding effect top to bottom
         * @param path            The String path leading to the .fxml of the next
         *                        section
         * @param actualContainer Current BorderPane containing the first section of
         *                        elements
         * @throws IOException
         */
        public static void transition(boolean axis, boolean direction, String path, Interpolator interpolator,
                        Parent actualContainer, EndFunction endFunction) throws IOException {

                if (currentlyPlaying) {
                        return;
                }

                currentlyPlaying = true;

                URL fxmlUrl = MediaHandler.getFXMLURL(path);
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent nextContainer = loader.load();

                Scene scene = actualContainer.getScene();

                // Parent container is the root (ABSOLUTE_ROOT)
                Pane parentContainer = (Pane) scene.getRoot();
                // Removes the actualContainer
                disappearTransition(axis, direction, interpolator, actualContainer, parentContainer, scene, true,
                                endFunction);

                // Adds the nextContainer
                appearTransition(axis, direction, interpolator, actualContainer, parentContainer, nextContainer, scene,
                                true);
        }

        /**
         * Transition needed to make disappear the first area of elements
         * 
         * @param axis            True if horizontal, False if vertical
         * @param direction       Horizontal :
         *                        - True : sliding effect right to left
         *                        - False : sliding effect left to right
         *                        Vertical :
         *                        - True : sliding effect bottom to top
         *                        - False : sliding effect top to bottom
         * @param actualContainer Current Pane containing the first section
         * @param parentContainer The element containing both of actual elements and
         *                        next elements to display
         * @param scene           Scene containing Pane and elements to display
         * @param endfonction     Fonction called once the animation is ended
         */
        private static void disappearTransition(boolean axis, boolean direction, Interpolator interpolator,
                        Parent actualContainer, Pane parentContainer, Scene scene, boolean toRemove,
                        EndFunction endFunction) {

                DoubleProperty translateProperty;

                // A different axis transition needs a different translate property
                if (axis) {
                        translateProperty = actualContainer.translateXProperty();
                } else {
                        translateProperty = actualContainer.translateYProperty();
                }

                // The starting coordonate of top left corner of actualContainer
                translateProperty.set(0);

                Timeline disappear = new Timeline();

                // KeyValue(tP, endPos, interpolator) :
                // tp -> translateProperty needed (X or Y oriented)
                // endPos -> the position where top left corner of actualContainer should be at
                // the end of animation
                // interpolator -> the style of translation (EASE_IN, EASE_OUT, EASE_BOTH)
                KeyValue kvD = new KeyValue(translateProperty, endPosition(axis, direction, scene), interpolator);
                KeyFrame kfD = new KeyFrame(Duration.seconds(1), kvD);
                disappear.getKeyFrames().add(kfD);

                // Once finished, removes the first container and call endfonction
                disappear.setOnFinished(t -> {
                        if (toRemove) {
                                parentContainer.getChildren().remove(actualContainer);
                        }

                        // An endfonction can be null in case of transition between menus
                        if (endFunction != null) {
                                endFunction.end();
                        }
                });

                // And launches disappear transition
                disappear.play();
        }

        /**
         * Transition needed to make appear the next area of elements
         * 
         * @param axis            True if horizontal, False if vertical
         * @param direction       Horizontal :
         *                        - True : sliding effect right to left
         *                        - False : sliding effect left to right
         *                        Vertical :
         *                        - True : sliding effect bottom to top
         *                        - False : sliding effect top to bottom
         * @param actualContainer Current Pane containing the first section
         * @param parentContainer The element containing both of actual elements and
         *                        next elements to display
         * @param nextContainer   Next Pane containing next section of elements
         * @param scene           Scene containing Pane and elements to display
         */
        private static void appearTransition(boolean axis, boolean direction, Interpolator interpolator,
                        Parent actualContainer, Pane parentContainer, Parent nextContainer, Scene scene,
                        boolean toRemove) {

                DoubleProperty translateProperty;

                // A different axis transition needs a different translate property
                if (axis) {
                        translateProperty = nextContainer.translateXProperty();
                } else {
                        translateProperty = nextContainer.translateYProperty();
                }

                // The starting coordonate of top left corner of nextContainer
                translateProperty.set(-endPosition(axis, direction, scene));

                // Adds the next elements to display to the global container
                parentContainer.getChildren().add(nextContainer);

                Timeline appear = new Timeline();

                // KeyValue(tP, endPos, interpolator) :
                // tp -> translateProperty needed (X or Y oriented)
                // endPos -> the position where top left corner of actualContainer should be at
                // the end of animation
                // interpolator -> the style of translation (EASE_IN, EASE_OUT, EASE_BOTH)
                KeyValue kv_a = new KeyValue(translateProperty, 0, interpolator);
                KeyFrame kf_a = new KeyFrame(Duration.seconds(1), kv_a);
                appear.getKeyFrames().add(kf_a);
                appear.setOnFinished(t -> {
                        currentlyPlaying = false;
                        if (toRemove) {
                                parentContainer.getChildren().remove(actualContainer);
                        }
                });

                // launches appear transition
                appear.play();
        }
}
