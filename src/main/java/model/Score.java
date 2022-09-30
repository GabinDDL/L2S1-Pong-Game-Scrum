package model;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Score {
    private int score;
    private Text Textscore;
    private int x; //coordonnées x d'affichage du score
    private int y; //coordonnées y d'affichage du score

    public Score (int x, int y) {
        score=0;
        Textscore = new Text("0");
        this.x = x;
        this.y = y;
    }

    public void initialisationAffichage (Color couleur, double width) {
        Textscore.setTranslateX(this.x); //coordonné x de l'affichage du score A
        Textscore.setTranslateY(this.y); //coordonné y de l'affichage du score A
        Textscore.setScaleX(10.0); //largeur de l'affichage du score A
        Textscore.setScaleY(10.0); //hateur de l'affichage du score A
        Textscore.setFill(couleur); //Couleur de l'affichage du score A
    }

    public void Gagne () {
        score +=1;
    }

    public void miseAJour () {
        Textscore.setText("" + score);
    }
    
    public Text getTextscore() {
        return Textscore;
    }
}
