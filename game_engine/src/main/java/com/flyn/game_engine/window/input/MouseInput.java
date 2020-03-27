package com.flyn.game_engine.window.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseInput extends GLFWMouseButtonCallback {

	private boolean[] keys = new boolean[GLFW_MOUSE_BUTTON_LAST];
	private double[][] pos = new double[2][1];
	private MouseInterface mouseInterface;
	
	public MouseInput(MouseInterface window) {
		mouseInterface = window;
	}

	@Override
	public void invoke(long window, int button, int action, int mods) {
		boolean state = action != GLFW_RELEASE;
		glfwGetCursorPos(window, pos[0], pos[1]);
		if(!keys[button] && state) mouseInterface.mousePressed(button, pos[0][0], pos[1][0]);
		if(keys[button] && !state) mouseInterface.mouseReleased(button, pos[0][0], pos[1][0]);
		keys[button] = state;
	}
	
}
