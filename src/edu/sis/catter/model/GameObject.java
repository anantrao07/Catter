package edu.sis.catter.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class GameObject {
    protected Vect2D position;
    protected Vect2D speed;
    protected Bitmap sprite;

    public GameObject(Bitmap sprite, Vect2D position) {
        this.sprite = sprite;
        this.position = position;
        this.speed = new Vect2D();
    }

    public void update() {
        position = position.add(speed);
    }

    public void render(Canvas canvas) {
        canvas.drawBitmap(sprite, position.getX(), position.getY(), null);
    }
}
