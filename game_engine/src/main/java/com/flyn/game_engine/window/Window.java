package com.flyn.game_engine.window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.Camera;
import com.flyn.game_engine.render.RawModel;
import com.flyn.game_engine.render.Renderer;
import com.flyn.game_engine.render.Texture;
import com.flyn.game_engine.render.TexturedModel;
import com.flyn.game_engine.shader.DefaultShader;
import com.flyn.game_engine.shader.TexturedShader;
import com.flyn.game_engine.utils.FileUtils;
import com.flyn.game_engine.utils.Loader;
import com.flyn.game_engine.window.input.KeyInput;
import com.flyn.game_engine.window.input.MouseInput;
import com.flyn.game_engine.window.input.MouseMotionInput;

public class Window {
	
	public static Input input = new Input();
	
	private int width = 0, height = 0;
	private long window;
	private GLFWVidMode vidmode;
	
	public Window() {
		if(!glfwInit()) {
			System.err.println("Couldn't initialize GLFW.");
			System.exit(-1);
		}
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()); //取得螢幕資訊
	}
	
	public void setWindow(String title, int width, int height) {
		this.width = width;
		this.height = height;
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); //設定視窗不可調整大小
		window = glfwCreateWindow(width, height, title, NULL, NULL); //創建視窗
		
		if(window == NULL) {
			System.err.println("Window couldn't be created.");
			System.exit(-1);
		}
		
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2); //設定螢幕位置
		
		//設定鍵盤和滑鼠輸入
		glfwSetKeyCallback(window, new KeyInput(input));
		glfwSetMouseButtonCallback(window, new MouseInput(input));
		glfwSetCursorPosCallback(window, new MouseMotionInput(input));
		
	}
	
	public void showWindow() {
		glfwShowWindow(window);
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		System.out.println("version : " + glGetString(GL_VERSION));
		
		Loader loader = new Loader();
		DefaultShader shader = new DefaultShader();
		TexturedShader textureShader = new TexturedShader();
		Renderer renderer = new Renderer(textureShader, width, height);
		Camera camera = new Camera();
		
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
		Entity entity = new Entity(textureModel, new Vector3f(0, 0, -1), new Vector3f(), new Vector3f(1, 1, 1));
		
		Texture stallTexture = new Texture(loader.loadTexture("src/main/java/texture/stallTexture.png"));
		TexturedModel stallModel = new TexturedModel(FileUtils.loadObjFile(loader, "src/main/java/model/stall.obj"), stallTexture);
		Entity stall = new Entity(stallModel, new Vector3f(0, -2.5f, -10), new Vector3f(0, 180, 0), new Vector3f(1, 1, 1));
		
		float s = 0.02f;
		
		while(!glfwWindowShouldClose(window)) {
			glfwPollEvents();
			camera.move();
			renderer.prepare();
			textureShader.enable();
			textureShader.setUniform4f("view", camera.createViewMatrix());
			renderer.render(stall, textureShader);
			renderer.render(entity, textureShader);
			textureShader.disable();
			glfwSwapBuffers(window);
			stall.rotate(0, 1, 0);
//			entity.move(0, 0, -0.01f);
//			if(s > 0 && entity.getScale().x > 1.5f) s = -0.01f;
//			else if(s < 0 && entity.getScale().x < 1) s = 0.02f;
//			entity.zoom(1.3f * s, s, 0);
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
