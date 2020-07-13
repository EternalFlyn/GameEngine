package com.flyn.game_engine.shader;

import com.flyn.game_engine.entity.Light;
import com.flyn.game_engine.math.Matrix4f;

public class TexturedShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/java/shader/texturedShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/shader/texturedShader.fs";
	
	public TexturedShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoords");
		bindAttribute(2, "normals");
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
	
	public void setLight(Light light) {
		setUniform3f("lightPosition", light.getPosition());
		setUniform3f("lightColor", light.getColor());
	}
	
	public void setShineVariables(float damper, float reflectivity) {
		setUniform1f("shineDamper", damper);
		setUniform1f("reflectivity", reflectivity);
	}
	
}
