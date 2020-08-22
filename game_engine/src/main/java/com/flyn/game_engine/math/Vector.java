package com.flyn.game_engine.math;

public class Vector extends Matrix {
	
	public Vector(int dimension) {
		super(dimension, 1);
	}
	
	public float length() {
		float sum = 0;
		for(int i = 0; i < row; i++) {
			sum += elements[i][0] * elements[i][0];
		}
		return (float) Math.sqrt(sum);
	}
	
	public Vector normalise() {
		float length = length();
		Vector result = (Vector) newMatrixObject(row, column);
		for(int i = 0; i < row; i++) result.elements[i][0] = elements[i][0] / length;
		return result;
	}
	
	static Vector newVectorObject(int dimension) {
		switch(dimension) {
		case 2:
			return new Vector2f();
		case 3:
			return new Vector3f();
		case 4:
			return new Vector4f();
		default:
			return new Vector(dimension);
		}
	}

}
