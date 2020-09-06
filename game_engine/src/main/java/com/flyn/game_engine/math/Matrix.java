package com.flyn.game_engine.math;

public class Matrix {
	
	public final int row, column;
	public final float[][] elements;
	
	public Matrix(int row, int column) {
		this.row = row;
		this.column = column;
		elements = new float[row][column];
	}
	
	public Matrix scale(float scale) {
		Matrix result = newMatrixObject(row, column);
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < column; j++) {
				result.elements[i][j] = scale * elements[i][j];
			}
		}
		return result;
	}
	
	public Matrix plus(Matrix matrix) {
		if(row != matrix.row || column != matrix.column) {
			try {
				throw new MatrixSizeException();
			} catch (MatrixSizeException e) {
				e.printStackTrace();
			}
			System.exit(-1);
		}
		Matrix result = newMatrixObject(row, matrix.column);
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < column; j++) {
				result.elements[i][j] = elements[i][j] + matrix.elements[i][j];
			}
		}
		return result;
	}
	
	public Matrix minus(Matrix matrix) {
		if(row != matrix.row || column != matrix.column) {
			try {
				throw new MatrixSizeException();
			} catch (MatrixSizeException e) {
				e.printStackTrace();
			}
			System.exit(-1);
		}
		Matrix result = newMatrixObject(row, matrix.column);
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < column; j++) {
				result.elements[i][j] = elements[i][j] - matrix.elements[i][j];
			}
		}
		return result;
	}
	
	public Matrix multiply(Matrix matrix) {
		if(column != matrix.row) {
			try {
				throw new MatrixSizeException();
			} catch (MatrixSizeException e) {
				e.printStackTrace();
			}
			System.exit(-1);
		}
		
		Matrix result = newMatrixObject(row, matrix.column);
		
		for(int y = 0; y < result.column; y++) {
			for(int x = 0; x < result.row; x++) {
				float sum = 0;
				for(int a = 0; a < column; a++) {
					sum += elements[x][a] * matrix.elements[a][y];
				}
				result.elements[x][y] = sum;
			}
		}
		
		return result;
	}
	
	public void fill(Matrix m) {
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < column; j++) {
				if(i >= m.row || j >= m.column) elements[i][j] = 0;
				else elements[i][j] = m.elements[i][j];
			}
		}
	}
	
	static Matrix newMatrixObject(int row, int column) {
		if(column == 1) return Vector.newVectorObject(row);
		if(row == column) {
			switch(row) {
			case 4:
				return new Matrix4f();
			}
		}
		return new Matrix(row, column);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString()).append("\n");
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < column; j++) {
				sb.append(elements[i][j]);
				if(j != column - 1) sb.append(" ");
			}
			if(i != row - 1) sb.append("\n");
		}
		return sb.toString();
	}
	
	private class MatrixSizeException extends Exception {
		
		private static final long serialVersionUID = 1522732144138327502L;

		public MatrixSizeException() {
			super("Multiplied matrix size not the same.");
		}
		
	}

}
