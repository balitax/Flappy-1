package com.kg.game.flappycat;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.util.color.Color;

public class ShadowText extends Text {

	public Text shadow;
	
	public ShadowText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, Color textColor, Color shadowColor, float shadowOffset) {
		super(pX, pY, pFont, pText, pCharactersMaximum, A.vbom);
		shadow = new Text(pX, pY + shadowOffset, pFont, pText, pCharactersMaximum, A.vbom);
		this.attachChild(shadow);
		
		this.setColor(textColor);
		shadow.setColor(shadowColor);

		shadow.setZIndex(-1);
	}
	
	public void setText(String t) {
		super.setText(t);
		shadow.setText(t);
	}

}
