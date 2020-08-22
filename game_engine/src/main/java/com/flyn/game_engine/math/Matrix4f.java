package com.flyn.game_engine.math;

import static java.lang.Math.*;

import java.nio.FloatBuffer;

import com.flyn.game_engine.utils.BufferUtils;

public class Matrix4f extends Matrix {
	
	public Matrix4f() {
		super(4, 4);
	}

	public static Matrix4f identity() {
		Matrix4f result = new Matrix4f();
		
		result.elements[0][0] = 1.0f;
		result.elements[1][1] = 1.0f;
		result.elements[2][2] = 1.0f;
		result.elements[3][3] = 1.0f;
		
		return result;
	}

	public static Matrix4f orthographic(float xi, float xf, float yi, float yf, float zi, float zf) {
		Matrix4f result = identity();
		
		result.elements[0][0] = 2.0f / (xf - xi);
		result.elements[1][1] = 2.0f / (yf - yi);
		result.elements[2][2] = 2.0f / (zf - zi);
		
		result.elements[0][3] = (xi + xf) / (xi - xf);
		result.elements[1][3] = (yi + yf) / (yi - yf);
		result.elements[2][3] = (zi + zf) / (zi - zf);
		
		return result;
	}
	
	public static Matrix4f zoom(float magnification) {
		Matrix4f result = identity();
		
		result.elements[0][0] = magnification;
		result.elements[1][1] = magnification;
		result.elements[2][2] = magnification;
		
		return result;
	}
	
	public static Matrix4f zoom(float magX, float magY, float magZ) {
		Matrix4f result = identity();
		
		result.elements[0][0] = magX;
		result.elements[1][1] = magY;
		result.elements[2][2] = magZ;
		
		return result;
	}
	
	public static Matrix4f translate(Vector3f vector) {
		Matrix4f result = identity();
		
		result.elements[0][3] = vector.x();
		result.elements[1][3] = vector.y();
		result.elements[2][3] = vector.z();
		
		return result;
	}
	
	public static Matrix4f roll(float angle) {
		Matrix4f result = identity();
		float radian = (float) toRadians(angle);
		float cos = (float) cos(radian), sin = (float) sin(radian);
		
		result.elements[1][1] = cos;
		result.elements[2][1] = sin;
		result.elements[1][2] = -sin;
		result.elements[2][2] = cos;
		
		return result;
	}
	
	public static Matrix4f pitch(float angle) {
		Matrix4f result = identity();
		float radian = (float) toRadians(angle);
		float cos = (float) cos(radian), sin = (float) sin(radian);
		
		result.elements[0][0] = cos;
		result.elements[0][2] = sin;
		result.elements[2][0] = -sin;
		result.elements[2][2] = cos;
		
		return result;
	}
	
	public static Matrix4f yaw(float angle) {
		Matrix4f result = identity();
		float radian = (float) toRadians(angle);
		float cos = (float) cos(radian), sin = (float) sin(radian);
		
		result.elements[0][0] = cos;
		result.elements[1][0] = sin;
		result.elements[0][1] = -sin;
		result.elements[1][1] = cos;
		
		return result;
	}
	
	public static Matrix4f rotateWithAxis(Vector3f vector, float angle) {
		Matrix4f result = identity();
		float radian = (float) toRadians(angle);
		float cos = (float) cos(radian), sin = (float) sin(radian), omc = 1.0f - cos;
		
		result.elements[0][0] = vector.x() * vector.x() * omc + cos;
		result.elements[1][0] = vector.y() * vector.x() * omc + vector.z() * sin;
		result.elements[2][0] = vector.x() * vector.z() * omc - vector.y() * sin;	
		
		result.elements[0][1] = vector.x() * vector.y() * omc - vector.z() * sin;
		result.elements[1][1] = vector.y() * vector.y() * omc + cos;
		result.elements[2][1] = vector.y() * vector.z() * omc + vector.x() * sin;
		
		result.elements[0][2] = vector.x() * vector.z() * omc + vector.y() * sin;
		result.elements[1][2] = vector.y() * vector.z() * omc - vector.x() * sin;
		result.elements[2][2] = vector.z() * vector.z() * omc + cos;
		return result;
	}
	
	public static Matrix4f rotate(float angleX, float angleY, float angleZ) {
		Matrix4f roll = roll(angleX), pitch = pitch(angleY), yaw = yaw(angleZ);
		Matrix4f result = (Matrix4f) yaw.multiply(pitch).multiply(roll);
		return result;
	}
	
	public double getDet() {
		double[][] m = new double[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				m[i][j] = elements[i][j];
			}
		}
		return determinant(m);
	}
	
	private double determinant(double[][] matrix) {
		int size = matrix.length - 1;
		if(matrix.length == 1) return matrix[0][0];
		if(matrix[0][0] == 0) {
			for(int i = 1; i < matrix.length; i++) {
				if(i == size) return 0;
				if(matrix[i][0] == 0) continue;
				for(int j = 0; j < matrix.length; j++) {
					double temp = matrix[0][j];
					matrix[0][j] = matrix[i][j];
					matrix[i][j] = -temp;
				}
				break;
			}
		}
		double[][] m = new double[size][size];
		for(int r = 0; r < size; r++) {
			for(int c = 0; c < size; c++) {
				m[r][c] = matrix[r + 1][c + 1];
				if(matrix[r][0] != 0) m[r][c] -= matrix[r + 1][0] / matrix[0][0] * matrix[0][c + 1];
			}
		}
		return matrix[0][0] * determinant(m);
	}
	
	private double[][] minor(double[][] matrix, int row, int col) {
		int size = matrix.length - 1;
		double[][] minor = new double[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				minor[i][j] = matrix[i < row ? i : i + 1][j < col ? j : j + 1];
			}
		}
		return minor;
	}
	
	public Matrix4f inverse() {
		double det = getDet();
		if(det == 0) return null;
		Matrix4f result = new Matrix4f();
		double[][] m = new double[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				m[i][j] = elements[i][j];
			}
		}
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				int sgn = (i + j) % 2 == 0 ? 1 : -1;
				result.elements[i][j] = (float) (sgn * determinant(minor(m, j, i)) / det);
			}
		}
		return result;
	}
	
	public FloatBuffer toFloatBuffer() {
		float[] buffer = new float[16];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				buffer[i + j * 4] = elements[i][j];
			}
		}
		return BufferUtils.createFloatBuffer(buffer);
	}

}
