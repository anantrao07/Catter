package edu.sis.catter;

import java.util.LinkedList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import edu.sis.catter.model.Cat;
import edu.sis.catter.model.Direction;
import edu.sis.catter.model.Lane;
import edu.sis.catter.model.Vect2D;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    // 480x800
    private static final int MAX_TIME = 20 * 1000; // 20s
    private static final int OBSTACLE_VELOCITY = 3;

    private static Random random = new Random();

    private GameActivity gameActivity;
    private MainLoop mainLoop;
    private Thread gameThread;

    private long startTime;
    private int score;

    private Cat cat;
    private LinkedList<Lane> lanes = new LinkedList<Lane>();

    private Paint paint;

    public GamePanel(GameActivity context) {
        super(context);
        gameActivity = context;
        mainLoop = new MainLoop(this, getHolder());
        gameThread = new Thread(mainLoop);
        getHolder().addCallback(this);
        setFocusable(true);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(25);
        paint.setAntiAlias(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap res = BitmapFactory.decodeResource(getResources(),
                R.drawable.cat);
        cat = new Cat(res, new Vect2D(getWidth() / 2, getHeight()
                - res.getHeight()), getWidth(), getHeight());
        for (int i = 1; i < 11; i++) {
            boolean b = (i / 2) % 2 == 0;
            lanes.add(new Lane(BitmapFactory.decodeResource(getResources(),
                    b ? R.drawable.car_left : R.drawable.car_right), i * 40,
                    getWidth(), OBSTACLE_VELOCITY, b ? Direction.RIGHT
                            : Direction.LEFT, 10));
        }

        mainLoop.setRunning(true);
        gameThread.start();
        startTime = SystemClock.uptimeMillis();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        killGameThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        cat.onTouchEvent(event);
        return true;
    }

    public void update() {
        cat.update();
        for (Lane lane : lanes) {
            if (random.nextDouble() < 0.05) {
                lane.createCar();
            }
        }
        for (Lane lane : lanes) {
            lane.updateCars();
        }

        for (Lane lane : lanes) {
            if (lane.carIntersect(cat) == true) {
                mainLoop.setRunning(false);
                lose();
                break;
            }
        }

        final long now = SystemClock.uptimeMillis();
        long time = (now - startTime);
        score = (int) ((MAX_TIME - time) / 100);
        if (time > MAX_TIME) {
            mainLoop.setRunning(false);
            lose();
        }

        if (cat.getPosition().y == 0) {
            mainLoop.setRunning(false);
            win();
        }
    }

    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        cat.render(canvas);
        for (Lane lane : lanes) {
            lane.renderCars(canvas);
        }

        canvas.drawText("Time left: " + String.valueOf(score), 10, 30, paint);
    }

    private void lose() {
        gameActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                showInfoDialog(R.string.lose);
            }
        });
    }

    private void win() {
        gameActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (gameActivity.isScoreBigEnough(score)) {
                    final EditText input = new EditText(gameActivity);
                    new AlertDialog.Builder(gameActivity)
                            .setTitle(R.string.win)
                            .setView(input)
                            .setMessage(R.string.enter_name)
                            .setNeutralButton("ok",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            gameActivity.addHighScore(input
                                                    .getText().toString(),
                                                    score);
                                            Intent mainScreen = new Intent(
                                                    gameActivity
                                                            .getApplicationContext(),
                                                    MainMenuActivity.class);
                                            killGameThread();
                                            gameActivity
                                                    .startActivity(mainScreen);
                                        }
                                    }).show();
                } else {
                    showInfoDialog(R.string.win_but_fail);
                }
            }
        });
    }

    private void killGameThread() {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    private void showInfoDialog(int res_title) {
        new AlertDialog.Builder(gameActivity).setTitle(res_title)
                .setNeutralButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mainScreen = new Intent(gameActivity
                                .getApplicationContext(),
                                MainMenuActivity.class);
                        killGameThread();
                        gameActivity.startActivity(mainScreen);
                    }
                }).show();
    }

    public MainLoop getMainLoop() {
        return mainLoop;
    }
}
