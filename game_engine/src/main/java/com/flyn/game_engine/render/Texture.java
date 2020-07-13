package com.flyn.game_engine.render;

public class Texture {

	private boolean transparency = false, useFakeLight = false;
	private int textureID;
	private float shineDamper = 1, reflectivity = 0;
	
	public Texture(int textureID) {
		this.textureID = textureID;
	}
	
	public int getID() {
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public boolean hasTransparency() {
		return transparency;
	}

	public void setTransparency(boolean hasTransparency) {
		this.transparency = hasTransparency;
	}

	public boolean isUsedFakeLight() {
		return useFakeLight;
	}

	public void setUsedFakeLight(boolean useFakeLight) {
		this.useFakeLight = useFakeLight;
	}

}
