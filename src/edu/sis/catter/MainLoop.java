package edu.sis.catter;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.SurfaceHolder;


public class MainLoop implements Runnable {
    public static final int FRAMES_PER_SECOND = 30;
    public static final double MILISECONDS_PER_FRAME = 1000 / (double) FRAMES_PER_SECOND;

    private boolean running;
    private GamePanel game;
    private SurfaceHolder holder;

    public MainLoop(GamePanel game, SurfaceHolder holder) {
        this.game = game;
        this.holder = holder;
    }

    @Override
    public void run() {
        long timeBase;
        Canvas canvas;

        while (running) {
            timeBase = SystemClock.uptimeMillis();
            canvas = null;
            try {
                game.update();
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    game.render(canvas);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            long timeDiff = SystemClock.uptimeMillis() - timeBase;
            if (timeDiff < MILISECONDS_PER_FRAME) {
                SystemClock.sleep((long) (MILISECONDS_PER_FRAME - timeDiff));
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
