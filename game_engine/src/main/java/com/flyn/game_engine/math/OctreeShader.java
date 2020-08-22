package com.flyn.game_engine.math;

import com.flyn.game_engine.basic.ShaderProgram;

public class OctreeShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/main/java/shader/octreeShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/shader/octreeShader.fs";

	public OctreeShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}
	
	public void setProjection(Matrix4f projection) {
		setUniform4m("projection", projection);
	}
	
	public void setViewPosition(Matrix4f camera) {
		setUniform4m("view", camera);
	}

}
