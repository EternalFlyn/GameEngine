package com.flyn.game_engine.input;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

public class WindowResizeInput extends GLFWWindowSizeCallback {
	
	private static HashMap<Long, int[]> size = new HashMap<>();
	
	public WindowResizeInput(long window) {
		int[] width = new int[1], height = new int[1];
		GLFW.glfwGetWindowSize(window, width, height);
		size.put(window, new int[] {width[0], height[0]});
	}
	
	public static int[] getSize(long window) {
		return size.get(window);
	}

	@Override
	public void invoke(long window, int width, int height) {
		size.put(window, new int[] {width, height});
	}

}
