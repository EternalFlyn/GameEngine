package com.flyn.game_engine.math;

public class Vector4f extends Vector {
	
	public Vector4f() {
		super(4);
	}
	
	public Vector4f(float x, float y, float z, float w) {
		super(4);
		elements[0][0] = x;
		elements[1][0] = y;
		elements[2][0] = z;
		elements[3][0] = w;
	}
	
	public Vector4f(Vector3f normal, float distance) {
		super(4);
		elements[0][0] = normal.x();
		elements[1][0] = normal.y();
		elements[2][0] = normal.z();
		elements[3][0] = distance;
	}
	
	public float x() {
		return elements[0][0];
	}
	
	public float y() {
		return elements[1][0];
	}
	
	public float z() {
		return elements[2][0];
	}
	
	public float w() {
		return elements[3][0];
	}

}
