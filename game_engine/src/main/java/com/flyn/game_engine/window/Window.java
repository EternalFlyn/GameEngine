package com.flyn.game_engine.window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.flyn.game_engine.entity.Camera;
import com.flyn.game_engine.entity.Dragon;
import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.entity.Light;
import com.flyn.game_engine.entity.Painting;
import com.flyn.game_engine.entity.Player;
import com.flyn.game_engine.entity.Poppy;
import com.flyn.game_engine.entity.Stall;
import com.flyn.game_engine.entity.Torch;
import com.flyn.game_engine.gui.GuiRenderer;
import com.flyn.game_engine.gui.GuiTexture;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.MasterRenderer;
import com.flyn.game_engine.render.RawModel;
import com.flyn.game_engine.render.Texture;
import com.flyn.game_engine.terrain.Terrain;
import com.flyn.game_engine.utils.FileUtils;
import com.flyn.game_engine.utils.Loader;
import com.flyn.game_engine.window.input.KeyInput;
import com.flyn.game_engine.window.input.MouseInput;
import com.flyn.game_engine.window.input.MouseMotionInput;
import com.flyn.game_engine.window.input.WheelInput;

public class Window {

	private static long currentFrameTime = 0, lastFrameTime = 0, time = 0;
	private static ArrayList<ExecuteInterface> executes = new ArrayList<>();
	
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
		glfwSetKeyCallback(window, new KeyInput());
		glfwSetMouseButtonCallback(window, new MouseInput());
		glfwSetCursorPosCallback(window, new MouseMotionInput());
		glfwSetScrollCallback(window, new WheelInput());
	}
	
	public void showWindow() {
		glfwShowWindow(window);
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		System.out.println("version : " + glGetString(GL_VERSION));
		
		startedTime = System.currentTimeMillis();
		MasterRenderer renderer = new MasterRenderer(width, height);
		GuiRenderer gui = new GuiRenderer(window);
		
		Terrain terrain = new Terrain(0, -1);
//		Terrain terrain2 = new Terrain(-1, -1);
//		terrain2.setGrassColor(new Color(153, 255, 77));
		
		Texture texture = new Texture(Loader.loadTexture("src/main/java/texture/re_zero_rem.jpg"));
		texture.setShineDamper(10);
		texture.setReflectivity(0.3f);
		Painting entity = new Painting(texture, new Vector3f(0, 0.5f, -1), new Vector3f(), new Vector3f(1, 1, 1));
		
		ArrayList<GuiTexture> guis = new ArrayList<>();
		guis.add(new GuiTexture(Loader.loadTexture("src/main/java/texture/64695689_p0.jpg"), 550, 0, 250, 250));
		guis.add(new GuiTexture(Loader.loadTexture("src/main/java/texture/icon_navigation_bar.png"), 0, 0, 75, 75));
		
		Stall stall = new Stall(new Vector3f(7, 0, -10), new Vector3f(0, 180, 0), new Vector3f(1, 1, 1));
		Dragon dragon = new Dragon(new Vector3f(-7, 0, -15), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		
		Texture girlTexture = new Texture(Loader.loadColorTexture(Color.white));
		RawModel girlModel = FileUtils.loadObjFile("src/main/java/model/girl.obj");
		Entity girl = new Entity(girlModel, girlTexture, new Vector3f(-1, 0, -1), new Vector3f(-90, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f));
		Player player = new Player(girlModel, girlTexture, new Vector3f(0, 0, 0), new Vector3f(-90, 180, 0), new Vector3f(0.1f, 0.1f, 0.1f));
		Camera camera = new Camera(player);
		Light light = new Light(new Vector3f(0, 1, 0), new Vector3f(1, 1, 1));
		
		Poppy[] poppys = new Poppy[100];
		for(int i = 0; i < poppys.length; i++) {
			Random ran = new Random();
			float x = ran.nextFloat() * 20 - 10, z = ran.nextFloat() * 20 - 10;
			poppys[i] = new Poppy(new Vector3f(x, terrain.getHeight(x, z), z), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		}
		
		Torch[] torchs = new Torch[100];
		for(int i = 0; i < torchs.length; i++) {
			Random ran = new Random();
			float x = ran.nextFloat() * 20 - 10, z = ran.nextFloat() * 20 - 10;
			torchs[i] = new Torch(new Vector3f(x, terrain.getHeight(x, z), z), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		}

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
			renderer.addEntity(player);
			renderer.addEntity(dragon);
			renderer.addEntity(girl);
			renderer.addEntity(stall);
			renderer.addEntity(entity);
			for(Entity poppy : poppys) renderer.addEntity(poppy);
			for(Entity torch : torchs) renderer.addEntity(torch);
			renderer.addTerrain(terrain);
//			renderer.addTerrain(terrain2);
			renderer.render(light, camera);
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
