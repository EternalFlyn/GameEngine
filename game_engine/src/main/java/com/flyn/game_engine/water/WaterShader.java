package com.flyn.game_engine.water;

import com.flyn.game_engine.basic.ShaderProgram;
import com.flyn.game_engine.math.Matrix4f;

public class WaterShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/main/java/shader/waterShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/shader/waterShader.fs";
	
	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}
	
	public void setTransformation(Matrix4f transformation) {
		setUniform4f("transformation", transformation);
	}
	
	public void setProjection(Matrix4f projection) {
		setUniform4f("projection", projection);
	}
	
	public void setViewPosition(Matrix4f camera) {
		setUniform4f("view", camera);
	}

}
