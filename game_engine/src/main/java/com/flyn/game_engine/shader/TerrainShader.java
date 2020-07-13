package com.flyn.game_engine.shader;

import java.awt.Color;

import com.flyn.game_engine.entity.Light;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/main/java/shader/terrainShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/shader/terrainShader.fs";
	
	public TerrainShader() {
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
	
	public void setGrassColor(Color color) {
		Vector3f colorVector = new Vector3f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f);
		setUniform3f("grassColor", colorVector);
	}
	
}
