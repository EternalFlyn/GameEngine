package com.flyn.game_engine.math;

import static java.lang.Math.*;

import java.nio.FloatBuffer;

import com.flyn.game_engine.utils.BufferUtils;

public class Matrix4f {

	public static final int SIZE = 4 * 4;
	public float[] elements = new float[SIZE];
	
	private Matrix4f() {}

	public static Matrix4f identity() {
		Matrix4f result = new Matrix4f();
		
		for(int i = 0; i < SIZE; i++) {
			result.elements[i] = 0.0f;
		}
		
		result.elements[0 + 0 * 4] = 1.0f;
		result.elements[1 + 1 * 4] = 1.0f;
		result.elements[2 + 2 * 4] = 1.0f;
		result.elements[3 + 3 * 4] = 1.0f;
		
		return result;
	}

	public static Matrix4f orthographic(float xi, float xf, float yi, float yf, float zi, float zf) {
		Matrix4f result = identity();
		
		result.elements[0 + 0 * 4] = 2.0f / (xf - xi);
		result.elements[1 + 1 * 4] = 2.0f / (yf - yi);
		result.elements[2 + 2 * 4] = 2.0f / (zf - zi);
		
		result.elements[0 + 3 * 4] = (xi + xf) / (xi - xf);
		result.elements[1 + 3 * 4] = (yi + yf) / (yi - yf);
		result.elements[2 + 3 * 4] = (zi + zf) / (zi - zf);
		
		return result;
	}
	
	public static Matrix4f zoom(float magnification) {
		Matrix4f result = identity();
		
		result.elements[0 + 0 * 4] = magnification;
		result.elements[1 + 1 * 4] = magnification;
		result.elements[2 + 2 * 4] = magnification;
		
		return result;
	}
	
	public static Matrix4f zoom(float magX, float magY, float magZ) {
		Matrix4f result = identity();
		
		result.elements[0 + 0 * 4] = magX;
		result.elements[1 + 1 * 4] = magY;
		result.elements[2 + 2 * 4] = magZ;
		
		return result;
	}
	
	public static Matrix4f translate(Vector3f vector) {
		Matrix4f result = identity();
		
		result.elements[0 + 3 * 4] = vector.x;
		result.elements[1 + 3 * 4] = vector.y;
		result.elements[2 + 3 * 4] = vector.z;
		
		return result;
	}
	
	public static Matrix4f roll(float angle) {
		Matrix4f result = identity();
		float radian = (float) toRadians(angle);
		float cos = (float) cos(radian), sin = (float) sin(radian);
		
		result.elements[1 + 1 * 4] = cos;
		result.elements[2 + 1 * 4] = sin;
		result.elements[1 + 2 * 4] = -sin;
		result.elements[2 + 2 * 4] = cos;
		
		return result;
	}
	
	public static Matrix4f pitch(float angle) {
		Matrix4f result = identity();
		float radian = (float) toRadians(angle);
		float cos = (float) cos(radian), sin = (float) sin(radian);
		
		result.elements[0 + 0 * 4] = cos;
		result.elements[0 + 2 * 4] = sin;
		result.elements[2 + 0 * 4] = -sin;
		result.elements[2 + 2 * 4] = cos;
		
		return result;
	}
	
	public static Matrix4f yaw(float angle) {
		Matrix4f result = identity();
		float radian = (float) toRadians(angle);
		float cos = (float) cos(radian), sin = (float) sin(radian);
		
		result.elements[0 + 0 * 4] = cos;
		result.elements[1 + 0 * 4] = sin;
		result.elements[0 + 1 * 4] = -sin;
		result.elements[1 + 1 * 4] = cos;
		
		return result;
	}
	
	public static Matrix4f rotateWithAxis(Vector3f vector, float angle) {
		Matrix4f result = identity();
		float radian = (float) toRadians(angle);
		float cos = (float) cos(radian), sin = (float) sin(radian), omc = 1.0f - cos;
		
		result.elements[0 + 0 * 4] = vector.x * vector.x * omc + cos;
		result.elements[1 + 0 * 4] = vector.y * vector.x * omc + vector.z * sin;
		result.elements[2 + 0 * 4] = vector.x * vector.z * omc - vector.y * sin;	
		
		result.elements[0 + 1 * 4] = vector.x * vector.y * omc - vector.z * sin;
		result.elements[1 + 1 * 4] = vector.y * vector.y * omc + cos;
		result.elements[2 + 1 * 4] = vector.y * vector.z * omc + vector.x * sin;
		
		result.elements[0 + 2 * 4] = vector.x * vector.z * omc + vector.y * sin;
		result.elements[1 + 2 * 4] = vector.y * vector.z * omc - vector.x * sin;
		result.elements[2 + 2 * 4] = vector.z * vector.z * omc + cos;
		return result;
	}
	
	public static Matrix4f rotate(float angleX, float angleY, float angleZ) {
		Matrix4f roll = roll(angleX), pitch = pitch(angleY), yaw = yaw(angleZ);
		Matrix4f result = roll.multiply(pitch).multiply(yaw);
		return result;
	}

	public Matrix4f multiply(Matrix4f matrix) {
		Matrix4f result = new Matrix4f();
		
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 4; x++) {
				float sum = 0.0f;
				for(int a = 0; a < 4; a++) {
					sum += elements[a + y *4] * matrix.elements[x + a * 4];
				}
				result.elements[x + y * 4] = sum;
			}
		}
		
		return result;
	}
	
	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(elements);
	}

}
