package com.flyn.game_engine.shader;

import java.awt.Color;

import com.flyn.game_engine.entity.Light;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

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
	
	public void setUsedFakeLight(boolean useFakeLight) {
		if(useFakeLight) setUniform1f("useFakeLight", 1);
		else setUniform1f("useFakeLight", 0);
	}
	
	public void setSkyColor(Color color) {
		setUniform3f("skyColor", Vector3f.colorVector(color));
	}
	
	public void setTextureAmount(int col, int row) {
		setUniform2f("textureAmount", col, row);
	}
	
	public void setTextureOffset(float x, float y) {
		setUniform2f("textureOffset", x, y);
	}
	
	public void setMinBrightness(float level) {
		setUniform1f("minBrightness", level);
	}

}
