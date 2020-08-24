package com.flyn.game_engine.water;

import java.awt.Color;

import com.flyn.game_engine.basic.Camera;
import com.flyn.game_engine.basic.Light;
import com.flyn.game_engine.basic.ShaderProgram;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector2f;
import com.flyn.game_engine.math.Vector3f;

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
		setUniform4m("transformation", transformation);
	}
	
	public void setProjection(Matrix4f projection) {
		setUniform4m("projection", projection);
	}
	
	public void setViewPosition(Camera camera) {
		setUniform3f("cameraPosition", camera.getPosition());
		setUniform4m("view", camera.createViewMatrix());
	}
	
	public void setViewPlaneDistance(Vector2f viewPlane) {
		setUniform2f("viewPlaneDistance", viewPlane.x(), viewPlane.y());
	}
	
	public void connectTextureUnit() {
		setUniform1i("reflectionTexture", 0);
		setUniform1i("refractionTexture", 1);
		setUniform1i("dudvMap", 2);
		setUniform1i("normalMap", 3);
		setUniform1i("depthMap", 4);
	}
	
	public void setSkyColor(Color color) {
		setUniform3f("skyColor", new Vector3f(color));
	}
	
	public void setWaveStrength(float value) {
		setUniform1f("waveStrength", value);
	}
	
	public void setMoveFactor(float value) {
		setUniform1f("moveFactor", value);
	}
	
	public void setLight(Light light) {
		setUniform3f("lightPosition", light.getPosition());
		setUniform3f("lightColor", light.getColor());
	}
	
}
