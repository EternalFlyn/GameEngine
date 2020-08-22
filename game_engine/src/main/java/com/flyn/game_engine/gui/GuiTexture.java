package com.flyn.game_engine.gui;

import org.lwjgl.glfw.GLFW;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

public class GuiTexture {
	
	private int textureID;
	private float x, y, w, h;
	
	public GuiTexture(int textureID, float x, float y, float width, float height) {
		this.textureID = textureID;
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
	}

	public int getTextureID() {
		return textureID;
	}
	
	public Matrix4f getTransformationMatirx(long window) {
		Matrix4f translate = Matrix4f.translate(new Vector3f(2 * x - 1, -2 * y + 1, 0));
		Matrix4f scale = Matrix4f.zoom(w, h, 0);
		return (Matrix4f) translate.multiply(scale);
	}
	
}
