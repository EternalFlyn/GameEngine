package com.flyn.game_engine.font;

import com.flyn.game_engine.basic.ShaderProgram;
import com.flyn.game_engine.math.Matrix4f;

public class TextShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/main/java/shader/textShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/shader/textShader.fs";
	
	public TextShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}
	
	public void setTransformation(Matrix4f transformation) {
		setUniform4m("transformation", transformation);
	}

}
