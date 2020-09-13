package com.flyn.game_engine.terrain;

import java.awt.Color;

import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.basic.Texture;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.misc.PerlinNoise;
import com.flyn.game_engine.utils.FileUtils;
import com.flyn.game_engine.utils.Loader;

public class Terrain {

	private static final int VERTEX_COUNT = 256;
	private static final float SIZE = 256, MAX_HEIGHT = 30, MAX_PIXEL_COLOR = 256 * 256 * 256;
	private static final PerlinNoise noise = new PerlinNoise();

	private float x, z;
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
		float[] vertices = new float[3 * count], textureCoords = new float[2 * count], normals = new float[3 * count];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int pointer = 0;
		for(int z = 0; z < VERTEX_COUNT; z++){
			for(int x = 0 ;x < VERTEX_COUNT; x++){
				float px = this.x + x * SIZE / (float) VERTEX_COUNT, pz = this.z + z * SIZE / (float) VERTEX_COUNT;
				vertices[pointer * 3] = (float) x / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[pointer * 3 + 1] = getHeight(px, pz);
				vertices[pointer * 3 + 2] = (float) z / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(px, pz);
				normals[pointer * 3] = normal.x();
				normals[pointer * 3 + 1] = normal.y();
				normals[pointer * 3 + 2] = normal.z();
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
	
	private Vector3f calculateNormal(float x, float z) {
		float d = SIZE / (float) VERTEX_COUNT;
		float L = getHeight(x - d, z);
		float R = getHeight(x + d, z);
		float B = getHeight(x, z - d);
		float T = getHeight(x, z + d);
		Vector3f normal = (Vector3f) new Vector3f(L - R, 2, B - T).normalize();
		return normal;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
	public static float getHeight(float x, float z) {
		return noise.getPerlinNoise(x / 512.0f, z / 512.0f) * MAX_HEIGHT;
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
