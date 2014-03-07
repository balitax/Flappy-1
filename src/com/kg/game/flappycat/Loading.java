package com.kg.game.flappycat;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

public class Loading extends Rectangle{

	private Sprite loadingSprite;
	
	public Loading() {
		super(0, 0, A.CW, A.CH, A.vbom);
		
		this.setColor(Color.WHITE);
		loadingSprite = new Sprite(0, 0, 256, 256, A.loading_TR, A.vbom);
		loadingSprite.setPosition((this.getWidth() - loadingSprite.getWidth()) / 2, (this.getHeight() - loadingSprite.getHeight()) / 2);
		this.attachChild(loadingSprite);
		
		setVisible(false);
	}
	
	public void show(float seconds) {
		setVisible(true);
		this.registerUpdateHandler(new TimerHandler(seconds, false, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				setVisible(false);
			}}));
	}
	
}
