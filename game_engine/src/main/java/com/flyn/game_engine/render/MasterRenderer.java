package com.flyn.game_engine.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.flyn.game_engine.entity.Camera;
import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.entity.Light;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.shader.TerrainShader;
import com.flyn.game_engine.shader.TexturedShader;
import com.flyn.game_engine.terrain.Terrain;
import com.flyn.game_engine.window.Window;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	
	private static Color SKYCOLOR = Color.white;
	
	private final float aspectRatio;
	
	private float hue = 0;
	private Matrix4f projectionMatrix;
	
	private TexturedShader entityShader = new TexturedShader();
	private EntityRenderer entityRenderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	
	private HashMap<TexturedModel, ArrayList<Entity>> entities = new HashMap<>();
	private ArrayList<Terrain> terrains = new ArrayList<>();
	
	public MasterRenderer(int width, int height) {
		enableCulling();
		aspectRatio = (float) width / (float) height;
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(Light light, Camera camera) {
		float brightnessFactor = 1 - (float) (Window.time % 30000) / 15000.0f;
		float minBrightness = 0.6f * (brightnessFactor * brightnessFactor) + 0.1f;
		prepare(minBrightness);
		
		entityShader.enable();
		entityShader.setLight(light);
		entityShader.setViewPosition(camera.createViewMatrix());
		entityShader.setSkyColor(SKYCOLOR);
		entityShader.setMinBrightness(minBrightness);
		entityRenderer.render(entities);
		entityShader.disable();
		
		terrainShader.enable();
		terrainShader.setLight(light);
		terrainShader.setViewPosition(camera.createViewMatrix());
		terrainShader.setSkyColor(SKYCOLOR);
		terrainShader.setMinBrightness(minBrightness);
		terrainRenderer.render(terrains);
		terrainShader.disable();
		
		terrains.clear();
		entities.clear();
	}
	
	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void addEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		ArrayList<Entity> list;
		if(entities.containsKey(model)) {
			list = entities.get(model);
		}
		else {
			list = new ArrayList<>();
			entities.put(model, list);
		}
		list.add(entity);
	}
	
	public void prepare(float brightness) {
		hue += 0.01f;
		if(hue > 1) hue = 0;
		SKYCOLOR = Color.getHSBColor(0.5f, 1, brightness);
		GL11.glClearColor(SKYCOLOR.getRed() / 255f, SKYCOLOR.getGreen() / 255f, SKYCOLOR.getBlue() / 255f, SKYCOLOR.getAlpha() / 255f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	private void createProjectionMatrix() {
		float yScale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))), xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE; //zm
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.elements[0 + 0 * 4] = xScale;
		projectionMatrix.elements[1 + 1 * 4] = yScale;
		projectionMatrix.elements[2 + 2 * 4] = -(FAR_PLANE + NEAR_PLANE) / frustumLength;
		projectionMatrix.elements[2 + 3 * 4] = -(2 * FAR_PLANE * NEAR_PLANE) / frustumLength;
		projectionMatrix.elements[3 + 2 * 4] = -1;
	}
	
	public void remove() {
		entityShader.remove();
		terrainShader.remove();
	}

}
