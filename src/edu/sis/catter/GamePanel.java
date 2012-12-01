package edu.sis.catter;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import edu.sis.catter.model.Cat;
import edu.sis.catter.model.GameObject;
import edu.sis.catter.model.Vect2D;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private static final int MAX_OBSTACLES = 15;

    private static Random random = new Random();

    private MainLoop mainLoop;
    private Thread gameThread;

    private Cat cat;
    private List<GameObject> obstacles = new LinkedList<GameObject>();

    public GamePanel(Context context) {
        super(context);
        mainLoop = new MainLoop(this, getHolder());
        gameThread = new Thread(mainLoop);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        cat = new Cat(BitmapFactory.decodeResource(getResources(),
                R.drawable.cat), new Vect2D(0, getHeight() / 2), getWidth(),
                getHeight());
        mainLoop.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        cat.onTouchEvent(event);
        return true;
    }

    public void update() {
        cat.update();
        createObstacles();
        for (GameObject o : obstacles) {
            o.update();
        }
        destroyObstacles();
    }

    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        cat.render(canvas);
        for (GameObject o : obstacles) {
            o.render(canvas);
        }
    }

    public MainLoop getMainLoop() {
        return mainLoop;
    }

    private void createObstacles() {
        if (random.nextDouble() < 0.1 && obstacles.size() < MAX_OBSTACLES) {
            Vect2D position = new Vect2D(40 + 20 * random.nextInt(36), 0);
            GameObject o = new GameObject(BitmapFactory.decodeResource(
                    getResources(), R.drawable.enemy), position);
            o.setSpeed(new Vect2D(0, 1 + random.nextInt(5)));
            obstacles.add(o);
        }
    }

    private void destroyObstacles() {
        List<GameObject> newList = new LinkedList<GameObject>();
        for (GameObject o : obstacles) {
            if (o.getPosition().y < getHeight()) {
                newList.add(o);
            }
        }
        obstacles = newList;
    }

}
