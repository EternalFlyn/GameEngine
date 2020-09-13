package com.flyn.game_engine.math;

public class Vector2f extends Vector {

	public Vector2f() {
		super(2);
	}
	
	public Vector2f(float x, float y) {
		super(2);
		elements[0][0] = x;
		elements[1][0] = y;
	}
	
	public float x() {
		return elements[0][0];
	}
	
	public float y() {
		return elements[1][0];
	}
	
	public void setXY(float x, float y) {
		elements[0][0] = x;
		elements[1][0] = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vector2f) {
			Vector2f v = (Vector2f) obj;
			return elements[0][0] == v.elements[0][0] && elements[1][0] == v.elements[1][0];
		}
		return false;
	}

}
