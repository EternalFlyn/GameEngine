package com.flyn.game_engine.terrain;

import java.awt.Color;

import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.basic.Texture;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.utils.FileUtils;
import com.flyn.game_engine.utils.Loader;

public class Terrain {

	private static final int VERTEX_COUNT = 256;
	private static final float SIZE = 800, MAX_HEIGHT = 30, MAX_PIXEL_COLOR = 256 * 256 * 256;

	private float x, z;
	private float[][] heights;
	private Color grassColor = new Color(124, 252, 0);
	private RawModel model;
	private Texture backgroundTexture, rTexture, gTexture, bTexture, blendMap;

	public Terrain(int gridX, int gridZ) {
		x = gridX * SIZE;
		z = gridZ * SIZE;
		backgroundTexture = new Texture(Loader.loadTexture("src/main/java/texture/grass_block_top.png"));
		rTexture = new Texture(Loader.loadTexture("src/main/java/texture/sand.png"));
		gTexture = new Texture(Loader.loadTexture("src/main/java/texture/chiseled_quartz_block.png"));
		bTexture = new Texture(Loader.loadTexture("src/main/java/texture/stone_bricks.png"));
		blendMap = new Texture(Loader.loadTexture("src/main/java/texture/blendMap.png"));
		model = generateTerrain();
	}

	private RawModel generateTerrain() {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		heights = getHeight();
		float[] vertices = new float[3 * count], textureCoords = new float[2 * count], normals = new float[3 * count];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int pointer = 0;
		for(int z = 0; z < VERTEX_COUNT; z++){
			for(int x = 0 ;x < VERTEX_COUNT; x++){
				vertices[pointer * 3] = (float) x / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[pointer * 3 + 1] = heights[x][z];
				vertices[pointer * 3 + 2] = (float) z / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(x, z, heights);
				normals[pointer * 3] = normal.x;
				normals[pointer * 3 + 1] = normal.y;
				normals[pointer * 3 + 2] = normal.z;
				textureCoords[pointer * 2] = (float) x / ((float) VERTEX_COUNT - 1);
				textureCoords[pointer * 2 + 1] = (float) z / ((float) VERTEX_COUNT - 1);
				pointer++;
			}
		}
		pointer = 0;
		for(int gz = 0; gz < VERTEX_COUNT-1; gz++){
			for(int gx = 0; gx < VERTEX_COUNT-1; gx++){
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return Loader.loadToVAO(indices, vertices, textureCoords, normals);
	}
	
	private float[][] getHeight() {
		int[][] img = FileUtils.loadImage("src/main/java/texture/heightmap.png");
		float[][] result = new float[img[0][0]][img[0][1]];
		for(int z = 0; z < img[0][1]; z++) {
			for(int x = 0; x < img[0][0]; x++) {
				int pixel = img[1][z * img[0][0] + x] & 0xFFFFFF;
				float height = (float) pixel / MAX_PIXEL_COLOR;
				height = 2 * (height - 0.5f) * MAX_HEIGHT;
				result[x][z] = height;
			}
		}
		return result;
	}
	
	public float getHeight(float worldX, float worldZ) {
		float terrainX = worldX - x, terrainZ = worldZ - z;
		if(terrainX < 0 || terrainX >= SIZE || terrainZ < 0 || terrainZ >= SIZE) return 0;
		float gridSize = SIZE / (VERTEX_COUNT - 1);
		int gridX = (int) (terrainX / gridSize), gridZ = (int) (terrainZ / gridSize);
		float xCoord = (terrainX % gridSize) / gridSize;
		float zCoord = (terrainZ % gridSize) / gridSize;
		Vector3f p2 = new Vector3f(1, heights[gridX + 1][gridZ], 0), p3 = new Vector3f(0, heights[gridX][gridZ + 1], 1);
		if(terrainX + terrainZ > gridSize) return baryCentric(p2, new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), p3, xCoord, zCoord);
		else return baryCentric(new Vector3f(0, heights[gridX][gridZ], 0), p2, p3, xCoord, zCoord);
	}
	
	private Vector3f calculateNormal(int x, int z, float[][] heights) {
		float L = x < 1 || x > heights.length ? 0 : heights[x-1][z];
		float R = x < -1 || x > heights.length - 2 ? 0 : heights[x+1][z];
		float B = z < 1 || z > heights[x].length ? 0 : heights[x][z-1];
		float T = z < -1 || z > heights[x].length - 2 ? 0 : heights[x][z+1];
		Vector3f normal = new Vector3f(L - R, 2, B - T).normalise();
		return normal;
	}
	
	private float baryCentric(Vector3f p1, Vector3f p2, Vector3f p3, float x, float y) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (x - p3.x) + (p3.x - p2.x) * (y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (x - p3.x) + (p1.x - p3.x) * (y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public Texture getBackgroundTexture() {
		return backgroundTexture;
	}

	public Texture getRTexture() {
		return rTexture;
	}

	public Texture getGTexture() {
		return gTexture;
	}

	public Texture getBTexture() {
		return bTexture;
	}

	public Texture getBlendMap() {
		return blendMap;
	}

	public Color getGrassColor() {
		return grassColor;
	}

	public void setGrassColor(Color grassColor) {
		this.grassColor = grassColor;
	}

	public Matrix4f getTransformationMatirx() {
		return Matrix4f.translate(new Vector3f(x, 0, z));
	}

}
