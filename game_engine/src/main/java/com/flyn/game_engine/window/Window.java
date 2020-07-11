package com.flyn.game_engine.window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.Color;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.RawModel;
import com.flyn.game_engine.render.Renderer;
import com.flyn.game_engine.render.Texture;
import com.flyn.game_engine.render.TexturedModel;
import com.flyn.game_engine.shader.DefaultShader;
import com.flyn.game_engine.shader.TexturedShader;
import com.flyn.game_engine.utils.Loader;
import com.flyn.game_engine.window.input.KeyInput;
import com.flyn.game_engine.window.input.MouseInput;

public class Window {
	
	private static final float COLOR_MAX = 255;
	
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
	
	public void showWindow() {
		glfwShowWindow(window);
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		System.out.println("version : " + glGetString(GL_VERSION));
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		DefaultShader shader = new DefaultShader();
		TexturedShader textureShader = new TexturedShader();
		
		int[] indices = {0, 1, 2, 2, 3, 0};
		
		float[] vertices = {
				0.5f, 0.5f, 0,
				-0.5f, 0.5f, 0,
				-0.5f, -0.5f, 0,
				0.5f, -0.5f, 0
		};
		
		float[] textureCoords = {
			1, 0,
			0, 0,
			0, 1,
			1, 1
		};
		
		RawModel model = loader.loadToVAO(indices, vertices, textureCoords);
		Texture texture = new Texture(loader.loadTexture("src/main/java/texture/re_zero_rem.jpg"));
		TexturedModel textureModel = new TexturedModel(model, texture);
		Entity entity = new Entity(textureModel, new Vector3f(), new Vector3f(), new Vector3f(1.3f, 1, 1));
		float s = 0.02f;
		
		while(!glfwWindowShouldClose(window)) {
			glfwPollEvents();
			renderer.prepare();
			textureShader.enable();
			renderer.render(entity, textureShader);
			textureShader.disable();
			glfwSwapBuffers(window);
//			entity.rotate(0, 0, 1);
			if(s > 0 && entity.getScale().x > 1.5f) s = -0.01f;
			else if(s < 0 && entity.getScale().x < 1) s = 0.02f;
			entity.zoom(1.3f * s, s, 0);
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		shader.remove();
		loader.clean();
		glfwTerminate();
	}
	
}
