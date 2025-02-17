package com.flyn.game_engine.basic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.entity.EntityRenderer;
import com.flyn.game_engine.entity.EntityShader;
import com.flyn.game_engine.entity.NormalMappingRenderer;
import com.flyn.game_engine.entity.NormalMappingShader;
import com.flyn.game_engine.input.WindowResizeInput;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector4f;
import com.flyn.game_engine.misc.Octree;
import com.flyn.game_engine.misc.OctreeRenderer;
import com.flyn.game_engine.misc.OctreeShader;
import com.flyn.game_engine.skybox.SkyboxRenderer;
import com.flyn.game_engine.skybox.SkyboxShader;
import com.flyn.game_engine.terrain.Terrain;
import com.flyn.game_engine.terrain.TerrainRenderer;
import com.flyn.game_engine.terrain.TerrainShader;

public class MasterRenderer {
	
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	
	private static Color skyColor = new Color(94, 134, 193); //Pale Denim
	
	private final long window;

	private float aspectRatio = 0;
	
	private Matrix4f projectionMatrix;
	
	private EntityShader entityShader = new EntityShader();
	private EntityRenderer entityRenderer;
	
	private NormalMappingShader normalMappingShader = new NormalMappingShader();
	private NormalMappingRenderer normalMappingRenderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	
	private SkyboxShader skyboxShader = new SkyboxShader();
	private SkyboxRenderer skyboxRenderer;
	
	private OctreeShader octreeShader = new OctreeShader();
	private OctreeRenderer octreeRenderer;
	
	private HashMap<RawModel, HashMap<Texture, ArrayList<Entity>>> entities = new HashMap<>(), normalMappingEntites = new HashMap<>();
	private ArrayList<Terrain> terrains = new ArrayList<>();
	private ArrayList<Octree> tree = new ArrayList<>();
	
	public MasterRenderer(long window) {
		enableCulling();
		this.window = window;
		int[] size = WindowResizeInput.getSize(window);
		aspectRatio = (float) size[0] / (float) size[1];
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		normalMappingRenderer = new NormalMappingRenderer(normalMappingShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(skyboxShader, projectionMatrix);
		octreeRenderer = new OctreeRenderer();
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(long time, ArrayList<Light> lights, Matrix4f view, Vector4f clipPlane) {
		sizeCheck();
		prepare();
		
		entityShader.enable();
		entityShader.setViewPosition(view);
		entityShader.setLight(lights);
		entityShader.setSkyColor(skyColor);
		entityShader.setClipPlane(clipPlane);
		entityRenderer.render(entities);
		entityShader.disable();
		
		normalMappingShader.enable();
		normalMappingShader.setViewPosition(view);
		normalMappingShader.setLight(lights, view);
		normalMappingShader.setSkyColor(skyColor);
		normalMappingShader.setClipPlane(clipPlane);
		normalMappingRenderer.render(normalMappingEntites);
		normalMappingShader.disable();
		
		terrainShader.enable();
		terrainShader.setViewPosition(view);
		terrainShader.setLight(lights);
		terrainShader.setSkyColor(skyColor);
		terrainShader.setClipPlane(clipPlane);
		terrainRenderer.render(terrains);
		terrainShader.disable();
		
		disableCulling();
		skyboxShader.enable();
		Matrix4f followView = new Matrix4f();
		followView.fill(view);
		followView.elements[0][3] = 0;
		followView.elements[1][3] = 0;
		followView.elements[2][3] = 0;
		Matrix4f skyRotate = Matrix4f.pitch((float) time / 1000.0f);
		skyboxShader.setViewPosition((Matrix4f) followView.multiply(skyRotate));
		skyboxShader.setSkyColor(skyColor);
		skyboxShader.setBlendFactor(dayNightSystem(time));
		skyboxRenderer.render();
		skyboxShader.disable();
		enableCulling();
		
		octreeShader.enable();
		octreeShader.setViewPosition(view);
		octreeRenderer.render(tree);
		octreeShader.disable();
	}
	
	public void addOctree(Octree octree) {
		tree.add(octree);
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
	
	public void addNormalMappingEntity(Entity entity) {
		RawModel model = entity.getModel();
		Texture texture = entity.getTexture();
		ArrayList<Entity> list;
		if(normalMappingEntites.containsKey(model)) {
			HashMap<Texture, ArrayList<Entity>> textureMap = normalMappingEntites.get(model);
			if(textureMap.containsKey(texture)) {
				list = normalMappingEntites.get(model).get(texture);
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
			normalMappingEntites.put(model, textureMap);
		}
		list.add(entity);
	}
	
	public void prepare() {
		GL11.glClearColor(skyColor.getRed() / 255f, skyColor.getGreen() / 255f, skyColor.getBlue() / 255f, skyColor.getAlpha() / 255f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void remove() {
		entityShader.remove();
		terrainShader.remove();
		skyboxShader.remove();
		octreeShader.remove();
	}
	
	private float dayNightSystem(long time) {
		//0 day texture, 1 night texture
		float hour = (int) (time % 24000);
		if(hour > 7000 && hour <= 17000) return 0;
		else if(hour > 5000 && hour <= 7000) return 1 - (hour - 5000) / 2000;
		else if(hour > 17000 && hour <= 19000) return (hour - 17000) / 2000;
		else return 1;
	}
	
	public void sizeCheck() {
		int[] size = WindowResizeInput.getSize(window);
		float newAspectRatio = (float) size[0] / (float) size[1];
		if(aspectRatio != newAspectRatio) {
			aspectRatio = newAspectRatio;
			createProjectionMatrix();
			GL11.glViewport(0, 0, size[0], size[1]);
			setProjectionMatrix();
		}
	}
	
	private void setProjectionMatrix() {
		entityRenderer.setProjectionMatrix(projectionMatrix);
		terrainRenderer.setProjectionMatrix(projectionMatrix);
		skyboxRenderer.setProjectionMatrix(projectionMatrix);
		
		octreeShader.enable();
		octreeShader.setProjection(projectionMatrix);
		octreeShader.disable();
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	private void createProjectionMatrix() {
		float yScale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))), xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE; //zm
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.elements[0][0] = xScale;
		projectionMatrix.elements[1][1] = yScale;
		projectionMatrix.elements[2][2] = -(FAR_PLANE + NEAR_PLANE) / frustumLength;
		projectionMatrix.elements[2][3] = -(2 * FAR_PLANE * NEAR_PLANE) / frustumLength;
		projectionMatrix.elements[3][2] = -1;
	}
	
}
