package edu.sis.catter.model;

public class Vect2D {
    private int x, y;

    public Vect2D() {
        x = y = 0;
    }

    public Vect2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vect2D add(Vect2D other) {
        return new Vect2D(x + other.getX(), y + other.getY());
    }

    public Vect2D sub(Vect2D other) {
        return new Vect2D(x - other.getX(), y - other.getY());
    }

    public void reset() {
        x = y = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
