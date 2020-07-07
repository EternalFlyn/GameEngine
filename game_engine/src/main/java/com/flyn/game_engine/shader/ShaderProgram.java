package com.flyn.game_engine.shader;

import org.lwjgl.opengl.GL20;

import com.flyn.game_engine.utils.ShaderUtils;

public abstract class ShaderProgram {
	
	private int programID, vertexShaderID, fragmentShaderID;
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		int[] ID = ShaderUtils.load(vertexFile, fragmentFile);
		programID = ID[0];
		vertexShaderID = ID[1];
		fragmentShaderID = ID[2];
		bindAttributes();
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attributeIndex, String attributeName) {
		GL20.glBindAttribLocation(programID, attributeIndex, attributeName);
	}
	
	public void enable() {
		GL20.glUseProgram(programID);
	}
	
	public void disable() {
		GL20.glUseProgram(0);
	}
	
	public void remove() {
		disable();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
}
