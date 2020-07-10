package com.flyn.game_engine.shader;

public class TexturedShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/java/shader/texturedShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/shader/texturedShader.fs";
	
	public TexturedShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
}
