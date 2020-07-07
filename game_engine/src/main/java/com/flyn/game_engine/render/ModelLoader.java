package com.flyn.game_engine.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.utils.BufferUtils;

public class ModelLoader {
	
	private ArrayList<Integer> vaos = new ArrayList<>(), vbos = new ArrayList<>();
	
	public RawModel loadToVAO(int[] indices, float[] positions) {
		int vaoID = createVAO();
		int indicesID = bindIndices(indices);
		storeAttribute(0, 3, positions);
		unbindVAO();
		return new RawModel(vaoID, indicesID, indices.length);
	}
	
	public void clean() {
		for(int vao : vaos) GL30.glDeleteVertexArrays(vao);
		for(int vbo : vbos) GL15.glDeleteBuffers(vbo);
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeAttribute(int attributeNumber, int dataSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(data), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, dataSize, GL20.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private int bindIndices(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices), GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

}
