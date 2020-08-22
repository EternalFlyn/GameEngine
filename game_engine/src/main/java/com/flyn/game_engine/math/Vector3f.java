package com.flyn.game_engine.math;

import java.awt.Color;

public class Vector3f extends Vector {
	
	public Vector3f() {
		super(3);
	}
	
	public Vector3f(float x, float y, float z) {
		super(3);
		elements[0][0] = x;
		elements[1][0] = y;
		elements[2][0] = z;
	}
	
	public Vector3f(Color color) {
		super(3);
		elements[0][0] = color.getRed() / 255f;
		elements[1][0] = color.getGreen() / 255f;
		elements[2][0] = color.getBlue() / 255f;
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
	
	public void set(String target, float... value) {
		int p = 0;
		a:
		for(char c : target.toCharArray()) {
			switch(c) {
			case 'x':
				elements[0][0] = value[p++];
				break;
			case 'y':
				elements[1][0] = value[p++];
				break;
			case 'z':
				elements[2][0] = value[p++];
				break;
			default :
				System.err.println("Contain the illegal character! At : " + target.indexOf(c));
				break a;
			}
		}
	}
	
	public void add(String target, float... value) {
		int p = 0;
		a:
		for(char c : target.toCharArray()) {
			switch(c) {
			case 'x':
				elements[0][0] += value[p++];
				break;
			case 'y':
				elements[1][0] += value[p++];
				break;
			case 'z':
				elements[2][0] += value[p++];
				break;
			default :
				System.err.println("Contain the illegal character! At : " + target.indexOf(c));
				break a;
			}
		}
	}
	
	public void setXYZ(float x, float y, float z) {
		elements[0][0] = x;
		elements[1][0] = y;
		elements[2][0] = z;
	}
	
	public void addXYZ(float x, float y, float z) {
		elements[0][0] += x;
		elements[1][0] += y;
		elements[2][0] += z;
	}

}
