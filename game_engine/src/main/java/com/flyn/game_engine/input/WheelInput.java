package com.flyn.game_engine.input;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWScrollCallback;

public class WheelInput extends GLFWScrollCallback {
	
	private static ArrayList<WheelInterface> interfaces = new ArrayList<>();

	public static void addWheelListener(WheelInterface wheelInterface) {
		interfaces.add(wheelInterface);
	}
	
	@Override
	public void invoke(long window, double xoffset, double yoffset) {
		for(WheelInterface i : interfaces) i.wheelScroll((int) xoffset, (int) yoffset);
	}

}
