package com.flyn.game_engine.font;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

public class Text {
	
	public static enum Alignment {
		RIGHT_ALIGNMENT,
		CENTER_ALIGNMENT,
		LEFT_ALIGNMENT
	}
	
	int alignment = 0;
	
	private int fontType = Font.PLAIN;
	private float x, y, size = 1, maxTextLength = -1, textHeight;
	private String fontName = "Naikai";
	private Color textColor = Color.WHITE;
	private Matrix4f transformationMatrix;
	private ArrayList<Glyph[]> text = new ArrayList<>();
	
	static {
		FontGenerator.addFontFile("Naikai", "src/main/java/font/NaikaiFont-Light.ttf");
	}
	
	public Text(String text, float x, float y) {
		String[] texts = text.split("\n");
		for(String s : texts) this.text.add(FontGenerator.getGlyphs(fontName, fontType, s));
		textHeight = this.text.get(0)[0].getHeight();
		this.x = x;
		this.y = y;
		updateMatrix();
	}
	
	private void updateMatrix() {
		Matrix4f translate = Matrix4f.translate(new Vector3f(2 * x - 1, -2 * y + 1, 0));
		Matrix4f scale = Matrix4f.zoom(size, size, 0);
		transformationMatrix = (Matrix4f) translate.multiply(scale);
	}
	
	public Matrix4f getTransformationMatrix() {
		return transformationMatrix;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getScale() {
		return size;
	}

	public void setScale(float size) {
		this.size = size;
		updateMatrix();
	}

	public ArrayList<Glyph[]> getText() {
		return text;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public void setFontType(int fontType) {
		this.fontType = fontType;
	}

	public float getTextHeight() {
		return textHeight;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	//To next line, if text offset more than this number
	public float getMaxTextLength() {
		if(maxTextLength < 0) return 1 - x;
		return maxTextLength;
	}

	public void setMaxTextLength(float maxTextLength) {
		this.maxTextLength = maxTextLength;
	}

	public Alignment getAlignment() {
		switch(alignment) {
		case 0:
			return Alignment.RIGHT_ALIGNMENT;
		case 1:
			return Alignment.CENTER_ALIGNMENT;
		case 2:
			return Alignment.LEFT_ALIGNMENT;
		}
		return null;
	}

	public void setAlignment(Alignment alignment) {
		switch(alignment) {
		case LEFT_ALIGNMENT:
			this.alignment = 0;
			break;
		case CENTER_ALIGNMENT:
			this.alignment = 1;
			break;
		case RIGHT_ALIGNMENT:
			this.alignment = 2;
			break;
		default:
			this.alignment = 0;;
		}
	}
		
}
