package com.flyn.game_engine.gui;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.shader.ShaderProgram;

public class GuiShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/main/java/shader/guiShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/shader/guiShader.fs";
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}
	
	public void setTransformation(Matrix4f transformation) {
		setUniform4f("transformation", transformation);
	}

}
