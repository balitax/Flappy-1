package com.kg.game.flappy;

import java.util.ArrayList;

import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadScoresResult;
import com.google.example.games.basegameutils.GameHelper;
import com.kg.game.flappy.Highscores.Medal;

public class FlagsController {

	private GameHelper mHelper;
	private Score score;
	private ArrayList<WallPair> wps;
	private ArrayList<FlagState> fs;
	
	public FlagsController(GameHelper gh, Score s, ArrayList<WallPair> w) {
		mHelper = gh;
		score = s;
		wps = w;
		
		fs = new ArrayList<FlagState>();

		/*fs.add(new FlagState(0, false, Medal.GOLD, "0"));
		fs.add(new FlagState(1, false, Medal.GOLD, "1"));
		fs.add(new FlagState(2, false, Medal.GOLD, "2"));
		fs.add(new FlagState(3, false, Medal.GOLD, "3"));
		fs.add(new FlagState(4, false, Medal.GOLD, "4"));
		fs.add(new FlagState(5, false, Medal.GOLD, "5"));
		fs.add(new FlagState(6, false, Medal.GOLD, "6"));
		fs.add(new FlagState(7, false, Medal.GOLD, "7"));
		fs.add(new FlagState(8, false, Medal.GOLD, "8"));
		fs.add(new FlagState(9, false, Medal.GOLD, "9"));*/
		
		resetFlags();
	}
	
	public void resetFlags() {
		loadTopScores();
	}
	
	public void check() {
		for (FlagState f : fs) checkScore(f);
	}
	
	
	
	// PRIVATE

	private Medal getMedalFromInt(int i) {
		if (i == 0) return Medal.GOLD;
		if (i == 1) return Medal.SILVER;
		if (i == 2) return Medal.BRONZE;
		
		return Medal.NO_MEDAL;
	}
	
	private void loadTopScores() {

		fs.clear();

		PendingResult<LoadScoresResult> prSocial = Games.Leaderboards.loadTopScores(mHelper.getApiClient(), A.LB_ID, 
				LeaderboardVariant.TIME_SPAN_DAILY, LeaderboardVariant.COLLECTION_SOCIAL, 3);
		prSocial.setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
			@Override
			public void onResult(LoadScoresResult lsr) {
				LeaderboardScoreBuffer lb = lsr.getScores();
				if (lb == null || lb.getCount() == 0) { lb.close(); return; }
				for (int i = 0; i < lb.getCount(); i++) {
					fs.add(new FlagState(
							Integer.parseInt(lb.get(i).getDisplayScore()), 
							false, 
							getMedalFromInt(i), 
							lb.get(i).getScoreHolderDisplayName()));
					
					Log.e("SOC", lb.get(i).getScoreHolderDisplayName() + " : " + lb.get(i).getDisplayScore());
				}
				lb.close();
			}});
		
		PendingResult<LoadScoresResult> prPublic = Games.Leaderboards.loadTopScores(mHelper.getApiClient(), A.LB_ID, 
				LeaderboardVariant.TIME_SPAN_DAILY, LeaderboardVariant.COLLECTION_PUBLIC, 3);
		prPublic.setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
			@Override
			public void onResult(LoadScoresResult lsr) {
				LeaderboardScoreBuffer lb = lsr.getScores();
				if (lb == null || lb.getCount() == 0) { lb.close(); return; }
				for (int i = 0; i < lb.getCount(); i++) {
					fs.add(new FlagState(
							Integer.parseInt(lb.get(i).getDisplayScore()), 
							true, 
							getMedalFromInt(i), 
							lb.get(i).getScoreHolderDisplayName()));
					
					Log.e("PUB", lb.get(i).getScoreHolderDisplayName() + " : " + lb.get(i).getDisplayScore());
				}
				lb.close();
			}});
	}
	
	
	private void checkScore(FlagState fs) {

		if (fs.passed) return;
		
		if (fs.score == 0) {
			return;
		}
		
		if (fs.score == 1) {
			wps.get(0).flag.show(fs); 
			return;
		}
		
		if (fs.score == 2) {
			wps.get(1).flag.show(fs); 
			return;
		}
		
		if (score.get() + 1 != fs.score) {
			return;
		}
		
		//getLeftWP().flag.show = true;
		//getLeftWP().flag.tempfs = fs;
		
		getRightWP().flag.show(fs);
		
		
	}
	
	private WallPair getRightWP() {
		WallPair res = wps.get(0);
		for (WallPair wp : wps) if (wp.getX() > res.getX()) res = wp;
		return res;
	}
	
	private WallPair getLeftWP() {
		WallPair res = wps.get(0);
		for (WallPair wp : wps) if (wp.getX() < res.getX()) res = wp;
		return res;
	}
	
}
