package com.kg.game.flappycat;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Wall extends Sprite {

	public Body body;

	private void createPhysics() {	
		body = PhysicsFactory.createBoxBody(A.pw, this, BodyType.KinematicBody, A.settings.wallFD);
		body.setUserData(A.settings.str_dangerousWall);
		A.pw.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
	}
	
	public Wall(float x, float y, boolean flipped) {
		super(x, y, A.settings.wallWidth, A.settings.wallHeight, A.wall_TR, A.vbom);
		createPhysics();
		if (flipped) 
			this.setFlipped(false, true);
			//this.setRotation(180);
			//body.setTransform(body.getPosition().x, body.getPosition().y, (float)Math.PI);
	}
	
}
