package com.kg.game.flappy;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadScoresResult;

public class BestSprite extends Sprite {

	private Text text;
	
	public BestSprite(float x, float y) {
		
		super(x, y,	A.settings.best_width, A.settings.best_height, A.best_TR, A.vbom);
		setX((A.CW - this.getWidth()) / 2);
		
		text = new Text(0, getHeight() / 2 + 20, A.font_small, "", A.settings.rankMaxLetters, A.vbom);
		text.setColor(Color.BLACK);
		this.attachChild(text);
		
		setVisible(false);
		update();
		
		
	}
	
	public void update() {
		PendingResult<LoadScoresResult> pr = Games.Leaderboards.loadTopScores(A.gameHelper.getApiClient(), A.LB_ID, 
				LeaderboardVariant.TIME_SPAN_DAILY, LeaderboardVariant.COLLECTION_PUBLIC, 1);
		pr.setResultCallback(new ResultCallback<Leaderboards.LoadScoresResult>() {
			@Override
			public void onResult(LoadScoresResult lsr) {
				LeaderboardScoreBuffer lb = lsr.getScores();
				if (lb == null || lb.getCount() == 0) { lb.close(); return; }
				for (int i = 0; i < lb.getCount(); i++) {
					show(lb.get(i).getScoreHolderDisplayName(), lb.get(i).getDisplayScore());
				}
				lb.close();
			}});
	}
	
	private void show(String name, String score) {
		name = "Кирилл Галкин";
		text.setText(name + " : " + score);
		text.setX((getWidth() - text.getWidth()) / 2);
		
		setVisible(true);
		//this.registerEntityModifier(new AlphaModifier(1, 0, 1));	
		//setWidth(A.CW);
		//setWidth(text.getWidth() + 50);
		
		//setX((A.CW - getWidth()) / 2);
	}
	
}
