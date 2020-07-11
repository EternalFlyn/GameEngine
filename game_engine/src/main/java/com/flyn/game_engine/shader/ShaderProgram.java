package com.flyn.game_engine.shader;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;

import org.lwjgl.opengl.GL20;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.utils.ShaderUtils;

public abstract class ShaderProgram {
	
	private int programID, vertexShaderID, fragmentShaderID;
	private HashMap<String, Integer> locationCache = new HashMap<String, Integer> ();
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		int[] ID = ShaderUtils.load(vertexFile, fragmentFile);
		programID = ID[0];
		vertexShaderID = ID[1];
		fragmentShaderID = ID[2];
		bindAttributes();
	}
	
	public int getUniform(String name) {
		if(locationCache.containsKey(name)) return locationCache.get(name);
		int result = glGetUniformLocation(programID, name);
		if (result == -1) System.err.println("Couldn't find uniform variable " + name + "!");
		else locationCache.put(name, result);
		return result;
	}
	
	public void setUniform1i(String name, int value) {
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value) {
		glUniform1f(getUniform(name), value);
	}
	
	public void setUniform2f(String name, float x, float y) {
		glUniform2f(getUniform(name), x, y);
	}
	
	public void setUniform3f(String name, Vector3f vector) {
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniform4f(String name, Matrix4f matrix) {
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
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
