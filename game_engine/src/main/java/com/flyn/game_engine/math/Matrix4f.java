package com.flyn.game_engine.math;

import static java.lang.Math.*;

import java.nio.FloatBuffer;

import com.flyn.game_engine.utils.BufferUtils;

public class Matrix4f {

	public static final int SIZE = 4 * 4;
	public float[] elements = new float[SIZE];

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
				float sum = 0;
				for(int a = 0; a < 4; a++) {
					sum += elements[a + y * 4] * matrix.elements[x + a * 4];
				}
				result.elements[x + y * 4] = sum;
			}
		}
		
		return result;
	}
	
	public double getDet() {
		double[] m = new double[16];
		for(int i = 0; i < 16; i++) m[i] = elements[i];
		return determinant(m, 3);
	}
	
	private double determinant(double[] matrix, int size) {
		if(size == 0) return matrix[0];
		if(matrix[0] == 0) {
			for(int i = 1; i <= size; i++) {
				if(i == size) return 0;
				if(matrix[i] == 0) continue;
				for(int j = 0; j < size + 1; j++) {
					double temp = matrix[0 + j * (size + 1)];
					matrix[0 + j * (size + 1)] = matrix[i + j * (size + 1)];
					matrix[i + j * (size + 1)] = -temp;
				}
				break;
			}
		}
		double[] m = new double[size * size];
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				m[r + c * size] = matrix[(r + 1) + (c + 1) * (size + 1)];
				if(matrix[r] != 0) m[r + c * size] -= matrix[r + 1] / matrix[0] * matrix[(c + 1) * (size + 1)];
			}
		}
		return matrix[0] * determinant(m, size - 1);
	}
	
	private double[] minor(double[] matrix, int size, int row, int col) {
		double[] minor = new double[size * size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				minor[i + j * size] = matrix[(i < row ? i : i + 1) + (j < col ? j : j + 1) * (size + 1)];
			}
		}
		return minor;
	}
	
	public Matrix4f inverse() {
		double det = getDet();
		if(det == 0) return null;
		Matrix4f result = new Matrix4f();
		double[] m = new double[16];
		for(int i = 0; i < 16; i++) m[i] = elements[i];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				int sgn = (i + j) % 2 == 0 ? 1 : -1;
				result.elements[i + j * 4] = (float) (sgn * determinant(minor(m, 3, j, i), 2) / det);
			}
		}
		return result;
	}
	
	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(elements);
	}
	
	public Matrix4f clone() {
		Matrix4f result = new Matrix4f();
		result.elements = this.elements.clone();
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				sb.append(elements[i + j * 4]);
				if(j != 3) sb.append(" ");
			}
			if(i != 3) sb.append("\n");
		}
		return sb.toString();
	}

}
