package com.kg.game.flappy;

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
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadScoresResult;
import com.google.android.gms.games.leaderboard.OnLeaderboardScoresLoadedListener;
import com.kg.game.flappy.Highscores.Medal;

import com.google.android.gms.common.api.*;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
//com.google.example.games.basegameutils.BaseGameActivity.
public class A extends SimpleBaseGameActivity implements IOnSceneTouchListener {
	//public InterstitialAd inter;
	//public AdView banner;
	public static final String LB_ID = "CgkI1cCb0OEIEAIQAA";
	
	// GAME STATE
	private Score score; 
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
	
	public static GameHelper gameHelper;
	public static UiLifecycleHelper uiHelper;

	// RESOURCES
	public static BuildableBitmapTextureAtlas game_TA;
	
	public static TiledTextureRegion 
	player_TR, 
	dizzy_TR,
	medals_TR,
	flags_TR;
	public static TextureRegion 
	wall_TR, 
	tap_to_play_TR, 
	button_play_TR, 
	button_hs_TR,
	button_show_TR,
	game_over_bg_TR, 
	game_over_text_TR, 
	title_TR, 
	particle_TR, 
	new_TR, 
	dir_TR, 
	loading_TR, 
	button_share_TR, 
	bg_front_TR, 
	bg_mid_TR, 
	bg_back_TR,
	best_TR,
	rank_TR;
	
	public static Font 
	font_small,
	font_normal, 
	font_big;

	// OBJECTS
	public static Player player;
	ArrayList<WallPair> wps;
	SpriteParticleSystem ps;
	IUpdateHandler scoreUpdateHandler;
	
	TimerHandler thStop, thAnimHS;
	MoveModifier mmGOText, mmGO, mmButtons;
	
	Sprite sprite_tapToPlay, sprite_buttonPlay, sprite_buttonHS, sprite_buttonShare, sprite_buttonShow, sprite_gameOverText, sprite_best, sprite_rank, 
	sprite_gameOver, sprite_titleText, sprite_new, sprite_dir;
	Text text_score, text_hs_curr, text_hs_best, text_rank_world, text_rank_friends; 
	Rectangle rect_menu, rect_gg, rect_buttons;
	TiledSprite sprite_medal;
	Loading loading;
	
