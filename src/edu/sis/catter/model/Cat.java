package edu.sis.catter.model;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.view.MotionEvent;


public class Cat extends GameObject {
    private static final long TIME_STEP = 300;
    public static final int JUMP_STEP = 40;

    private int screenWidth;
    private int screenHeight;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private long lastMoved = 0;

    public Cat(Bitmap sprite, Vect2D position, int width, int height) {
        super(sprite, position);
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void move(Direction dir) {
        switch (dir) {
        case UP:
            moveUp = true;
            break;

        case DOWN:
            moveDown = true;
            break;

        case LEFT:
            moveLeft = true;
            break;

        case RIGHT:
            moveRight = true;
            break;
        }
    }

    public void stop() {
        speed.reset();
    }

    @Override
    public void update() {
        super.update();

        if (moveRight) {
            moveRight = !moveRight;
            position.x += JUMP_STEP;
            lastMoved = SystemClock.uptimeMillis();
        } else if (moveLeft) {
            moveLeft = !moveLeft;
            position.x -= JUMP_STEP;
            lastMoved = SystemClock.uptimeMillis();
        } else if (moveDown) {
            moveDown = !moveDown;
            position.y += JUMP_STEP;
            lastMoved = SystemClock.uptimeMillis();
        } else if (moveUp) {
            moveUp = !moveUp;
            position.y -= JUMP_STEP;
            lastMoved = SystemClock.uptimeMillis();
        }

        if (position.x < 0) {
            position.x += JUMP_STEP;
        } else if (position.x > screenWidth - sprite.getWidth()) {
            position.x -= JUMP_STEP;
        }

        if (position.y < 0) {
            position.y += JUMP_STEP;
        } else if (position.y > screenHeight - sprite.getHeight()) {
            position.y -= JUMP_STEP;
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        final int action = e.getAction();

        if (action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_MOVE) {
            final int x = (int) e.getX();
            final int y = (int) e.getY();
            final long now = SystemClock.uptimeMillis();

            if (now - lastMoved > TIME_STEP) {
                if (x < 100) {
                    move(Direction.LEFT);
                } else if (x > screenWidth - 100) {
                    move(Direction.RIGHT);
                } else if (y < 100) {
                    move(Direction.UP);
                } else if (y > screenHeight - 100) {
                    move(Direction.DOWN);
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            stop();
        }

        return true;
    }
}
