package gui;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import model.MediaHandler;

/**
 * {@code App} represents the primary and main element of our application.
 * <p>
 * App is graphically a Window that will be the parent of all the elements of
 * our game.
 * </p>
 */
public class App extends Application {

    private static String linkProjectPage = "https://pongworld.teleporthq.app/";

    private static int scoreLimit = 7;

    // Buttons 1 to 4 are the options (switchs in this case) that can be enable.
    public static boolean soundsButton = true;
    public static boolean aleas = false;
    public static boolean whichScore = true;
    public static boolean infiniteGame = false;

    // Load first .fxml file's URL to load the 'BorderPane' of HomePage
    URL fxmlURL = MediaHandler.getFXMLURL("home.fxml");
    FXMLLoader loader = new FXMLLoader(fxmlURL);

    public static boolean musicState = true;

    public static int getScoreLimit() {
        return scoreLimit;
    }

    public static void setScoreLimit(int newScore) {
        scoreLimit = newScore;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        // root is the pane containing all the game
        // Transitions, addition of objects, etc are made in it
        Pane root = new Pane();
        root.setId("ABSOLUTE_ROOT");

        // We place the 'BorderPane' of the HomePage as the first child in the root
        root.getChildren().add(loader.load());

        // gameScene is the scene appearing on the screen
        Scene gameScene = new Scene(root); // scene qui apparaît dans l'écran

        // Defines options of the Window
        primaryStage.setResizable(false);
        primaryStage.setTitle("Pong World");
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    public void openProjectPage() {
        getHostServices().showDocument(linkProjectPage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
