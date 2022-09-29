package gui;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Court;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class GameView {
    // class parameters
    private final Court court;
    private final Pane gameRoot; // main node of the game
    private final double scale;
    private final double xMargin = 50.0, racketThickness = 10.0; // pixels

    // children of the game main node
    private final Rectangle racketA, racketB;
    private final Circle ball;

    /**
     * @param court le "modèle" de cette vue (le terrain de jeu de raquettes et tout ce qu'il y a dessus)
     * @param root  le nœud racine dans la scène JavaFX dans lequel le jeu sera affiché
     * @param scale le facteur d'échelle entre les distances du modèle et le nombre de pixels correspondants dans la vue
     */
    
    public void changerImageobjet(String objet,String type, String nomDeLImage, Color couleur) {
        // le type est soit "image" soit "couleur"
        Image img = new Image("file:.\\Images\\" + nomDeLImage); //creer une image à partir du fichier.
        if (type == "image") {
            switch (objet) {
                case "racketA":
                    racketA.setFill(new ImagePattern(img)); //attribut comme remplissage de la raquette, le pattern de l'image.
                    break;
                case "racketB":
                    racketB.setFill(new ImagePattern(img));
                    break;
                case "ball":
                    ball.setFill(new ImagePattern(img));//pour la ball
                    break;
            }
        }
        else {
            switch (objet) {
                case "racketA":
                    racketA.setFill(couleur); //attribut comme remplissage de la raquette, la couleur.
                    break;
                case "racketB":
                    racketB.setFill(couleur);
                    break;
                case "ball":
                    ball.setFill(couleur);//paur la ball
                    break;
            }
        }
    }

    public void changerImageFond (String nomDeLImage){
        gameRoot.setStyle("-fx-background-image: url('file:./Images/" + nomDeLImage +"'); -fx-background-position: center center; -fx-background-repeat:no-repeat; -fx-background-size:100% 100%;");
    }

    public GameView(Court court, Pane root, double scale) {
        this.court = court;
        this.gameRoot = root;
        this.scale = scale;

        root.setMinWidth(court.getWidth() * scale + 2 * xMargin);
        root.setMinHeight(court.getHeight() * scale);

        //this.changerImageFond("terrain.jpg"); //edit le fond d'ecran

        racketA = new Rectangle();
        racketA.setHeight(court.getRacketSize() * scale);
        racketA.setWidth(racketThickness);
        
        this.changerImageobjet("racketA", "couleur", "", Color.RED); //change la couleur de la racketA

        racketA.setX(xMargin - racketThickness);
        racketA.setY(court.getRacketA() * scale);

        racketB = new Rectangle();
        racketB.setHeight(court.getRacketSize() * scale);
        racketB.setWidth(racketThickness);
        this.changerImageobjet("racketB", "couleur", "", Color.BLUE); //change la couleur de la racketB

        racketB.setX(court.getWidth() * scale + xMargin);
        racketB.setY(court.getRacketB() * scale);

        court.getScores().initialisationAffichage(Color.BLACK, court.getWidth()); //initialise l'affichage des scores

        ball = new Circle();
        ball.setRadius(court.getBallRadius());
        
        this.changerImageobjet("ball", "couleur", "balle.jpg", Color.PINK); //change la couleur de ball

        ball.setCenterX(court.getBallX() * scale + xMargin);
        ball.setCenterY(court.getBallY() * scale);

        gameRoot.getChildren().addAll(racketA, racketB, ball, court.getScores().getTextscoreA(), court.getScores().getTextscoreB());


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
                court.update((now - last) * 1.0e-9); // convert nanoseconds to seconds
                last = now;
                // Updates graphical part of the element
                racketA.setY(court.getRacketA() * scale);
                racketB.setY(court.getRacketB() * scale);
                ball.setCenterX(court.getBallX() * scale + xMargin);
                ball.setCenterY(court.getBallY() * scale);
                court.getScores().miseAJour();//met à jour la valeur du score du joueur A et du joueur B
            }
        }.start();
    }
}
