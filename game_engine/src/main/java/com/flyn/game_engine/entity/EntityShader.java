package com.flyn.game_engine.entity;

import java.awt.Color;
import java.util.ArrayList;

import com.flyn.game_engine.basic.Light;
import com.flyn.game_engine.basic.ShaderProgram;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.math.Vector4f;

public class EntityShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 4;
	private static final String VERTEX_FILE = "src/main/java/shader/texturedShader.vs";
	private static final String FRAGMENT_FILE = "src/main/java/shader/texturedShader.fs";
	
	public EntityShader() {
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
	
	public void setUsedFakeLight(boolean useFakeLight) {
		if(useFakeLight) setUniform1f("useFakeLight", 1);
		else setUniform1f("useFakeLight", 0);
	}
	
	public void setSkyColor(Color color) {
		setUniform3f("skyColor", new Vector3f(color));
	}
	
	public void setTextureAmount(int col, int row) {
		setUniform2f("textureAmount", col, row);
	}
	
	public void setTextureOffset(float x, float y) {
		setUniform2f("textureOffset", x, y);
	}
	
	public void setClipPlane(Vector4f plane) {
		setUniform4f("clipPlane", a, b, c, d);
	}

}
