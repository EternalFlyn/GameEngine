package com.flyn.game_engine.skybox;

import java.awt.Color;

import com.flyn.game_engine.basic.ShaderProgram;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/java/shader/skyboxShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/shader/skyboxShader.fs";
	
	public SkyboxShader() {
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
	
	public void setSkyColor(Color color) {
		setUniform3f("skyColor", new Vector3f(color));
	}
	
	public void setBlendFactor(float blend) {
		setUniform1f("blendFactor", blend);
	}
	
	public void connectTextureUnit() {
		setUniform1i("dayTexture", 0);
		setUniform1i("nightTexture", 1);
	}
	
}
