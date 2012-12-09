package edu.sis.catter.model;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Lane {
	private LinkedList<GameObject> cars;
	private Bitmap bitmap;
	private int y;
	private int width;
	private int velocity;
	private int carLimits;
	private Direction direction;
	private GameObject lastCar = null;
	private Vect2D carStartPosition;
	private Vect2D carSpeed;

	public Lane(Bitmap bitmap, int y, int width, int velocity, Direction dir,
			int carLimits) {
		this.bitmap = bitmap;
		this.y = y;
		this.width = width;
		this.velocity = velocity;
		this.direction = dir;
		this.carLimits = carLimits;

		init();
	}

	public void createCar() {
		if (canCreateCar() == false) {
			return;
		}
		GameObject car;
		car = new GameObject(bitmap, carStartPosition);
		car.setSpeed(carSpeed);
		lastCar = car;
		cars.add(car);
	}

	public void updateCars() {
		for (GameObject car : cars) {
			car.update();
		}
		
		destroyCars();
	}

	public void renderCars(Canvas canvas) {
		for (GameObject car : cars) {
			car.render(canvas);
		}
	}

	private void destroyCars() {
		LinkedList<GameObject> newCars = new LinkedList<GameObject>();

		for (GameObject car : cars) {
			if (direction == Direction.RIGHT || direction == Direction.UP) {
				if (car.position.x < width) {
					newCars.add(car);
				} else {
					car.setSpeed(new Vect2D(0, 0));
				}
			} else {
				if (car.position.x > 0 - bitmap.getWidth()) {
					newCars.add(car);
				} else {
					car.setSpeed(new Vect2D(0, 0));
				}
			}
		}
		cars = newCars;
	}

	private boolean canCreateCar() {
		if (cars.size() > carLimits)
			return false;
		if (lastCar == null)
			return true;
		if (direction == Direction.RIGHT || direction == Direction.UP) {
			if (lastCar.position.x > bitmap.getWidth() * 3) {
				return true;
			}
		} else {
			if (lastCar.position.x < width - bitmap.getWidth() * 4) {
				return true;
			}
		}
		return false;
	}

	private void init() {
		cars = new LinkedList<GameObject>();
		
		switch (direction) {
		case RIGHT:
		case UP:
			carStartPosition = new Vect2D(0 - bitmap.getWidth(), y);
			carSpeed = new Vect2D(velocity, 0);
			break;
		case LEFT:
		case DOWN:
			carStartPosition = new Vect2D(width, y);
			carSpeed = new Vect2D(-velocity, 0);
			break;
		}
	}
	
	public boolean carIntersect(GameObject obj) {
		for(GameObject car : cars) {
			if(car.intersect(obj) == true) {
				return true;
			}
		}
		return false;
	}

	public LinkedList<GameObject> getObjects() {
		return cars;
	}

	public int getY() {
		return y;
	}
}
