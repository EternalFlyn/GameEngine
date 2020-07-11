package com.flyn.game_engine.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;

import org.lwjgl.opengl.GL20;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.utils.FileUtils;
public abstract class ShaderProgram {
	
	private int programID, vertexShaderID, fragmentShaderID;
	private HashMap<String, Integer> locationCache = new HashMap<String, Integer> ();
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		int[] ID = load(vertexFile, fragmentFile);
		programID = ID[0];
		vertexShaderID = ID[1];
		fragmentShaderID = ID[2];
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
	
	private int[] load(String vertexFile, String fragmentFile) {
		String vertexData = FileUtils.loadAsString(vertexFile);
		String fragmentData = FileUtils.loadAsString(fragmentFile);
		return create(vertexData, fragmentData);
	}
	
	private int[] create(String vertexData, String fragmentData) {
		int programID = glCreateProgram();
		int vertexID = glCreateShader(GL_VERTEX_SHADER);
		int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertexID, vertexData);
		glShaderSource(fragmentID, fragmentData);
		
		glCompileShader(vertexID);
		if(glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile vertex shader!");
			System.err.println(glGetShaderInfoLog(vertexID));
			return null;
		}
		
		glCompileShader(fragmentID);
		if(glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile fragment shader!");
			System.err.println(glGetShaderInfoLog(fragmentID));
			return null;
		}
		
		glAttachShader(programID, vertexID);
		glAttachShader(programID, fragmentID);
		
		bindAttributes();
		
		glLinkProgram(programID);
		if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Failed to link program!");
			System.err.println(glGetShaderInfoLog(programID));
			return null;
		}
		
		glValidateProgram(programID);
		if(glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
			System.err.println("Failed to validate program!");
			System.err.println(glGetShaderInfoLog(programID));
			return null;
		}
		
		return new int[] {programID, vertexID, fragmentID};
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
