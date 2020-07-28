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
	
	public Vector3f(Color color) {
		x = color.getRed() / 255f;
		y = color.getGreen() / 255f;
		z = color.getBlue() / 255f;
	}
	
	public float length() {
		double dx = x, dy = y, dz = z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public Vector3f normalise() {
		float length = length();
		Vector3f result = new Vector3f(x / length, y / length, z / length);
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(", ");
		sb.append(z);
		return sb.toString();
	}
}
