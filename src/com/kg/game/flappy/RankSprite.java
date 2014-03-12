package com.kg.game.flappy;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadPlayerScoreResult;
import com.kg.game.flappy.Highscores.Medal;

public class RankSprite extends Sprite {
	
	private Text text;
	private  Flag flag;
	private String rank;
	private Long rank_long;
	
	public RankSprite(float x, float y) {
		super(x, y,	A.settings.rank_width, A.settings.rank_height, A.rank_TR, A.vbom);
		setX((A.settings.best_width - this.getWidth()) / 2);

		Color textColor = Color.WHITE;
		
		text = new Text(0, getHeight() * 0.59f, A.font_small, "", A.settings.rankMaxLetters, A.vbom);
		text.setColor(textColor);
		this.attachChild(text);

		setVisible(false);
		
		rank = "";
		rank_long = 0l;
		
		flag = new Flag(this.getWidth(), 0, this.getHeight() / 4 * A.settings.flagWidth / A.settings.flagHeight, this.getHeight() / 4);
		
		flag.setY(text.getY() + (text.getHeight() - flag.getHeight()) / 2);

		attachChild(flag);

		update();
	}

	public void update() {
		
		flag.hide();

		PendingResult<LoadPlayerScoreResult> pr = Games.Leaderboards.loadCurrentPlayerLeaderboardScore(A.gameHelper.getApiClient(), A.LB_ID, 
				LeaderboardVariant.TIME_SPAN_DAILY, LeaderboardVariant.COLLECTION_PUBLIC);
		pr.setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {

			@Override
			public void onResult(LoadPlayerScoreResult lsr) {
				LeaderboardScore ls = lsr.getScore();
				if (ls == null) return;
				rank = ls.getDisplayRank();
				rank_long = ls.getRank();
				show();
			}});
	
	}

	
	private Medal getMedalFromNum(long i) {
		if (i == 1) return Medal.GOLD;
		if (i == 2) return Medal.SILVER;
		if (i == 3) return Medal.BRONZE;
		
		return Medal.NO_MEDAL;
	}
	
	private void show() {
		
		A.player.setVisible(false);
		
		text.setText(rank);
		text.setX((getWidth() - text.getWidth()) / 2);
		
		Medal medal_pub = getMedalFromNum(rank_long);
		if (medal_pub != Medal.NO_MEDAL) flag.show(new FlagState(0, true, medal_pub, ""));

		setVisible(true);
		//this.registerEntityModifier(new AlphaModifier(1, 0, 1));	
		//setWidth(A.CW);
		//setWidth(text.getWidth() + 50);

		//setX((A.CW - getWidth()) / 2);
	}

}

