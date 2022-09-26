package model;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Score {
    private int scoreA;
    private int scoreB;
    private Text TextscoreA;
    private Text TextscoreB;

    public Score () {
        scoreA=0;
        scoreB=0;

        TextscoreA = new Text("0");
        TextscoreB = new Text("0");
    }

    public void initialisationAffichage (Color couleur, double width) {
        TextscoreA.setTranslateX(300); //coordonné x de l'affichage du score A
        TextscoreA.setTranslateY(50); //coordonné y de l'affichage du score A
        TextscoreA.setScaleX(10.0); //largeur de l'affichage du score A
        TextscoreA.setScaleY(10.0); //hateur de l'affichage du score A
        TextscoreA.setFill(couleur); //Couleur de l'affichage du score A

        TextscoreB.setTranslateX(width-200); //coordonné x de l'affichage du score B
        TextscoreB.setTranslateY(50); //coordonné y de l'affichage du score B
        TextscoreB.setScaleX(10.0); //largeur de l'affichage du score B
        TextscoreB.setScaleY(10.0); //hateur de l'affichage du score B
        TextscoreB.setFill(couleur); //couleur de l'affichage du score B
    }

    public void aGagne () {
        scoreA +=1;
    }

    public void bGagne () {
        scoreB +=1;
    }

    public void miseAJour () {
        TextscoreA.setText("" + scoreA);
        TextscoreB.setText("" + scoreB);
    }
    
    public Text getTextscoreA() {
        return TextscoreA;
    }

    public Text getTextscoreB() {
        return TextscoreB;
    }
}
