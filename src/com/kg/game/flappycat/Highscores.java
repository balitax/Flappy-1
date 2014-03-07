package com.kg.game.flappycat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Highscores {

	public int r1, r2, r3;
	public enum Medal { GOLD_NEW, GOLD_OLD, SILVER, BRONZE, NO_MEDAL }
	
	private String r1_str, r2_str, r3_str;
	private SharedPreferences prefs;
	
	public Highscores() {
		r1 = r2 = r3 = 0;
		r1_str = "result1";
		r2_str = "result2";
		r3_str = "result3";
		
		prefs = A.activity.getSharedPreferences("hs", Context.MODE_PRIVATE);
		
		loadHS();
	}
	
	private void loadHS() {
		r1 = prefs.getInt(r1_str, 0);
		r2 = prefs.getInt(r2_str, 0);
		r3 = prefs.getInt(r3_str, 0);
	}
	
	private void saveHS() {
		Editor e = prefs.edit();
		e.putInt(r1_str, r1);
		e.putInt(r2_str, r2);
		e.putInt(r3_str, r3);
		e.commit();
	}
	
	public void resetHS() {
		r1 = r2 = r3 = 0;
		saveHS();
	}
	
	public Medal addHS(int score) {
		
		if (score == r1) {
			return Medal.GOLD_OLD;
			
		} else if (score > r1) {
			r3 = r2; 
			r2 = r1;
			r1 = score;
			
			saveHS();
			return Medal.GOLD_NEW;
			
		} else if (score >= r2) {
			r3 = r2;
			r2 = score;	
			
			saveHS();
			return Medal.SILVER;
			
		} else if (score >= r3) {
			r3 = score;
			
			saveHS();
			return Medal.BRONZE;
		}
		
		return Medal.NO_MEDAL;
	}
	
}
