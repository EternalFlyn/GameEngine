package com.flyn.game_engine.utils;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.basic.RawModel;

public class Loader {
	
	private static ArrayList<Integer> vaos = new ArrayList<>(), vbos = new ArrayList<>(), textures = new ArrayList<>();
	
	public static RawModel loadToVAO(int[] indices, float[] vertices, float[] textureCoords, float[] normals) {
		int vaoID = createVAO();
		int indicesID = bindIndices(indices);
		storeAttribute(0, 3, vertices);
		storeAttribute(1, 2, textureCoords);
		storeAttribute(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indicesID, indices.length);
	}
	
	public static RawModel loadToVAO(int[] indices, float[] vertices, float[] textureCoords, float[] normals, float[] tangents) {
		int vaoID = createVAO();
		int indicesID = bindIndices(indices);
		storeAttribute(0, 3, vertices);
		storeAttribute(1, 2, textureCoords);
		storeAttribute(2, 3, normals);
		storeAttribute(3, 3, tangents);
		unbindVAO();
		return new RawModel(vaoID, indicesID, indices.length);
	}
	
	public static RawModel loadToVAO(float[] vertices, int dimension) {
		int vaoID = createVAO();
		storeAttribute(0, dimension, vertices);
		unbindVAO();
		return new RawModel(vaoID, 0, vertices.length / dimension);
	}
	
	public static int loadTexture(String imagePath) {
		int[][] img = FileUtils.loadImage(imagePath);
		int result = GL11.glGenTextures();
		textures.add(result);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, img[0][0], img[0][1], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(img[1]));
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return result;
	}
	
	public static int loadColorTexture(Color color) {
		int pixel = color.getRGB();
		int a = (pixel & 0xFF000000) >> 24;
		int r = (pixel & 0xFF0000) >> 16;
		int g = (pixel & 0xFF00) >> 8;
		int b = (pixel & 0xFF);
		int[] data = new int[] {a << 24 | b << 16 | g << 8 | r};
		
		int result = GL11.glGenTextures();
		textures.add(result);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 1, 1, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return result;
	}
	
	public static int loadCubeMap(String[] imagePaths) {
		int result = GL11.glGenTextures();
		textures.add(result);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, result);
		for(int i = 0; i < imagePaths.length; i++) {
			int[][] img = FileUtils.loadImage(imagePaths[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, img[0][0], img[0][1], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(img[1]));
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		return result;
	}
	
	public static void clean() {
		for(int vao : vaos) GL30.glDeleteVertexArrays(vao);
		for(int vbo : vbos) GL15.glDeleteBuffers(vbo);
		for(int texture : textures) GL11.glDeleteTextures(texture);
	}
	
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private static void storeAttribute(int attributeNumber, int dataSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(data), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, dataSize, GL20.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private static int bindIndices(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices), GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

}
