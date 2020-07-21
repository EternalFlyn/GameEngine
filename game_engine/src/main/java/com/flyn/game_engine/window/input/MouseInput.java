package com.flyn.game_engine.window.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseInput extends GLFWMouseButtonCallback {
	
	private static ArrayList<MouseInterface> interfaces = new ArrayList<>();

	private static boolean[] buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];
	private double[] x = new double[1], y = new double[1];

	public static void addMouseListener(MouseInterface mouseInterface) {
		interfaces.add(mouseInterface);
	}
	
	public static boolean isButtonPressed(int buttonCode) {
		if(buttonCode >= GLFW_MOUSE_BUTTON_LAST) {
			System.err.println("Nope");
			return false;
		}
		return buttons[buttonCode];
	}
	
	public static boolean isButtonReleased(int buttonCode) {
		if(buttonCode >= GLFW_MOUSE_BUTTON_LAST) {
			System.err.println("Nope");
			return false;
		}
		return !buttons[buttonCode];
	}
	
	@Override
	public void invoke(long window, int button, int action, int mods) {
		boolean state = action != GLFW_RELEASE;
		glfwGetCursorPos(window, x, y);
		if(!buttons[button] && state) {
			for(MouseInterface i : interfaces) i.mousePressed(button, x[0], y[0]);
		}
		if(buttons[button] && !state) {
			for(MouseInterface i : interfaces) i.mouseReleased(button, x[0], y[0]);
		}
		buttons[button] = state;
	}
	
}
