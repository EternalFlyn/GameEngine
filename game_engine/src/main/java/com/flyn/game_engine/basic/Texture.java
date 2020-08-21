package com.flyn.game_engine.basic;

public class Texture {

	private boolean transparency = false, useFakeLight = false;
	private int textureID, column = 1, row = 1;
	private float shineDamper = 1, reflectivity = 0;
	
	public Texture(int textureID) {
		this.textureID = textureID;
	}
	
	public Texture(int textureID, int col, int row) {
		this.textureID = textureID;
		this.column = col;
		this.row = row;
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
	
	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

}
