package edu.sis.catter.model;

import android.graphics.Bitmap;
import android.view.MotionEvent;


public class Cat extends GameObject {
    private static final int VELOCITY = 8;

    private int screenWidth;
    private int screenHeight;

    public Cat(Bitmap sprite, Vect2D position, int width, int height) {
        super(sprite, position);
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void move(Direction dir) {
        switch (dir) {
        case UP:
            speed.setY(-VELOCITY);
            break;

        case DOWN:
            speed.setY(VELOCITY);
            break;

        case LEFT:
            speed.setX(-VELOCITY);
            break;

        case RIGHT:
            speed.setX(VELOCITY);
            break;
        }
    }

    public void stop() {
        speed.reset();
    }

    @Override
    public void update() {
        super.update();

        if (position.getX() < 0) {
            position.setX(0);
        } else if (position.getX() >= screenWidth - sprite.getWidth()) {
            position.setX(screenWidth - sprite.getWidth());
        }

        if (position.getY() < 0) {
            position.setY(0);
        } else if (position.getY() >= screenHeight - sprite.getHeight()) {
            position.setY(screenHeight - sprite.getHeight());
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        final int action = e.getAction();

        if (action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_MOVE) {
            final int x = (int) e.getX();
            final int y = (int) e.getY();

            if (x < 100) {
                move(Direction.LEFT);
            } else if (x > screenWidth - 100) {
                move(Direction.RIGHT);
            }

            if (y < 50) {
                move(Direction.UP);
            } else if (y > screenHeight - 50) {
                move(Direction.DOWN);
            }
        } else if (action == MotionEvent.ACTION_UP) {
            stop();
        }

        return true;
    }
}
