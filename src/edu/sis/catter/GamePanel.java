package edu.sis.catter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import edu.sis.catter.model.Cat;
import edu.sis.catter.model.Vect2D;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainLoop mainLoop;
    private Thread gameThread;

    private Cat cat;

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
    }

    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        cat.render(canvas);
    }

    public MainLoop getMainLoop() {
        return mainLoop;
    }

}
