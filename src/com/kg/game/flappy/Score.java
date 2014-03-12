package com.kg.game.flappy;

public class Score {
	
	private int value;
	
	public Score() {
		reset();
	}
	
	public void add() {
		value ++;
	}
	
	public void reset() {
		value = 0;
	}
	
	public int get() {
		return value;
	}
}
