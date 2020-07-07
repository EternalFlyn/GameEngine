package com.flyn.game_engine.utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {

	private ShaderUtils() {}
	
	public static int[] load(String vertexFile, String fragmentFile) {
		String vertexData = FileUtils.loadAsString(vertexFile);
		String fragmentData = FileUtils.loadAsString(fragmentFile);
		return create(vertexData, fragmentData);
	}
	
	public static int[] create(String vertexData, String fragmentData) {
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
	
}
