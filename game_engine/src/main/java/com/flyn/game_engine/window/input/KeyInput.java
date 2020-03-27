package com.flyn.game_engine.window.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyInput extends GLFWKeyCallback {
	
	private boolean[] keys = new boolean[GLFW_KEY_LAST];
	private KeyInterface keyInterface;
	
	public KeyInput(KeyInterface window) {
		keyInterface = window;
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		boolean state = action != GLFW_RELEASE;
		if(!keys[key] && state) keyInterface.keyPressed(key);
		if(keys[key] && !state) keyInterface.keyReleased(key);
		keys[key] = state;
	}

}
