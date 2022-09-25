package model;

public class Vector2 {
    // The two arguments of 2D vector
    private double Xdir,Ydir; 

    public Vector2(double Xdir, double Ydir){
        this.Xdir = Xdir;
        this.Ydir = Ydir;
    }

    public Vector2(Vector2 vector){
        this(vector.Xdir, vector.Ydir);
    }

    public double getXdir (){
        return this.Xdir;
    }

    public double getYdir (){
        return this.Ydir;
    }

    public void setDirection(double i, double j){
        this.Xdir = i;
        this.Ydir = j;
    }

    public void coppyVector(Vector2 vector){
        this.Xdir = vector.Xdir;
        this.Ydir = vector.Ydir;
    }

    public double norm(){
        return Math.sqrt(this.Xdir*this.Xdir + this.Ydir*this.Ydir);
    }

    public void add(Vector2 vector){
        this.Xdir += vector.Xdir;
        this.Ydir += vector.Ydir;
    }

    public void add(double i, double j){
        this.Xdir += i;
        this.Ydir += j;
    }

    public void scalarMultiplication(double x){
        this.Xdir *= x;
        this.Ydir *= x;
    }

    public void normalise(){
        this.scalarMultiplication(1/this.norm());
    }

    public void unitaryAdd(Vector2 vector){
        double n = this.norm();
        vector.normalise();
        this.normalise();
        this.add(vector);
        this.normalise();
        this.scalarMultiplication(n);
    }

    public void updateVector(Vector2 speed, double deltaT){
        this.Xdir += speed.Xdir * deltaT;
        this.Ydir += speed.Ydir * deltaT;
    }

    public String toString(){
        return "(" + this.Xdir + "," + this.Ydir + ")";
    }

}
