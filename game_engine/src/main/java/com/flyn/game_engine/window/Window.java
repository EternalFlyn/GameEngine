package com.flyn.game_engine.window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.flyn.game_engine.entity.Camera;
import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.entity.Light;
import com.flyn.game_engine.entity.Player;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.MasterRenderer;
import com.flyn.game_engine.render.RawModel;
import com.flyn.game_engine.render.Texture;
import com.flyn.game_engine.render.TexturedModel;
import com.flyn.game_engine.terrain.Terrain;
import com.flyn.game_engine.utils.FileUtils;
import com.flyn.game_engine.utils.Loader;
import com.flyn.game_engine.window.input.KeyInput;
import com.flyn.game_engine.window.input.MouseInput;
import com.flyn.game_engine.window.input.MouseMotionInput;

public class Window {
	
	public static long time = 0;
	public static Input input = new Input();
	
	private static long currentFrameTime = 0, lastFrameTime = 0;
	
	private int width = 0, height = 0;
	private long window, startedTime = 0;
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
		
		startedTime = System.currentTimeMillis();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(width, height);
		Light light = new Light(new Vector3f(0, 1, 0), new Vector3f(1, 1, 1));
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
		
		float[] normals = {
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1
		};
		
		RawModel model = loader.loadToVAO(indices, vertices, textureCoords, normals);
		Texture texture = new Texture(loader.loadTexture("src/main/java/texture/re_zero_rem.jpg"));
		texture.setShineDamper(10);
		texture.setReflectivity(0.3f);
		TexturedModel textureModel = new TexturedModel(model, texture);
		Entity entity = new Entity(textureModel, new Vector3f(0, 0.5f, -1), new Vector3f(), new Vector3f(1, 1, 1));
		
		Texture stallTexture = new Texture(loader.loadTexture("src/main/java/texture/stallTexture.png"));
		TexturedModel stallModel = new TexturedModel(FileUtils.loadObjFile(loader, "src/main/java/model/stall.obj"), stallTexture);
		Entity stall = new Entity(stallModel, new Vector3f(7, 0, -10), new Vector3f(0, 180, 0), new Vector3f(1, 1, 1));
		
		Texture girlTexture = new Texture(loader.loadColorTexture(Color.white));
		TexturedModel girlModel = new TexturedModel(FileUtils.loadObjFile(loader, "src/main/java/model/girl.obj"), girlTexture);
		Entity girl = new Entity(girlModel, new Vector3f(-7, 0, -10), new Vector3f(-90, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f));
		Player player = new Player(girlModel, new Vector3f(0, 0, -1), new Vector3f(-90, 180, 0), new Vector3f(0.1f, 0.1f, 0.1f));
		
		Texture dragonTexture = new Texture(loader.loadColorTexture(Color.yellow));
		dragonTexture.setShineDamper(10);
		dragonTexture.setReflectivity(1);
		TexturedModel dragonModel = new TexturedModel(FileUtils.loadObjFile(loader, "src/main/java/model/dragon.obj"), dragonTexture);
		Entity dragon = new Entity(dragonModel, new Vector3f(-7, 0, -15), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		
		Texture poppyTexture = new Texture(loader.loadTexture("src/main/java/texture/poppy.png"));
		poppyTexture.setTransparency(true);
		poppyTexture.setUsedFakeLight(true);
		TexturedModel poppyModel = new TexturedModel(FileUtils.loadObjFile(loader, "src/main/java/model/poppy.obj"), poppyTexture);
		Entity[] poppys = new Entity[100];
		for(int i = 0; i < poppys.length; i++) {
			Random ran = new Random();
			float x = ran.nextFloat() * 20 - 10, z = ran.nextFloat() * 20 - 10;
			poppys[i] = new Entity(poppyModel, new Vector3f(x, 0, z), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		}
		
		Terrain terrain = new Terrain(0, -1, loader);
		Terrain terrain2 = new Terrain(-1, -1, loader);
		terrain2.setGrassColor(new Color(153, 255, 77));
		
		while(!glfwWindowShouldClose(window)) {
			time = System.currentTimeMillis() - startedTime;
			lastFrameTime = currentFrameTime;
			currentFrameTime = System.currentTimeMillis();
			glfwPollEvents();
//			camera.move();
			player.move();
			renderer.addEntity(player);
			renderer.addEntity(dragon);
			renderer.addEntity(girl);
			renderer.addEntity(stall);
			renderer.addEntity(entity);
			for(Entity poppy : poppys) renderer.addEntity(poppy);
			renderer.addTerrain(terrain);
			renderer.addTerrain(terrain2);
			renderer.render(light, camera);
			glfwSwapBuffers(window);
			girl.rotate(0, 1, 0);
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		renderer.remove();
		loader.clean();
		glfwTerminate();
	}
	
	public static float getFrameTimeSeconds() {
		return (currentFrameTime - lastFrameTime) / 1000f;
	}
	
}
