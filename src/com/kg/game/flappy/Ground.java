package com.kg.game.flappy;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Ground {

	public Sprite g1, g2;
	private boolean isMoving;

	public Ground() {
		
		isMoving = false;
	
		g1 = new Sprite(0, 0, A.bg_front_TR.getWidth() * A.CH / A.bg_front_TR.getHeight(), A.CH, A.bg_front_TR, A.vbom);
		g2 = new Sprite(g1.getWidth(), 0, A.bg_front_TR.getWidth() * A.CH / A.bg_front_TR.getHeight(), A.CH, A.bg_front_TR, A.vbom);

		//g2.setFlipped(true, false);

		IUpdateHandler uh = new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (!isMoving) return;

				g1.setX(g1.getX() - A.settings.bg_groundSpeed);
				g2.setX(g2.getX() - A.settings.bg_groundSpeed);
				
				if (g1.getX() <= -g1.getWidth()) { 
					g1.setX(g1.getX() + g1.getWidth());
					g2.setX(g1.getX() + g1.getWidth());
				}
				
				/*if (g2.getX() + g2.getWidth() <= A.CW) { 
					g1.setX(g1.getX() + g1.getWidth());
					g2.setX(g1.getX() + g2.getWidth());
				}*/
			}
			@Override
			public void reset() { }};

			g1.registerUpdateHandler(uh);
	}
	
	public void startMoving() {
		isMoving = true;
	}

	public void stopMoving() { 
		isMoving = false;
	}

	public void attachTo(Scene scene) {
		scene.attachChild(g1);
		scene.attachChild(g2);
	}

}
