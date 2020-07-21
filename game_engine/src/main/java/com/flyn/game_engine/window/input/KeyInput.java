package com.flyn.game_engine.window.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyInput extends GLFWKeyCallback {
	
	private static ArrayList<KeyInterface> interfaces = new ArrayList<>();
	private static boolean[] keys = new boolean[GLFW_KEY_LAST];
	
	public static void addKeyListener(KeyInterface keyInterface) {
		interfaces.add(keyInterface);
	}
	
	public static boolean isKeyPressed(int keyCode) {
		if(keyCode >= GLFW_KEY_LAST) {
			System.err.println("Nope");
			return false;
		}
		return keys[keyCode];
	}
	
	public static boolean isKeyReleased(int keyCode) {
		if(keyCode >= GLFW_KEY_LAST) {
			System.err.println("Nope");
			return false;
		}
		return !keys[keyCode];
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		boolean state = action != GLFW_RELEASE;
		if(!keys[key] && state) {
			for(KeyInterface i : interfaces) i.keyPressed(key);
		}
		if(keys[key] && !state) {
			for(KeyInterface i : interfaces) i.keyReleased(key);
		}
		keys[key] = state;
	}

}
