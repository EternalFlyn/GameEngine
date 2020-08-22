package com.flyn.game_engine.terrain;

import java.awt.Color;
import java.util.ArrayList;

import com.flyn.game_engine.basic.Light;
import com.flyn.game_engine.basic.ShaderProgram;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

public class TerrainShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;
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
		setUniform4m("transformation", transformation);
	}
	
	public void setProjection(Matrix4f projection) {
		setUniform4m("projection", projection);
	}
	
	public void setViewPosition(Matrix4f camera) {
		setUniform4m("view", camera);
	}
	
	public void setLight(ArrayList<Light> lights) {
		for(int i = 0; i < MAX_LIGHTS; i++) {
			if(i < lights.size()) {
				Light light = lights.get(i);
				setUniform3f("lightPosition[" + i + "]", light.getPosition());
				setUniform3f("lightColor[" + i + "]", light.getColor());
				setUniform3f("attenuation[" + i + "]", light.getAttenuation());
			}
			else {
				setUniform3f("lightPosition[" + i + "]", new Vector3f());
				setUniform3f("lightColor[" + i + "]",  new Vector3f());
				setUniform3f("attenuation[" + i + "]",  new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void setShineVariables(float damper, float reflectivity) {
		setUniform1f("shineDamper", damper);
		setUniform1f("reflectivity", reflectivity);
	}
	
	public void setGrassColor(Color color) {
		setUniform3f("grassColor", new Vector3f(color));
	}
	
	public void setSkyColor(Color color) {
		setUniform3f("skyColor", new Vector3f(color));
	}
	
	public void connectTextureUnit() {
		setUniform1i("backgroundTexture", 0);
		setUniform1i("rTexture", 1);
		setUniform1i("gTexture", 2);
		setUniform1i("bTexture", 3);
		setUniform1i("blendMap", 4);
	}
	
}
