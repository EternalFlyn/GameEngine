package com.flyn.game_engine.window.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseMotionInput extends GLFWCursorPosCallback {
	
	private double x, y;
	private MouseMotionInterface window;
	
	public MouseMotionInput(MouseMotionInterface window) {
		this.window = window;
	}

	@Override
	public void invoke(long window, double xpos, double ypos) {
		if(x != xpos || y != ypos) {
			x = xpos;
			y = ypos;
			this.window.mouseMotion(xpos, ypos);
		}
	}

}
