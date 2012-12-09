package edu.sis.catter.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class GameObject {
    protected Vect2D position;
    protected Vect2D speed;
    protected Bitmap sprite;
    private Rect rect;

    public GameObject(Bitmap sprite, Vect2D position) {
        this.sprite = sprite;
        this.position = position;
        this.speed = new Vect2D();
        rect = new Rect(position.x, position.y, position.x + sprite.getWidth(),
                position.y + sprite.getHeight());
    }

    public void update() {
        position = position.add(speed);
        rect.left = position.x;
        rect.top = position.y;
        rect.right = position.x + sprite.getWidth();
        rect.bottom = position.y + sprite.getHeight();
    }

    public void render(Canvas canvas) {
        canvas.drawBitmap(sprite, position.x, position.y, null);
    }

    public Rect getBoundingRect() {
        return rect;
    }

    public Vect2D getPosition() {
        return position;
    }

    public void setPosition(Vect2D position) {
        this.position = position;
    }

    public Vect2D getSpeed() {
        return speed;
    }

    public void setSpeed(Vect2D speed) {
        this.speed = speed;
    }

    public boolean intersect(GameObject obj) {
        return Rect.intersects(rect, obj.rect);
    }
}
