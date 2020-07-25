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

public class MasterRenderer {
	
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	
	private static Color skyColor = Color.cyan;
	
	private final float aspectRatio;
	
	private Matrix4f projectionMatrix;
	
	private TexturedShader entityShader = new TexturedShader();
	private EntityRenderer entityRenderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	
	private HashMap<RawModel, HashMap<Texture, ArrayList<Entity>>> entities = new HashMap<>();
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
	
	public void render(ArrayList<Light> lights, Camera camera) {
		prepare();
		
		entityShader.enable();
		entityShader.setLight(lights);
		entityShader.setViewPosition(camera.createViewMatrix());
		entityShader.setSkyColor(skyColor);
		entityRenderer.render(entities);
		entityShader.disable();
		
		terrainShader.enable();
		terrainShader.setLight(lights);
		terrainShader.setViewPosition(camera.createViewMatrix());
		terrainShader.setSkyColor(skyColor);
		terrainRenderer.render(terrains);
		terrainShader.disable();
		
		terrains.clear();
		entities.clear();
	}
	
	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void addEntity(Entity entity) {
		RawModel model = entity.getModel();
		Texture texture = entity.getTexture();
		ArrayList<Entity> list;
		if(entities.containsKey(model)) {
			HashMap<Texture, ArrayList<Entity>> textureMap = entities.get(model);
			if(textureMap.containsKey(texture)) {
				list = entities.get(model).get(texture);
			}
			else {
				list = new ArrayList<>();
				textureMap.put(texture, list);
			}
		}
		else {
			HashMap<Texture, ArrayList<Entity>> textureMap = new HashMap<>();
			list = new ArrayList<>();
			textureMap.put(texture, list);
			entities.put(model, textureMap);
		}
		list.add(entity);
	}
	
	public void prepare() {
		GL11.glClearColor(skyColor.getRed() / 255f, skyColor.getGreen() / 255f, skyColor.getBlue() / 255f, skyColor.getAlpha() / 255f);
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
