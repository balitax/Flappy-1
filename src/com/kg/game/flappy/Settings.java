package com.kg.game.flappy;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.color.Color;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Settings {

	float best_width = A.CW * 0.9f;
	float best_height = best_width / (A.best_TR.getWidth() / A.best_TR.getHeight());
			
	float rank_width = best_width / (A.best_TR.getWidth() / A.rank_TR.getWidth());
	float rank_height = rank_width / (A.rank_TR.getWidth() / A.rank_TR.getHeight());
	
	String str_player = "player";
	String str_dangerousWall = "wall";
	
	float gravity = 90;
	int sps = 1; // physics steps per second
	
	// PLAYER
	//FixtureDef playerFD = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f); // Density, Elasticity, Friction
	FixtureDef playerFD = PhysicsFactory.createFixtureDef(1000f, 0.5f, 100.0f);
	float playerJumpForce = 26;
	float playerMass = 1;
	
	float dizzySize = 150;
	float playerRadius = 37;
	float playerWidth = 150;
	float playerHeight = playerWidth;
	float playerXOffset = 100;
	
	long dizzyAnimationSpeed = 150;
	long playerAnimationSpeed = 80;
	long[] durs = new long[] { 80, 80, 80 + 20, 80 + 20 };
	int[] frms = new int[] { 1, 2, 1, 0 };
	
	
	// WALL
	public FixtureDef wallFD = PhysicsFactory.createFixtureDef(0, 0, 0.5f);
	
	float wallWidth = 125;
	float wallHeight = 750;
	
	float wallSpaceV = 242; //242
	float wallSpaceH = 377;
	
	float wallSpeed = 10;
	float wallDVMax = 200; // -DV ... +DV
	float wallStartX = 1000;
	
	int wallN = 3;
	
	float flagWidth = 25 * 4;
	float flagHeight = 18 * 4;
	
	float medalSize = 90;
	float medalY = 300;

	// GROUND
	public FixtureDef groundFD = wallFD;
	float groundHeight = 275;
	
	// INTERFACE
	float new_width = 120;
	float button_width = 275;
	float button_height = button_width / 2;
	float hs_width = 500;
	float buttons_y = 1000;
	float gameOverTextHeight = A.CW / 4;
	float gameOverText_y = -gameOverTextHeight + 20;
	
	int scoreMaxLetters = 4;
	int rankMaxLetters = 100;
	int nameMaxLetters = 100;
	
	// TEXT
	float shadowOffsetBig = 10;
	float shadowOffsetSmall = 7; 
	
	Color color_score = new Color(1f, 1f, 1f);
	Color color_scoreShadow = new Color(0.8f, 0.41f, 0.078f);
	Color color_score_hs = new Color(1f, 1f, 1f);
	Color color_scoreShadow_hs = new Color(0.31f, 0.196f, 0.26f);
	
	// BG
	float bg_groundSpeed = 6f;
	float bg_midSpeed = 150;
	float bg_backSpeed = 50;
	
	public Settings() { }
	
}
