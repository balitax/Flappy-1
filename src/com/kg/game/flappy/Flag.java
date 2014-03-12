package com.kg.game.flappy;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.kg.game.flappy.Highscores.Medal;

public class Flag extends TiledSprite {

	public boolean show = false;
	public FlagState tempfs = null;
	
	private Text ownerNameText;
	
	public Flag() {
		
		this((A.settings.wallWidth -A.settings.flagWidth) / 2, -A.settings.flagHeight, A.settings.flagWidth, A.settings.flagHeight);

	}
	
	public Flag(float x, float y, float w, float h) {
		
		super(x, y, w, h, A.flags_TR, A.vbom);
		
		ownerNameText = new Text(0, 0, A.font_small, "", A.settings.nameMaxLetters, A.vbom);
		ownerNameText.setColor(Color.WHITE);
		this.attachChild(ownerNameText);
		
		hide();
	}
	
	public void show(FlagState fs) {
		int row, col;
		if (fs.isPublic) row = 0; else row = 1;
		if (fs.medal == Medal.GOLD) col = 0; else if (fs.medal == Medal.SILVER) col = 1; else col = 2;
		setCurrentTileIndex(row * 3 + col);

		ownerNameText.setText(fs.name);
		ownerNameText.setPosition((A.settings.flagWidth - ownerNameText.getWidth()) / 2, -ownerNameText.getHeight() - 20);
		
		this.setVisible(true);
		fs.pass();
	}
	
	public void hide() {
		this.setVisible(false);
	}
	
}
