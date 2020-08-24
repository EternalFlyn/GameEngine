package com.flyn.game_engine.water;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

public class WaterTile {
	
	public static final float TILE_SIZE = 60;
	
	private float x, y, z;
	private float waveStrength = 0.02f;

	public WaterTile(float centerX, float height, float centerZ) {
		this.x = centerX;
		this.y = height;
		this.z = centerZ;
	}
	
	public Matrix4f getTransformationMatirx() {
		return Matrix4f.translate(new Vector3f(x, y, z));
	}

	public float getWaveStrength() {
		return waveStrength;
	}

}
