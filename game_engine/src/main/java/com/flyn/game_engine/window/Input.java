package com.flyn.game_engine.window;

import static org.lwjgl.glfw.GLFW.*;

import com.flyn.game_engine.window.input.KeyInterface;
import com.flyn.game_engine.window.input.MouseInterface;

class Input implements KeyInterface, MouseInterface {
	
	private final long window;
	
	Input(long window) {
		this.window = window;
	}

	@Override
	public void keyPressed(int keyCode) {
		System.out.println("Key Pressed : " + keyCode);
	}

	@Override
	public void keyReleased(int keyCode) {
		System.out.println("Key Released : " + keyCode);
	}

	@Override
	public void mousePressed(int mouseButton, double x, double y) {
		System.out.printf("Mouse Pressed : %d, pos : (%f , %f)%n", mouseButton, x, y);
		if(mouseButton == GLFW_MOUSE_BUTTON_1) ;
		if(mouseButton == GLFW_MOUSE_BUTTON_2) ;
		if(mouseButton == GLFW_MOUSE_BUTTON_3) ;
	}

	@Override
	public void mouseReleased(int mouseButton, double x, double y) {
		System.out.printf("Mouse Released : %d, pos : (%f , %f)%n", mouseButton, x, y);
	}
	
}
