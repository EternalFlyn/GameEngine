package com.flyn.game_engine.shader;

public class DefaultShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/main/java/com/flyn/game_engine/shader/vertexShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/com/flyn/game_engine/shader/fragmentShader.fs";
	
	public DefaultShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
