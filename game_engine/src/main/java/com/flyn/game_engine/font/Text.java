package com.flyn.game_engine.font;

import java.awt.Color;
import java.awt.Font;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

public class Text {
	
	private float x, y, size = 1;
	private String fontName = "Naikai", text;
	private int fontType = Font.PLAIN;
	private Color textColor = Color.WHITE;
	private Matrix4f transformationMatrix;
	
	static {
		FontGenerator.addFontFile("Naikai", "src/main/java/font/NaikaiFont-Light.ttf");
	}
	
	public Text(String text, float x, float y) {
		this.text = text;
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

	public void setScale(float size) {
		this.size = size;
		updateMatrix();
	}

	public String getText() {
		return text;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontType() {
		return fontType;
	}

	public void setFontType(int fontType) {
		this.fontType = fontType;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
		
}
