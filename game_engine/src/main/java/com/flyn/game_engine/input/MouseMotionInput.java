package com.flyn.game_engine.input;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseMotionInput extends GLFWCursorPosCallback {
	
	private static ArrayList<MouseMotionInterface> interfaces = new ArrayList<>();
	
	private double x, y;
	
	public static void addMouseMotionListener(MouseMotionInterface mouseMotionInterface) {
		interfaces.add(mouseMotionInterface);
	}

	@Override
	public void invoke(long window, double xpos, double ypos) {
		if(x != xpos || y != ypos) {
			x = xpos;
			y = ypos;
			for(MouseMotionInterface i : interfaces) i.mouseMotion((int) xpos, (int) ypos);
		}
	}

}
