package com.flyn.game_engine.window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.Color;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.flyn.game_engine.window.input.KeyInput;
import com.flyn.game_engine.window.input.MouseInput;

public class Window {
	
	private static final float COLOR_MAX = 255;
	
	private boolean isRunning = true;
	private float hue = 0;
	private long window;
	private Color backgroundColor;
	private GLFWVidMode vidmode;
	
	public Window() {
		if(!glfwInit()) {
			System.err.println("Couldn't initialize GLFW.");
			System.exit(-1);
		}
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()); //取得螢幕資訊
	}
	
	public void setWindow(String title, int width, int height) {
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); //設定視窗不可調整大小
		window = glfwCreateWindow(width, height, title, NULL, NULL); //創建視窗
		
		if(window == NULL) {
			System.err.println("Window couldn't be created.");
			System.exit(-1);
		}
		
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2); //設定螢幕位置
		
		//設定鍵盤和滑鼠輸入
		glfwSetKeyCallback(window, new KeyInput(new Input(window)));
		glfwSetMouseButtonCallback(window, new MouseInput(new Input(window)));
		
	}
	
	protected void render() {
		hue += 0.01f;
		if(hue > 1) hue = 0;
		backgroundColor = Color.getHSBColor(hue, 1, 1);
		glClearColor(backgroundColor.getRed() / COLOR_MAX, backgroundColor.getGreen() / COLOR_MAX, backgroundColor.getBlue() / COLOR_MAX, backgroundColor.getAlpha() / COLOR_MAX);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glfwSwapBuffers(window);
	}
	
	protected void stop() {
		glfwTerminate();
	}
	
	public void run() {
		glfwPollEvents();
		render();
		if(glfwWindowShouldClose(window)) {
			isRunning = false;
			stop();
		}
	}
	
	public void showWindow() {
		glfwShowWindow(window);
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		System.out.println("version : " + glGetString(GL_VERSION));
		while(isRunning) {
			run();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
