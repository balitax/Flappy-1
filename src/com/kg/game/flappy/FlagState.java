package com.kg.game.flappy;

import com.kg.game.flappy.Highscores.Medal;

public class FlagState {
	
	public boolean passed;
	
	public int score;
	public boolean isPublic;
	public Medal medal;
	public String name;
	
	public FlagState(int s, boolean isp, Medal m, String n) {
		
		passed = false;
		
		score = s;
		isPublic = isp;
		medal = m;
		name = n;
	}
	
	public void pass() {
		passed = true;
	}
	
	public void reset() {
		passed = false;
	}
	
	

}
