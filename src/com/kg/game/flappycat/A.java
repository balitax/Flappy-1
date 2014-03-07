package com.kg.game.flappycat;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.modifier.ease.EaseBounceOut;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.google.android.gms.games.Games;
import com.kg.game.flappycat.Highscores.Medal;

import com.google.android.gms.common.api.*;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
//com.google.example.games.basegameutils.BaseGameActivity.
public class A extends SimpleBaseGameActivity implements IOnSceneTouchListener {
	//public InterstitialAd inter;
	//public AdView banner;
	public final String LEADERBOARD_ID = "CgkI1cCb0OEIEAIQAA";
	
	// GAME STATE
	private int score; 
	public enum State { MENU, GAME, HS }
	private boolean canPushButton = false;

	// BASE
	public static int CW, CH;
	public static VertexBufferObjectManager vbom;
	public static SimpleBaseGameActivity activity;
	public static Scene scene;
	public static HUD hud;
	public static Camera camera;
	public static PhysicsWorld pw;
	public static State currentState;
	public static Settings settings;
	public static Random ran;

	// RESOURCES
	public static BuildableBitmapTextureAtlas game_TA;
	public static TiledTextureRegion player_TR;
	public static TiledTextureRegion dizzy_TR;
	public static TiledTextureRegion medals_TR;
	public static TextureRegion wall_TR;
	public static TextureRegion tap_to_play_TR;
	public static TextureRegion button_play_TR;
	public static TextureRegion button_hs_TR;
	public static TextureRegion game_over_bg_TR;
	public static TextureRegion game_over_text_TR;
	public static TextureRegion title_TR;
	public static TextureRegion particle_TR;
	public static TextureRegion new_TR;
	public static TextureRegion dir_TR;
	public static TextureRegion loading_TR;

	public static TextureRegion bg_front_TR, bg_mid_TR, bg_back_TR;

	// OBJECTS
	public static Player player;
	ArrayList<WallPair> wps;
	SpriteParticleSystem ps;
	IUpdateHandler scoreUpdateHandler;
	
	Sprite sprite_tapToPlay, sprite_buttonPlay, sprite_buttonHS, sprite_gameOverText, 
	sprite_gameOver, sprite_titleText, sprite_new, sprite_dir;
	Text text_score, text_hs_curr, text_hs_best; 
	Rectangle rect_menu, rect_gg, rect_buttons;
	Font font_small, font_normal, font_big;
	TiledSprite sprite_medal;
	Loading loading;
	
	AutoParallaxBackground bg;
	Ground gr;
	Highscores hs;
	Medal medal;
	
	GameHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		mHelper.enableDebugLog(true, "GameHelper");
		
		super.onCreate(savedInstanceState);

