package com.flyn.game_engine.basic;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.entity.object.Dragon;
import com.flyn.game_engine.entity.object.Player;
import com.flyn.game_engine.entity.object.Poppy;
import com.flyn.game_engine.entity.object.Stall;
import com.flyn.game_engine.entity.object.Torch;
import com.flyn.game_engine.gui.GuiRenderer;
import com.flyn.game_engine.gui.GuiTexture;
import com.flyn.game_engine.input.KeyInput;
import com.flyn.game_engine.input.MouseInput;
import com.flyn.game_engine.input.MouseMotionInput;
import com.flyn.game_engine.input.WheelInput;
import com.flyn.game_engine.input.WindowResizeInput;
import com.flyn.game_engine.math.Octree;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.terrain.Terrain;
import com.flyn.game_engine.utils.FileUtils;
import com.flyn.game_engine.utils.Loader;
import com.flyn.game_engine.water.WaterFrameBuffers;
import com.flyn.game_engine.water.WaterTile;

public class Window {

	private static long currentFrameTime = 0, lastFrameTime = 0, time = 0;
	private static ArrayList<ExecuteInterface> executes = new ArrayList<>();
	
	private long window, startedTime = 0;
	private GLFWVidMode vidmode;
	
	private ArrayList<Light> lights = new ArrayList<>();
	private ArrayList<GuiTexture> guis = new ArrayList<>();
	
	public Window() {
		if(!glfwInit()) {
			System.err.println("Couldn't initialize GLFW.");
			System.exit(-1);
		}
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()); //取得螢幕資訊
	}
	
	public void setWindow(String title, int width, int height) {
//		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); //設定視窗不可調整大小
		window = glfwCreateWindow(width, height, title, NULL, NULL); //創建視窗
		
		if(window == NULL) {
			System.err.println("Window couldn't be created.");
			System.exit(-1);
		}
		
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2); //設定螢幕位置
		
		//設定鍵盤和滑鼠輸入
		glfwSetKeyCallback(window, new KeyInput());
		glfwSetMouseButtonCallback(window, new MouseInput());
		glfwSetCursorPosCallback(window, new MouseMotionInput());
		glfwSetScrollCallback(window, new WheelInput());
		glfwSetWindowSizeCallback(window, new WindowResizeInput(window));
	}
	
	public void showWindow() {
		glfwShowWindow(window);
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		System.out.println("version : " + glGetString(GL_VERSION));
		
		startedTime = System.currentTimeMillis();
		MasterRenderer renderer = new MasterRenderer(window);
		GuiRenderer gui = new GuiRenderer(window);
		
		MousePicker picker = new MousePicker(window, renderer.getProjectionMatrix());
		
		Terrain terrain = new Terrain(0, -1);
		renderer.addTerrain(terrain);
//		Terrain terrain2 = new Terrain(-1, -1);
//		renderer.addTerrain(terrain2);
//		terrain2.setGrassColor(new Color(153, 255, 77));
		
		lights.add(new Light(new Vector3f(0, 10000, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0, 0)));
		lights.add(new Light(new Vector3f(1, 1, 1), new Vector3f(Color.red), new Vector3f(1, 0.1f, 0.02f)));
		lights.add(new Light(new Vector3f(1, 1, -1), new Vector3f(Color.green), new Vector3f(1, 0.1f, 0.02f)));
		lights.add(new Light(new Vector3f(-1, 1, -1), new Vector3f(Color.blue), new Vector3f(1, 0.1f, 0.02f)));

		guis.add(new GuiTexture(Loader.loadTexture("src/main/java/texture/64695689_p0.jpg"), 0.82f, 0, 0.18f, 0.32f));
		guis.add(new GuiTexture(Loader.loadTexture("src/main/java/texture/re_zero_rem.jpg"), 0.82f, 0.32f, 0.18f, 0.24f));
		guis.add(new GuiTexture(Loader.loadTexture("src/main/java/texture/icon_navigation_bar.png"), 0, 0, 0.045f, 0.08f));
		
		renderer.addEntity(new Stall(new Vector3f(7, 0, -10), new Vector3f(0, 180, 0), new Vector3f(1, 1, 1)));
		renderer.addEntity(new Dragon(new Vector3f(-7, 0, -15), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));
		
		Texture girlTexture = new Texture(Loader.loadColorTexture(Color.white));
		RawModel girlModel = FileUtils.loadObjFile("src/main/java/model/girl.obj");
		Entity girl = new Entity(girlModel, girlTexture, new Vector3f(-1, 0, -1), new Vector3f(-90, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f));
		renderer.addEntity(girl);
		Player player = new Player(girlModel, girlTexture, new Vector3f(0, 0, 0), new Vector3f(-90, 180, 0), new Vector3f(0.1f, 0.1f, 0.1f));
		renderer.addEntity(player);
		Camera camera = new Camera(player);
		
		for(int i = 0; i < 100; i++) {
			Random ran = new Random();
			float x = ran.nextFloat() * 20 - 10, z = ran.nextFloat() * 20 - 10;
			renderer.addEntity(new Poppy(new Vector3f(x, terrain.getHeight(x, z), z), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));
		}

		for(int i = 0; i < 100; i++) {
			Random ran = new Random();
			float x = ran.nextFloat() * 20 - 10, z = ran.nextFloat() * 20 - 10;
			renderer.addEntity(new Torch(new Vector3f(x, terrain.getHeight(x, z), z), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));
		}
		
		WaterTile water = new WaterTile(-2, 0, -1);
		renderer.addWater(water);
		WaterFrameBuffers fbos = new WaterFrameBuffers(window);
		guis.add(new GuiTexture(fbos.getReflectionTexture(), 0, 0, 0.2f, 0.25f));
		
//		long tt = System.nanoTime();
//		Octree o = FileUtils.test("src/main/java/model/dragon.obj");
//		renderer.addOctree(o);
//		System.out.println(System.nanoTime() - tt);

		while(!glfwWindowShouldClose(window)) {
			time = System.currentTimeMillis() - startedTime;
			lastFrameTime = currentFrameTime;
			currentFrameTime = System.currentTimeMillis();
			glfwPollEvents();
			for(int i = 0; i < executes.size(); i++) {
				if(executes.get(i).execute(time)) executes.remove(i);
			}
			player.move(terrain);
			camera.move();
//			System.out.println(picker.getCurrentRay(camera.createViewMatrix()));
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			fbos.bindReflectionFrameBuffer();
			renderer.render(time, lights, camera);
			fbos.unbindCurrentFrameBuffer();
			
			renderer.render(time, lights, camera);
			gui.render(guis);
			glfwSwapBuffers(window);
			girl.rotate(0, 1, 0);
//			System.out.println("FPS:" + 1 / getFrameTimeSeconds());
		}
		
		renderer.remove();
		gui.remove();
		Loader.clean();
		glfwTerminate();
	}
	
	public static float getFrameTimeSeconds() {
		return (currentFrameTime - lastFrameTime) / 1000f;
	}
	
	public static void addExecuteListener(ExecuteInterface execute) {
		executes.add(execute);
	}
	
}
