package com.kg.game.flappycat;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;

import android.util.Log;

public class WallPair {
	
	public Wall upper, lower;
	public Line scoreLine;
	
	private IUpdateHandler uh;
	private float uy, ly;

	public WallPair(float x) {
		this.uy = A.CH / 2 - A.settings.wallSpaceV / 2 - A.settings.wallHeight;
		this.ly = A.CH / 2 + A.settings.wallSpaceV / 2;
		
		uy -= A.settings.groundHeight / 2;
		ly -= A.settings.groundHeight / 2;
		
		upper = new Wall(x, uy, true);
		lower = new Wall(x, ly, false);

		scoreLine = new Line(
				upper.getWidth() / 2, upper.getHeight(), 
				upper.getWidth() / 2, upper.getHeight() + A.settings.wallSpaceV, A.vbom);
		upper.attachChild(scoreLine);
		scoreLine.setVisible(false);
		
		uh = new IUpdateHandler(){
	        @Override
	        public void onUpdate(float pSecondsElapsed) { 
	        	
	        	if (getX() < - A.settings.wallWidth) {
					upper.body.setTransform(upper.body.getPosition().x + (A.settings.wallSpaceH * A.settings.wallN) / 32, 
							upper.body.getPosition().y, 
							upper.body.getAngle());
					lower.body.setTransform(lower.body.getPosition().x + (A.settings.wallSpaceH * A.settings.wallN) / 32, 
							lower.body.getPosition().y, 
							lower.body.getAngle());

					setRandomDY();
				}
	        }
			@Override
			public void reset() {
			}};
	}

	public void attachTo(Scene scene) {
		scene.attachChild(upper);
		scene.attachChild(lower);
	}
	
	private float getDY() {
		return A.settings.wallDVMax - A.ran.nextInt((int)A.settings.wallDVMax * 2);
	}
	
	public void setDY(float dy) {
		upper.body.setTransform(upper.body.getPosition().x, (uy + A.settings.wallHeight / 2 + dy) / 32, upper.body.getAngle());
		lower.body.setTransform(lower.body.getPosition().x, (ly + A.settings.wallHeight / 2 + dy) / 32, lower.body.getAngle());
	}
	
	public void setRandomDY() {
		setDY(getDY());
		scoreLine.setPosition(upper.getWidth() / 2, upper.getHeight(), 
				upper.getWidth() / 2, upper.getHeight() + A.settings.wallSpaceV);
	}
	
	public void setX(float x) {
		upper.body.setTransform(x / 32 + A.settings.wallWidth / 2 / 32, upper.body.getPosition().y, upper.body.getAngle());
		lower.body.setTransform(x / 32 + A.settings.wallWidth / 2 / 32, lower.body.getPosition().y, lower.body.getAngle());
	}
	
	public float getX() {
		return upper.getX();
	}
	
	public void startMoving() {
		upper.body.setLinearVelocity(-A.settings.wallSpeed, 0);
		lower.body.setLinearVelocity(-A.settings.wallSpeed, 0);

		upper.registerUpdateHandler(uh);
	}
	
	public void stopMoving() {
		upper.body.setLinearVelocity(0, 0);
		lower.body.setLinearVelocity(0, 0);

		//upper.body.setLinearDamping(5);
		//lower.body.setLinearDamping(5);
		
		upper.unregisterUpdateHandler(uh);
	}
	
}