	AutoParallaxBackground bg;
	Ground gr;
	Highscores hs;
	Medal medal;
	FlagsController fc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Session.StatusCallback callback = new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {}
		};
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
	    
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		
		super.onCreate(savedInstanceState);

		/*GameHelperListener listener = new GameHelper.GameHelperListener() {
			@Override
			public void onSignInSucceeded() {        }
			@Override
			public void onSignInFailed() {        }
		};
		gameHelper.setup(listener);*/
		
		
	}

	@Override
	protected void onStart() {
	    super.onStart();
	    gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
	    super.onStop();
	    gameHelper.onStop();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
	    super.onActivityResult(request, response, data);
	    
	    uiHelper.onActivityResult(request, response, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	    
	    gameHelper.onActivityResult(request, response, data);
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

		createScoreHandler();
		
		scene.setOnSceneTouchListener(this);
		scene.setTouchAreaBindingOnActionDownEnabled(true);


		createWallPairs();
		createBG();
		//createParticleSystem();
		createObjects();
		createGrounds();
		//createParticleSystemFront();
		
		initTimersAndModifiers();
		
		//DebugRenderer debug = new DebugRenderer(pw, vbom); scene.attachChild(debug);
		//mEngine.registerUpdateHandler(new FPSLogger());
		hud.sortChildren();
		
		score = new Score();
		fc = new FlagsController(gameHelper, score, wps);

		setGameState(State.MENU);
		
		return scene;
	}
	
	private void createScoreHandler() {
		scoreUpdateHandler = new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) { 
				for (WallPair wp : wps) 
					if (A.player.collidesWith(wp.scoreLine)) {
						wp.scoreLine.setPosition(-1000, -1000, -1000, -1000);
		                addScore();
		                fc.check();
		            }
			}

			@Override
			public void reset() {
			}
		};
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
		score.add();
		setScore(score.get());
	}

	private void resetScore() {
		score.reset();
		setScore(score.get());
	}

	private void setButtonsVisible(boolean b) {
		if (b) {
			canPushButton = false;
			
			rect_buttons.setY(CH);
			rect_buttons.registerEntityModifier(mmButtons);
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
		if (gameHelper.isSignedIn()) 
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), LB_ID), REQUEST_LEADERBOARD);
	}
	
	private void buttonSharePushed() {
		String name, desc, link;
		
		if (currentState == State.HS) 
			desc = "My highscore is " + hs.r1 + " in the Flappy Foxy game!";
		else
			desc = "Compete with me in the Flappy Foxy game!";	
		
		name = "Flappy Foxy";
		desc = "Compete with me in the Flappy Foxy game!";
		link = "https://play.google.com/store/apps/details?id=com.kg.game.flappy";

		if (FacebookDialog.canPresentShareDialog(getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
	        .setName(name)
	        .setDescription(desc)
			.setLink(link)
	        .build();
			uiHelper.trackPendingDialogCall(shareDialog.present());
		} else {
			Bundle params = new Bundle();
		    params.putString("name", name);
		    params.putString("description", desc);
		    params.putString("link", link);
		    WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(activity, Session.getActiveSession(), params))
		        .setOnCompleteListener(new OnCompleteListener() {
		            @Override
		            public void onComplete(Bundle values, FacebookException error) {
		            	if (error == null && values.getString("post_id") != null) {
		            		// SUCCESS
		            	} else {
		            		// FAIL
		            	}
		            }
		        }).build();
		    feedDialog.show();
		}
		
		
		
		
	}
	
	private void recordHS(int score) {
		if (gameHelper.isSignedIn()) Games.Leaderboards.submitScore(gameHelper.getApiClient(), LB_ID, score);
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
	
	private void setCenterPositionXInShape(RectangularShape s, RectangularShape sc) {
		s.setPosition((sc.getWidth() - s.getWidth()) / 2, s.getY());
	}


	private void setObjectPositions() {

		sprite_dir.setPosition(
				CW / 2 - settings.playerWidth / 2 - settings.playerXOffset + (settings.playerWidth - sprite_dir.getWidth()) / 2, 
				CH / 2 - settings.playerHeight / 2 - 30);
		
		rect_buttons.setPosition(0, settings.buttons_y);
		
		sprite_buttonPlay.setPosition(CW / 2 - sprite_buttonPlay.getWidth(), 0);
		sprite_buttonHS.setPosition(CW / 2, 0);
		sprite_buttonShare.setPosition(0, -settings.button_height);
		setCenterPositionYInShape(sprite_buttonPlay, rect_buttons);
		setCenterPositionYInShape(sprite_buttonHS, rect_buttons);
		setCenterPositionXInShape(sprite_buttonShare, rect_buttons);
		
		setCenterPosition(sprite_tapToPlay);
		setCenterPosition(sprite_gameOver);

		setCenterPositionX(sprite_titleText);
		sprite_titleText.setY(200);

		setCenterPositionX(text_score);
		text_score.setY(200);

		sprite_gameOverText.setPosition((sprite_gameOver.getWidth() - sprite_gameOverText.getWidth()) / 2, settings.gameOverText_y);
		text_hs_curr.setPosition(100, 200);
		text_hs_best.setPosition(sprite_gameOver.getWidth() / 2 + 100, text_hs_curr.getY());
		
		sprite_medal.setY(text_hs_curr.getY() + (text_hs_curr.getHeight() - sprite_medal.getHeight()) / 2);
		sprite_new.setY(text_hs_best.getY());
		
		sprite_best.setY(sprite_titleText.getY() + sprite_titleText.getHeight());
		
		sprite_rank.setY(sprite_best.getHeight());
		
		sprite_buttonShow.setPosition(-1000, sprite_titleText.getY() + sprite_titleText.getHeight());
	}
	
	private void switchShowBest(boolean showRanks) {
		if (showRanks) {
			// BEST
			player.setVisible(false);
			sprite_best.setX((CW - sprite_best.getWidth()) / 2);
			sprite_buttonShow.setX(-1000);
		} else {
			// SHOW
			player.setVisible(true);
			sprite_best.setX(-1000);
			sprite_buttonShow.setX((CW - sprite_buttonShow.getWidth()) / 2);
		}
	}
	
	private void switchShowBest() {
		if (sprite_best.getX() < 0) {
			switchShowBest(true);
		} else {
			switchShowBest(false);
		}
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
		
		sprite_best = new BestSprite(0, 0) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
			{
				if (pSceneTouchEvent.isActionDown())
				{
					if (sprite_rank.isVisible()) switchShowBest();
				}
				return true;
			}
		};
		sprite_rank = new RankSprite(0, 0) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
			{
				if (pSceneTouchEvent.isActionDown())
				{
					if (sprite_rank.isVisible()) switchShowBest();
				}
				return true;
			}
		};
		
		sprite_buttonShow = new Sprite(0, 0, settings.button_width, settings.button_height, button_show_TR, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
			{
				if (pSceneTouchEvent.isActionDown())
				{
					switchShowBest();
				}
				return true;
			}};
		
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
		sprite_buttonShare = new Sprite(0, 0, settings.button_width, settings.button_height, button_share_TR, vbom){
					@Override
					public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
					{
						if (pSceneTouchEvent.isActionDown())
						{
							if (canPushButton)
								buttonSharePushed();
						}
						return true;
					}};
		

				hud.registerTouchArea(sprite_best);
				hud.registerTouchArea(sprite_rank);
				hud.registerTouchArea(sprite_buttonShow);
				
				scene.registerTouchArea(sprite_buttonPlay);
				scene.registerTouchArea(sprite_buttonHS);
				scene.registerTouchArea(sprite_buttonShare);
				
				sprite_new = new Sprite(0, 0, settings.new_width, settings.new_width / 2, new_TR, vbom);
				sprite_gameOver = new Sprite(0, 0, CW, CW / 2, game_over_bg_TR, vbom);
				sprite_gameOverText = new Sprite(0, 0, CW, CW / 4, game_over_text_TR, vbom);
				sprite_titleText = new Sprite(0, 0, 600, 170, title_TR, vbom);

				player = new Player((CW - settings.playerWidth) / 2, (CH - settings.playerHeight) / 2);
				player.body.setActive(false);
				sprite_dir = new Sprite(0, 0, 50, 50, dir_TR, vbom);
				hud.attachChild(sprite_buttonShow);
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
				rect_buttons.attachChild(sprite_buttonShare);
				
				hud.attachChild(rect_buttons);
				hud.attachChild(sprite_best);
				sprite_best.attachChild(sprite_rank);
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
			
			player.animate();
			startMovingBG();
			
			sprite_best.setScale(0.8f);
			
			player.setScaleCenter(player.getWidth() / 2, 0.18f * player.getHeight());
			player.setScale(2.5f);
			//player.setScale(3f);
			
			player.setY(sprite_best.getY() + sprite_best.getHeight());
			
			player.setVisible(true);
			setButtonsVisible(true);
			sprite_titleText.setVisible(true);
			break;

		case GAME:
			player.setScale(1);
			player.animate();
			player.setVisible(true);
			
			resetGame();
			startMovingBG();
			
			sprite_buttonShow.setVisible(false);
			sprite_best.setVisible(false);
			setButtonsVisible(false);
			sprite_titleText.setVisible(false);
			sprite_gameOver.setVisible(false);

			setScoreVisible(true);
			setTapToPlayVisible(true);
			
			break;

		case HS:
			player.die();
			medal = hs.addHS(score.get());
			
			switch(medal) {
			case GOLD: case GOLD_NEW:
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
			
			text_hs_curr.setText("" + score.get());
			text_hs_best.setText("" + hs.r1);
			
			sprite_medal.setX(text_hs_curr.getX() + text_hs_curr.getWidth() + 20);
			
			if (medal == Medal.GOLD_NEW) {
				sprite_new.setVisible(true);
				sprite_new.setX(text_hs_best.getX() + text_hs_best.getWidth());
			} else 
				sprite_new.setVisible(false);

			setScoreVisible(false);
			
			recordHS(score.get());
			
			scene.unregisterUpdateHandler(scoreUpdateHandler);
			
			scene.registerUpdateHandler(thStop);
			scene.registerUpdateHandler(thAnimHS);
			break;
		}
	}
	
	private void initTimersAndModifiers() {
		thStop = new TimerHandler(0.1f, false, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				stopMovingWalls();
				stopMovingBG();
				scene.unregisterUpdateHandler(thStop);
				thStop.reset();
			}});
		
		thAnimHS = new TimerHandler(1f, false, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				animateHS();
				scene.unregisterUpdateHandler(thAnimHS);
				thAnimHS.reset();
			}});
		
		mmGOText = new MoveModifier(
				1, 
				sprite_gameOverText.getX(), sprite_gameOverText.getX(),
				-sprite_gameOver.getY() - settings.gameOverTextHeight, settings.gameOverText_y,
				EaseBounceOut.getInstance()) {
					@Override
					public void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						
						setButtonsVisible(true);
						
						sprite_gameOverText.clearEntityModifiers();
						mmGOText.reset();
					}};
		
		mmGO = new MoveModifier(
				1, 
				-sprite_gameOver.getWidth(), 0,
				sprite_gameOver.getY(), sprite_gameOver.getY(), 
				EaseBackOut.getInstance()) {
					@Override
					public void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);

						sprite_gameOver.clearEntityModifiers();
						mmGO.reset();
					}};
					
         mmButtons = new MoveModifier(
					0.5f, 
					rect_buttons.getX(), rect_buttons.getX(),
					CH, settings.buttons_y, 
					EaseBackOut.getInstance()) {
        	 @Override
				public void onModifierFinished(IEntity pItem) {
        		 	super.onModifierFinished(pItem);
        		 	
        		 	canPushButton = true;
        		 	
					rect_buttons.clearEntityModifiers();
					mmButtons.reset();
				}
         };
					
	}
	
	private void animateHS() {

		sprite_gameOverText.setY(-sprite_gameOver.getY() - settings.gameOverTextHeight);
		sprite_gameOver.setX(-sprite_gameOver.getWidth());
		
		sprite_gameOverText.registerEntityModifier(mmGOText);
		sprite_gameOver.registerEntityModifier(mmGO);
		
		sprite_gameOver.setVisible(true);
	
	}

	private void resetGame() {
		resetScore();

		player.reset();
		player.body.setTransform(CW / 2 / 32 - settings.playerXOffset / 32, CH / 2/ 32, 0);

		for (int i = 0; i < wps.size(); i ++) {
			wps.get(i).setX(settings.wallStartX + settings.wallSpaceH * i);
			wps.get(i).setRandomDY();
			wps.get(i).flag.hide();

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
					fc.resetFlags();
					fc.check();
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
		String fontNameBest = "fontBest.ttf";
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
		font_small = FontFactory.createStrokeFromAsset(this.getFontManager(), fontTexture, this.getAssets(), 
				fontNameBest, 40, true, android.graphics.Color.WHITE, 0.5f, android.graphics.Color.WHITE);
		font_small.load();
		
		fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		font_normal = FontFactory.createStrokeFromAsset(this.getFontManager(), fontTexture, this.getAssets(), fontName, 100, true, Color.WHITE, 10, Color.BLACK);
		font_normal.load();
		
		fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		font_big = FontFactory.createStrokeFromAsset(this.getFontManager(), fontTexture, this.getAssets(), fontName, 120, true, Color.WHITE, 8, Color.BLACK);
		font_big.load();
	}

	private void loadTextures() {
		game_TA = new BuildableBitmapTextureAtlas(getTextureManager(), 1024 * 4, 1024 * 4, TextureOptions.DEFAULT);

		player_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(game_TA, this, "foxy.png", 4, 1);
		dizzy_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(game_TA, this, "dizzy.png", 6, 1);
		medals_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(game_TA, this, "medals.png", 2, 2);
		flags_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(game_TA, this, "flags.png", 3, 2);
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
		button_share_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "button_share.png");
		best_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "best.png");
		rank_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "rank.png");
		button_show_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(game_TA, this, "button_show.png");
		
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
