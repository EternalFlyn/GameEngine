package com.flyn.game_engine.math;

import java.awt.Color;

public class Vector3f {

	public float x, y, z;
	
	public Vector3f() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector3f colorVector(Color color) {
		float r = color.getRed() / 255f;
		float g = color.getGreen() / 255f;
		float b = color.getBlue() / 255f;
		return new Vector3f(r, g, b);
	}
	
	public Vector3f normalise() {
		double dx = x, dy = y, dz = z;
		float norm = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
		Vector3f result = new Vector3f(x / norm, y / norm, z / norm);
		return result;
	}
}
