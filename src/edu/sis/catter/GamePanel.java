package edu.sis.catter;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import edu.sis.catter.model.Cat;
import edu.sis.catter.model.Direction;
import edu.sis.catter.model.GameObject;
import edu.sis.catter.model.Lane;
import edu.sis.catter.model.Vect2D;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
	// 480x800

	private static final int MAX_OBSTACLES = 25;

	private static Random random = new Random();

	private MainLoop mainLoop;
	private Thread gameThread;

	private int obstacleVelocity = 3;

	private Cat cat;
	private LinkedList<Lane> lanes = new LinkedList<Lane>();

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
		Bitmap res = BitmapFactory.decodeResource(getResources(),
				R.drawable.cat);
		cat = new Cat(res, new Vect2D(getWidth() / 2, getHeight() - res.getHeight()), getWidth(),
				getHeight());
		for (int i = 1; i < 11; i++) {
			lanes.add(new Lane(BitmapFactory.decodeResource(getResources(),
					R.drawable.enemy), i * 40, getWidth(), obstacleVelocity,
					(i / 2) % 2 == 0 ? Direction.RIGHT : Direction.LEFT, 10));
		}

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
		for(Lane lane : lanes) {
			if(random.nextDouble() < 0.05) {
				lane.createCar();
			}
		}
		for (Lane lane : lanes) {
			lane.updateCars();
		}
	}

	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		cat.render(canvas);
		for (Lane lane : lanes) {
			lane.renderCars(canvas);
		}
	}

	public MainLoop getMainLoop() {
		return mainLoop;
	}

	private int getLaneY(int lane) {
		return cat.JUMP_STEP * lane;
	}
}
