package model;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Score {
    private int score;
    private Text textScore;
    private int x; //coordonnées x d'affichage du score
    private int y; //coordonnées y d'affichage du score

    public Score (int x, int y) {
        score=0;
        textScore = new Text("0");
        this.x = x;
        this.y = y;
    }

    public void initDisplay (Color color, double width) {
        textScore.setTranslateX(this.x); //coordonné x de l'affichage du score A
        textScore.setTranslateY(this.y); //coordonné y de l'affichage du score A
        textScore.setScaleX(10.0); //largeur de l'affichage du score A
        textScore.setScaleY(10.0); //hateur de l'affichage du score A
        textScore.setFill(color); //Couleur de l'affichage du score A
    }

    public void win () {
        score +=1;
    }

    public void updateDisplay () {
        textScore.setText("" + score);
    }
    
    public Text getTextScore() {
        return textScore;
    }
}
