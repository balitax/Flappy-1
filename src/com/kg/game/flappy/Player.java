package com.kg.game.flappy;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;

public class Player extends AnimatedSprite {

	public Body body;
	
	private AnimatedSprite dizzy;
	private boolean isDizzy = false;

	private void createPhysics() {
		body = PhysicsFactory.createCircleBody(A.pw, getX() + getWidth() / 2, getY() + getHeight() / 2, 
				A.settings.playerRadius, BodyType.DynamicBody, A.settings.playerFD);
		body.setUserData(A.settings.str_player);
		A.pw.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
		body.setBullet(true);
	}
	
	public Player(float x, float y) {
		super(x, y, A.settings.playerWidth, A.settings.playerHeight, A.player_TR, A.vbom);
		createPhysics();
		
		dizzy = new AnimatedSprite(0, 0, A.settings.dizzySize, A.settings.dizzySize / 2.7f, A.dizzy_TR, A.vbom);
		dizzy.animate(A.settings.durs);
		attachChild(dizzy);
		
		hideDizzy();
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		
		if (!isDizzy) return;
		dizzy.setRotation(-this.getRotation());
		float[] coordsThis = this.convertLocalToSceneCoordinates(this.getWidth() /2, this.getHeight() / 2);
		float[] coordsDizzy = this.convertSceneToLocalCoordinates(coordsThis[0], coordsThis[1] - 100);
		dizzy.setPosition(coordsDizzy[0] - dizzy.getWidth() / 2, coordsDizzy[1] - dizzy.getHeight() / 2);
	}
	
	public void animate() {
		super.animate(A.settings.durs, A.settings.frms);
	}
	
	public void reset() {
		body.setActive(false);
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
		body.setAngularVelocity(0);
		body.setAngularDamping(0);
		hideDizzy();
	}
	
	public void resetRotation() {
		body.setAngularVelocity(0);
		body.setAngularDamping(0);
	}
	
	public void showDizzy() {
		isDizzy = true;
		dizzy.setVisible(isDizzy);
	}
	
	public void hideDizzy() {
		isDizzy = false;
		dizzy.setVisible(isDizzy);
	}
	
	public void die() {
		showDizzy();
		stopAnimation();
		resetRotation();
		setCurrentTileIndex(3);
	}
	
	public void jump() {
		
		if (this.getY() < 0) return;
		body.setLinearVelocity(0, -A.settings.playerJumpForce);
		
		body.setTransform(body.getPosition(), (float)Math.toRadians(-30));
		body.setAngularVelocity(1);
		body.setAngularDamping(1);

		super.animate(A.settings.durs, A.settings.frms, false);
	}
	
}