		GameHelperListener listener = new GameHelper.GameHelperListener() {
			@Override
			public void onSignInSucceeded() {        }
			@Override
			public void onSignInFailed() {        }
		};
		mHelper.setup(listener);
		
		
	}

	@Override
	protected void onStart() {
	    super.onStart();
	    mHelper.onStart(this);
	}

	@Override
	protected void onStop() {
	    super.onStop();
	    mHelper.onStop();
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
	    super.onActivityResult(request, response, data);
	    mHelper.onActivityResult(request, response, data);
	}
	
	@Override
	protected Scene onCreateScene() {
		
		initBase();
		
		loading = new Loading();
		hud.attachChild(loading);
		loading.setZIndex(100);
		loading.show(3);
		
		font_normal.prepareLetters("1234567890".toCharArray());
		font_big.prepareLetters("1234567890".toCharArray());
		
		hs = new Highscores();
		createPhysics();

		scoreUpdateHandler = new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) { 
				for (WallPair wp : wps) 
					if (A.player.collidesWith(wp.scoreLine)) {
						wp.scoreLine.setPosition(0, 0, 0, 0);
		                addScore();
		            }
			}

			@Override
			public void reset() {
			}
		};
		
		scene.setOnSceneTouchListener(this);
		scene.setTouchAreaBindingOnActionDownEnabled(true);


		createWallPairs();
		createBG();
		//createParticleSystem();
		createObjects();
		createGrounds();
		//createParticleSystemFront();

		//DebugRenderer debug = new DebugRenderer(pw, vbom); scene.attachChild(debug);
		//mEngine.registerUpdateHandler(new FPSLogger());
		hud.sortChildren();
		
		setGameState(State.MENU);
		
		return scene;
	}
	
	
	/*@Override
	protected void onSetContentView() {
		
		// BANNER
		
		FrameLayout frameLayout = new FrameLayout(this);
		
		FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		
		FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		
		this.mRenderSurfaceView = new RenderSurfaceView(this);
		mRenderSurfaceView.setRenderer(mEngine, this);
		
		banner = new AdView(this);
		banner.setAdUnitId("ca-app-pub-9768967449520031/7829691908");
		banner.setAdSize(AdSize.SMART_BANNER);
		banner.refreshDrawableState();
		banner.setVisibility(AdView.VISIBLE);
		banner.loadAd(new AdRequest.Builder().addTestDevice("0A9A2EE825DAFD9E6D9D496FAB5AA4A7").build());
		
		RelativeLayout.LayoutParams surfaceViewLayoutParams = new RelativeLayout.LayoutParams(
				BaseGameActivity.createSurfaceViewLayoutParams());
		frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
		frameLayout.addView(banner, adViewLayoutParams);
		this.setContentView(frameLayout, frameLayoutLayoutParams);
		
		// INTER
		
		inter = new InterstitialAd(this);
		inter.setAdUnitId("ca-app-pub-9768967449520031/4736624705");
		inter.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				inter.show(); 
			}   
		});
		inter.loadAd(new AdRequest.Builder()
		.addTestDevice("0A9A2EE825DAFD9E6D9D496FAB5AA4A7")
		.build());
	}*/
	
	private void setScore(Integer sc) {
		text_score.setText("" + sc);
		setCenterPositionX(text_score);
	}
	
	private void addScore() {
		score ++;
		setScore(score);
	}

	private void resetScore() {
		score = 0;
		setScore(score);
	}

	private void setButtonsVisible(boolean b) {
		if (b) {
			canPushButton = false;
			
			rect_buttons.setY(CH);
			rect_buttons.registerEntityModifier(new MoveModifier(
					0.5f, 
					rect_buttons.getX(), rect_buttons.getX(),
					rect_buttons.getY(), settings.buttons_y, new IEntityModifierListener() {
						@Override
						public void onModifierStarted(IModifier<IEntity> pModifier,
								IEntity pItem) {}

						@Override
						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {
							canPushButton = true;
						}},
					EaseBackOut.getInstance()));
		} else {
			rect_buttons.setY(-1000);
		}
	}


	private void hideObjects() {
		setButtonsVisible(false);
		setTapToPlayVisible(false);
		sprite_gameOver.setVisible(false);
		player.setVisible(false);

		sprite_titleText.setVisible(false);
		text_score.setVisible(false);
	}

	private void buttonPlayPushed() {
		setGameState(State.GAME);
	}

	private void buttonHSPushed() {
		//hs.resetHS();
		int REQUEST_LEADERBOARD = 1;
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), LEADERBOARD_ID), REQUEST_LEADERBOARD);
	}
	
	private void recordHS(int score) {
		Games.Leaderboards.submitScore(mHelper.getApiClient(), "CgkI1cCb0OEIEAIQAA", score);
	}

	private void setCenterPosition(RectangularShape s) {
		s.setPosition((CW - s.getWidth()) / 2, (CH - s.getHeight()) / 2);
	}
	
	private void setCenterPositionX(RectangularShape s) {
		s.setPosition((CW - s.getWidth()) / 2, s.getY());
	}
	
	private void setCenterPositionY(RectangularShape s) {
		s.setPosition(s.getX(), (CH - s.getHeight()) / 2);
	}
	
	private void setCenterPositionYInShape(RectangularShape s, RectangularShape sc) {
		s.setPosition(s.getX(), (sc.getHeight() - s.getHeight()) / 2);
	}

	private void setObjectPositions() {

		sprite_dir.setPosition(
				CW / 2 - settings.playerWidth / 2 - settings.playerXOffset + (settings.playerWidth - sprite_dir.getWidth()) / 2, 
				CH / 2 - settings.playerHeight / 2 - 30);
		
		rect_buttons.setPosition(0, settings.buttons_y);
		
		sprite_buttonPlay.setPosition(CW / 2 - sprite_buttonPlay.getWidth(), 0);
		sprite_buttonHS.setPosition(CW / 2, 0);
		setCenterPositionYInShape(sprite_buttonPlay, rect_buttons);
		setCenterPositionYInShape(sprite_buttonHS, rect_buttons);
		
		setCenterPosition(sprite_tapToPlay);
		setCenterPosition(sprite_gameOver);

		setCenterPositionX(sprite_titleText);
		sprite_titleText.setY(200);

		setCenterPositionX(text_score);
		text_score.setY(200);

		sprite_gameOverText.setPosition((sprite_gameOver.getWidth() - sprite_gameOverText.getWidth()) / 2, settings.gameOverText_y);
		text_hs_curr.setPosition(55, 200);
		text_hs_best.setPosition(sprite_gameOver.getWidth() / 2 + 85, text_hs_curr.getY());
		
		sprite_medal.setY(text_hs_curr.getY() + (text_hs_curr.getHeight() - sprite_medal.getHeight()) / 2);
		sprite_new.setY(text_hs_best.getY());

	}

	private void setScoreVisible(boolean b) {
		text_score.setVisible(b);
	}

	private void setTapToPlayVisible(boolean b) {
		sprite_tapToPlay.setVisible(b);
		sprite_dir.setVisible(b);
	}
	
	private void createObjects() {

		// SPRITES
		sprite_medal = new TiledSprite(0, 0, settings.medalSize, settings.medalSize, medals_TR, vbom);
		sprite_tapToPlay = new Sprite(0, 0, 512, 512, tap_to_play_TR, vbom);
		sprite_buttonPlay = new Sprite(0, 0, settings.button_width, settings.button_height, button_play_TR, vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
			{
				if (pSceneTouchEvent.isActionDown())
				{
					if (canPushButton)
						buttonPlayPushed();
				}
				return true;
			}};
			sprite_buttonHS = new Sprite(0, 0, settings.button_width, settings.button_height, button_hs_TR, vbom){
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
				{
					if (pSceneTouchEvent.isActionDown())
					{
						if (canPushButton)
							buttonHSPushed();
					}
					return true;
				}};

				scene.registerTouchArea(sprite_buttonPlay);
				scene.registerTouchArea(sprite_buttonHS);
				
				sprite_new = new Sprite(0, 0, settings.new_width, settings.new_width / 2, new_TR, vbom);
				sprite_gameOver = new Sprite(0, 0, CW, CW / 2, game_over_bg_TR, vbom);
				sprite_gameOverText = new Sprite(0, 0, CW, settings.gameOverTextHeight, game_over_text_TR, vbom);
				sprite_titleText = new Sprite(0, 0, 600, 170, title_TR, vbom);

				player = new Player((CW - settings.playerWidth) / 2, (CH - settings.playerHeight) / 2);
				player.body.setActive(false);
				sprite_dir = new Sprite(0, 0, 50, 50, dir_TR, vbom);
				
				hud.attachChild(sprite_gameOver);
				hud.attachChild(sprite_titleText);
				sprite_gameOver.attachChild(sprite_gameOverText);
				sprite_gameOver.attachChild(sprite_new);
				sprite_gameOver.attachChild(sprite_medal);
				
				hud.attachChild(sprite_tapToPlay);
				hud.attachChild(sprite_dir);
				
				rect_buttons = new Rectangle(0, 0, CW, CW / 4, vbom);
				rect_buttons.setAlpha(0);
				rect_buttons.attachChild(sprite_buttonPlay);
				rect_buttons.attachChild(sprite_buttonHS);
				
				hud.attachChild(rect_buttons);

				scene.attachChild(player);
				

				// TEXT


				/*text_hs_curr = new ShadowText(0, 0, font_normal, "123", settings.scoreMaxLetters, 
						settings.color_score_hs, settings.color_scoreShadow_hs, settings.shadowOffsetSmall);
				text_hs_best = new ShadowText(0, 0, font_normal, "456", settings.scoreMaxLetters, 
						settings.color_score_hs, settings.color_scoreShadow_hs, settings.shadowOffsetSmall);
				
				text_score = new ShadowText(0, 0, font_big, "0", settings.scoreMaxLetters, 
						settings.color_score, settings.color_scoreShadow, settings.shadowOffsetBig);*/
				
				text_hs_curr = new Text(0, 0, font_normal, "0", settings.scoreMaxLetters, vbom);
				text_hs_curr.setColor(settings.color_score_hs);
				
				text_hs_best = new Text(0, 0, font_normal, "0", settings.scoreMaxLetters, vbom);
				text_hs_best.setColor(settings.color_score_hs);
				
				text_score = new Text(0, 0, font_big, "0", settings.scoreMaxLetters, vbom);
				text_score.setColor(settings.color_score);

				hud.attachChild(text_score);

				sprite_gameOver.attachChild(text_hs_curr);
				sprite_gameOver.attachChild(text_hs_best);

				setObjectPositions();
				hideObjects();

				sprite_gameOver.setCullingEnabled(true);
				sprite_gameOverText.setCullingEnabled(true);
				sprite_titleText.setCullingEnabled(true);
				sprite_tapToPlay.setCullingEnabled(true);
				sprite_buttonPlay.setCullingEnabled(true);
				sprite_buttonHS.setCullingEnabled(true);		
				text_score.setCullingEnabled(true);
				text_hs_curr.setCullingEnabled(true);
				text_hs_best.setCullingEnabled(true);

				/*sprite_tapToPlay.setZIndex(0);
				sprite_buttonPlay.setZIndex(0);
				sprite_buttonHS.setZIndex(0);*/
	}

	private void setGameState(State state) {

		currentState = state;
		switch (state) {

		case MENU:
			player.setScale(3);
			player.animate();
			startMovingBG();

			player.setVisible(true);
			setButtonsVisible(true);
			sprite_titleText.setVisible(true);
			break;

		case GAME:
			player.setScale(1);
			resetScore();

			player.animate();
			resetGame();
			startMovingBG();

			setButtonsVisible(false);
			sprite_titleText.setVisible(false);
			sprite_gameOver.setVisible(false);

			setScoreVisible(true);
			setTapToPlayVisible(true);
			
			break;

		case HS:
			player.die();
			medal = hs.addHS(score);
			
			switch(medal) {
			case GOLD_OLD: case GOLD_NEW:
				sprite_medal.setCurrentTileIndex(0);
				break;
			case SILVER:
				sprite_medal.setCurrentTileIndex(1);
				break;
			case BRONZE:
				sprite_medal.setCurrentTileIndex(2);
				break;
			case NO_MEDAL:
				sprite_medal.setCurrentTileIndex(3);
				break;
			}
			
			text_hs_curr.setText("" + score);
			text_hs_best.setText("" + hs.r1);
			
			sprite_medal.setX(text_hs_curr.getX() + text_hs_curr.getWidth() + 20);
			
			if (medal == Medal.GOLD_NEW) {
				sprite_new.setVisible(true);
				sprite_new.setX(text_hs_best.getX() + text_hs_best.getWidth());
			} else 
				sprite_new.setVisible(false);

			setScoreVisible(false);
			
			recordHS(score);
			
			scene.unregisterUpdateHandler(scoreUpdateHandler);
			
			scene.registerUpdateHandler(new TimerHandler(0.1f, false, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					stopMovingWalls();
					stopMovingBG();
				}}));
			scene.registerUpdateHandler(new TimerHandler(1f, false, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					animateHS();
				}}));
			break;
		}
	}

	private void animateHS() {
		sprite_gameOverText.setY(-sprite_gameOver.getY() - settings.gameOverTextHeight);
		sprite_gameOverText.registerEntityModifier(new MoveModifier(
				1, 
				sprite_gameOverText.getX(), sprite_gameOverText.getX(),
				sprite_gameOverText.getY(), settings.gameOverText_y, EaseBounceOut.getInstance()));
		
		
		
		sprite_gameOver.setX(-sprite_gameOver.getWidth());
		sprite_gameOver.setVisible(true);
		sprite_gameOver.registerEntityModifier(new MoveModifier(
				1, 
				sprite_gameOver.getX(), 0,
				sprite_gameOver.getY(), sprite_gameOver.getY(), 
				new IEntityModifierListener() {
					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {}

					@Override
					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						setButtonsVisible(true);
					}},
				EaseBackOut.getInstance()));
		
		
	}

	private void resetGame() {
		resetScore();

		player.reset();
		player.body.setTransform(CW / 2 / 32 - settings.playerXOffset / 32, CH / 2/ 32, 0);

		for (int i = 0; i < wps.size(); i ++) {
			wps.get(i).setX(settings.wallStartX + settings.wallSpaceH * i);
			wps.get(i).setRandomDY();

			// TEST FOR HARDNESS
			//wps.get(0).setDY(settings.wallDVMax);
			//wps.get(1).setDY(-settings.wallDVMax);
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene sc, TouchEvent te) {
		if (currentState == State.GAME) 
			if (te.isActionDown()) {
				if (sprite_tapToPlay.isVisible()) {
					scene.registerUpdateHandler(scoreUpdateHandler);
					player.body.setActive(true);
					startMovingWalls();
					player.stopAnimation();
					setTapToPlayVisible(false);
					player.jump();
				} else {
					player.jump();
				}
			}
		return false;
	}

	private void createWallPairs() {
		wps = new ArrayList<WallPair>();
		for (int i = 0; i < settings.wallN; i ++) { 
			WallPair wp = new WallPair(settings.wallStartX);
			wps.add(wp);
			wp.attachTo(scene);
		}
	}

	private void createGrounds() {
		Line l;
		Body lb;

		l = new Line(0, CH - settings.groundHeight, CW, CH - settings.groundHeight, vbom);
		l.setAlpha(0);
		lb = PhysicsFactory.createLineBody(A.pw, l, A.settings.groundFD);
		lb.setUserData(settings.str_dangerousWall);
		scene.attachChild(l);
		
		l = new Line(0, 0, 0, CH, vbom);
		l.setAlpha(0);
		lb = PhysicsFactory.createLineBody(A.pw, l, A.settings.groundFD);
		scene.attachChild(l);
		
		l = new Line(CW, 0, CW, CH, vbom);
		l.setAlpha(0);
		lb = PhysicsFactory.createLineBody(A.pw, l, A.settings.groundFD);
		scene.attachChild(l);
		
		gr = new Ground();
		gr.attachTo(scene);
	}

	private void startMovingWalls() {
		for (WallPair wp : wps) wp.startMoving();
	}

	private void stopMovingWalls() {
		for (WallPair wp : wps) wp.stopMoving();
	}

	private void gameOver() {
		if (currentState == State.GAME) 
			setGameState(State.HS);
	}

	@Override
	public Engine onCreateEngine(EngineOptions eo) {
		Engine engine = new Engine(eo);
		//Engine engine = new LimitedFPSEngine(eo, 60);
		//Engine engine = new FixedStepEngine(eo, 60);
		return engine;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		defineDimensions();
		camera = new Camera(0, 0, CW, CH);
		EngineOptions eo = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CW, CH), camera);
		return eo;
	}

	private void loadFonts() {
		String fontName = "font.ttf";

		/*ITexture finalfont;

		finalfont = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font_normal = FontFactory.createStrokeFromAsset(this.getFontManager(), finalfont, this.getAssets(), 
				fontName, 90, true, android.graphics.Color.WHITE, 1, android.graphics.Color.WHITE);
		font_normal.load();

		finalfont = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font_big = FontFactory.createStrokeFromAsset(this.getFontManager(), finalfont, this.getAssets(), 
				fontName, 120, true, android.graphics.Color.WHITE, 1, android.graphics.Color.WHITE);
		font_big.load();*/
		
		ITexture fontTexture; 
		
		fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		font_normal = FontFactory.createStrokeFromAsset(this.getFontManager(), fontTexture, this.getAssets(), fontName, 90, true, Color.WHITE, 6, Color.BLACK);
		font_normal.load();
		
		fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		font_big = FontFactory.createStrokeFromAsset(this.getFontManager(), fontTexture, this.getAssets(), fontName, 120, true, Color.WHITE, 8, Color.BLACK);
		font_big.load();
	}

	private void loadTextures() {
		game_TA = new BuildableBitmapTextureAtlas(getTextureManager(), 1024 * 4, 1024 * 4, TextureOptions.DEFAULT);

		player_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(game_TA, this, "foxy.png", 2, 2);
		dizzy_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(game_TA, this, "dizzy.png", 6, 1);
		medals_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(game_TA, this, "medals.png", 2, 2);
		wall_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "wall.png");
		tap_to_play_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "tap_to_play.png");
		button_play_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "button_play.png");
		button_hs_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "button_hs.png");
		game_over_bg_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "game_over_bg.png");
		game_over_text_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "game_over_text.png");
		title_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "title.png");
		bg_front_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "bg_front.png");
		bg_mid_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "bg_mid.png");
		bg_back_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "bg_back.png");
		particle_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "particle.png");
		new_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "new.png");
		dir_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "dir.png");
		loading_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "loading.png");
		
		try {
			game_TA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		game_TA.load();
	}

	@Override
	protected void onCreateResources() {
		loadFonts();
		loadTextures();
	}

	private void createPhysics() {
		pw = new MaxStepPhysicsWorld(settings.sps, new Vector2(0, settings.gravity), false);
		//pw = new FixedStepPhysicsWorld(settings.sps, new Vector2(0, settings.gravity), false);
		scene.registerUpdateHandler(pw);

		pw.setContactListener(new ContactListener()
		{
			@Override
			public void beginContact(Contact contact)
			{
				gameOver();
			}

			@Override
			public void endContact(Contact contact)
			{

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold)
			{

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse)
			{

			}
		});
	}

	private void createParticleSystem() {
		ps = new SpriteParticleSystem(
				new RectangleParticleEmitter(CW / 2, CH / 2, CW * 2, CH), 
				5, 10, 50, particle_TR, vbom);
		
		ps.addParticleInitializer(new ColorParticleInitializer<Sprite>(1, 1, 1));
		ps.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0));
		ps.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		ps.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-200, -150, 100, 80));
		ps.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
		ps.addParticleInitializer(new ScaleParticleInitializer<Sprite>(1, 5));
		ps.addParticleInitializer(new ExpireParticleInitializer<Sprite>(6));
	
		ps.addParticleModifier(new AlphaParticleModifier<Sprite>(0, 1, 0, 1));
		ps.addParticleModifier(new AlphaParticleModifier<Sprite>(5, 6, 1, 0));

		scene.attachChild(ps);
	}
	
	private void createParticleSystemFront() {
		ps = new SpriteParticleSystem(
				new RectangleParticleEmitter(CW / 2, CH / 2, CW * 2, CH), 
				1, 5, 20, particle_TR, vbom);
		
		ps.addParticleInitializer(new ColorParticleInitializer<Sprite>(1, 1, 1));
		ps.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0));
		ps.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		ps.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-400, -300, 200, 150));
		ps.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
		ps.addParticleInitializer(new ScaleParticleInitializer<Sprite>(5, 10));
		ps.addParticleInitializer(new ExpireParticleInitializer<Sprite>(6));
	
		ps.addParticleModifier(new AlphaParticleModifier<Sprite>(0, 0.1f, 0, 1));
		ps.addParticleModifier(new AlphaParticleModifier<Sprite>(5, 6, 1, 0));

		scene.attachChild(ps);
	}
	
	private void createBG() {
		bg = new AutoParallaxBackground(1, 1, 1, 1);
		bg.attachParallaxEntity(new ParallaxEntity(-settings.bg_backSpeed, new Sprite(0, 0, bg_back_TR.getWidth() * CH / bg_back_TR.getHeight(), CH, bg_back_TR, vbom)));
		bg.attachParallaxEntity(new ParallaxEntity(-settings.bg_midSpeed, new Sprite(0, 0, bg_mid_TR.getWidth() * CH / bg_mid_TR.getHeight(), CH, bg_mid_TR, vbom)));
		
		scene.setBackground(bg);
	}

	private void startMovingBG() {
		bg.setParallaxChangePerSecond(1);
		gr.startMoving();
	}

	private void stopMovingBG() {
		bg.setParallaxChangePerSecond(0);
		gr.stopMoving();
	}

	private void initBase() {
		activity = this;
		scene = new Scene();
		hud = new HUD();
		camera.setHUD(hud);
		settings = new Settings();
		vbom = getVertexBufferObjectManager();
		ran = new Random();
	}

	private void defineDimensions() {
		CW = 720; 
		CH = 1280;

		final DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		int w = displayMetrics.widthPixels;
		int h = displayMetrics.heightPixels;
		if (CH > CW)
			CW = w * CH / h;
		else
			CH = h * CW / w;
	}

}
