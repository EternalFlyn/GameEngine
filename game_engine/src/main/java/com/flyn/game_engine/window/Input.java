package com.flyn.game_engine.window;

import static org.lwjgl.glfw.GLFW.*;

import com.flyn.game_engine.window.input.KeyInterface;
import com.flyn.game_engine.window.input.MouseInterface;
import com.flyn.game_engine.window.input.MouseMotionInterface;

public class Input implements KeyInterface, MouseInterface, MouseMotionInterface {
	
	private boolean[] keyState = new boolean[GLFW_KEY_LAST], mouseState = new boolean[GLFW_MOUSE_BUTTON_LAST];
	
	public boolean isKeyPressed(int keyCode) {
		return keyState[keyCode];
	}
	
	public boolean isKeyReleased(int keyCode) {
		return !keyState[keyCode];
	}
	
	public boolean isMousePressed(int mouseButton) {
		return mouseState[mouseButton];
	}
	
	public boolean isMouseReleased(int mouseButton) {
		return !mouseState[mouseButton];
	}

	@Override
	public void keyPressed(int keyCode) {
		keyState[keyCode] = true;
		System.out.println("Key Pressed : " + keyCode);
	}

	@Override
	public void keyReleased(int keyCode) {
		keyState[keyCode] = false;
		System.out.println("Key Released : " + keyCode);
	}

	@Override
	public void mousePressed(int mouseButton, double x, double y) {
		mouseState[mouseButton] = true;
		System.out.printf("Mouse Pressed : %d, pos : (%f , %f)%n", mouseButton, x, y);
		if(mouseButton == GLFW_MOUSE_BUTTON_1) ;
		if(mouseButton == GLFW_MOUSE_BUTTON_2) ;
		if(mouseButton == GLFW_MOUSE_BUTTON_3) ;
	}

	@Override
	public void mouseReleased(int mouseButton, double x, double y) {
		mouseState[mouseButton] = false;
		System.out.printf("Mouse Released : %d, pos : (%f , %f)%n", mouseButton, x, y);
	}

	@Override
	public void mouseMotion(double x, double y) {
//		System.out.printf("Mouse Motion, pos : (%f, %f)%n", x, y);
	}
	
}
