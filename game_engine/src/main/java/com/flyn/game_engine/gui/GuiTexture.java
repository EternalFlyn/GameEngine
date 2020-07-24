package com.flyn.game_engine.gui;

import org.lwjgl.glfw.GLFW;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

public class GuiTexture {
	
	private int textureID;
	private float x, y, w, h;
	
	public GuiTexture(int textureID, int x, int y, int width, int height) {
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
		int[] width = new int[] {0}, height = new int[] {0};
		GLFW.glfwGetWindowSize(window, width, height);
		Matrix4f translate = Matrix4f.translate(new Vector3f(2 * x / (float) width[0], -2 * y / (float) height[0], 0));
		Matrix4f scale = Matrix4f.zoom(w / (float) width[0], h / (float) height[0], 0);
		return scale.multiply(translate).multiply(Matrix4f.translate(new Vector3f(-1, 1, 0)));
	}
	
}
