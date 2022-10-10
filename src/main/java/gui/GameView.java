package gui;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Court;
import model.SceneDisplayController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class GameView {
    // class parameters
    private final Court court;
    private final Pane gameRoot; // main node of the game
    private final double scale;
    private final double xMargin = 50.0, racketThickness = 10.0; // pixels
    private SceneDisplayController sceneDisplayModifier;

    // children of the game main node
    private final Rectangle racketA, racketB;
    private final Circle ball;
    
    public void changeImageObject(String objet,String type, String imageTitle, Color color) {
        // le type est soit "image" soit "color"
        Image img = new Image("file:./Images/" + imageTitle); //creer une image à partir du fichier.
        if (type == "image") {
            switch (objet) {
                case "racketA":
                    racketA.setFill(new ImagePattern(img)); //attribut comme remplissage de la raquette, le motif de l'image.
                    break;
                case "racketB":
                    racketB.setFill(new ImagePattern(img));
                    break;
                case "ball":
                    ball.setFill(new ImagePattern(img));//pour la balle
                    break;
            }
        }
        else {
            switch (objet) {
                case "racketA":
                    racketA.setFill(color); //attribut comme remplissage de la raquette, la couleurr.
                    break;
                case "racketB":
                    racketB.setFill(color);
                    break;
                case "ball":
                    ball.setFill(color);//pour la ball
                    break;
            }
        }
    }

    public void changeImageBackground (String imageTitle){
        gameRoot.setStyle("-fx-background-image: url('file:./Images/" + imageTitle +"'); -fx-background-position: center center; -fx-background-repeat:no-repeat; -fx-background-size:100% 100%;");
    }

    /**
     * @param court le "modèle" de cette vue (le terrain de jeu de raquettes et tout ce qu'il y a dessus)
     * @param root  le nœud racine dans la scène JavaFX dans lequel le jeu sera affiché
     * @param scale le facteur d'échelle entre les distances du modèle et le nombre de pixels correspondants dans la vue
     */    

    public GameView(Court court, Pane root, double scale, SceneDisplayController sceneDisplayModifier) {
        this.court = court;
        this.gameRoot = root;
        this.scale = scale;
        this.sceneDisplayModifier = sceneDisplayModifier;

        root.setMinWidth(court.getWidth() * scale + 2 * xMargin);
        root.setMinHeight(court.getHeight() * scale);

        this.changeImageBackground("terrain.jpg"); //edit le fond d'ecran

        racketA = new Rectangle();
        racketA.setHeight(court.getRacketSize() * scale);
        racketA.setWidth(racketThickness);
        
        this.changeImageObject("racketA", "color", "", Color.RED); //change la couleur de racketA

        racketA.setX(xMargin - racketThickness);
        racketA.setY(court.getRacketA() * scale);

        racketB = new Rectangle();
        racketB.setHeight(court.getRacketSize() * scale);
        racketB.setWidth(racketThickness);
        this.changeImageObject("racketB", "color", "", Color.BLUE); //change la couleur de racketB

        racketB.setX(court.getWidth() * scale + xMargin);
        racketB.setY(court.getRacketB() * scale);

        court.getScoreA().initDisplay(Color.WHITE, court.getWidth()); //initialise l'affichage du score de racketA
        court.getScoreB().initDisplay(Color.WHITE, court.getWidth()); //initialise l'affichage du score de racketB

        ball = new Circle();
        ball.setRadius(court.getBallRadius());
        
        this.changeImageObject("ball", "image", "balle.jpg", Color.PINK); //change la color de ball

        ball.setCenterX(court.getBallX() * scale + xMargin);
        ball.setCenterY(court.getBallY() * scale);

        gameRoot.getChildren().addAll(racketA, racketB, ball, court.getScoreA().getTextScore(), court.getScoreB().getTextScore());


    }

    public void animate() {
        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {

                if (last == 0) { // ignore the first tick, just compute the first deltaT
                    last = now;
                    return;
                }

                if (sceneDisplayModifier.isInGame()) {

                    court.update((now - last) * 1.0e-9); // convert nanoseconds to seconds
                    
                    // Updates graphical part of the elements
                    racketA.setY(court.getRacketA() * scale);
                    racketB.setY(court.getRacketB() * scale);
                    ball.setCenterX(court.getBallX() * scale + xMargin);
                    ball.setCenterY(court.getBallY() * scale);
                    court.getScoreA().updateDisplay();//met à jour affichage de la valeur du score de racketA 
                    court.getScoreB().updateDisplay();//met à jour affichage de la valeur du score de racketB
                }
                last = now;
            }
        }.start();
    }
}
