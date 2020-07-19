package com.flyn.game_engine.terrain;

import java.awt.Color;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.RawModel;
import com.flyn.game_engine.render.Texture;
import com.flyn.game_engine.utils.Loader;

public class Terrain {

	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;

	private float x, z;
	private Color grassColor = new Color(124, 252, 0);
	private RawModel model;
	private Texture backgroundTexture, rTexture, gTexture, bTexture, blendMap;

	public Terrain(int gridX, int gridZ, Loader loader) {
		x = gridX * SIZE;
		z = gridZ * SIZE;
		backgroundTexture = new Texture(loader.loadTexture("src/main/java/texture/grass_block_top.png"));
		rTexture = new Texture(loader.loadTexture("src/main/java/texture/sand.png"));
		gTexture = new Texture(loader.loadTexture("src/main/java/texture/chiseled_quartz_block.png"));
		bTexture = new Texture(loader.loadTexture("src/main/java/texture/stone_bricks.png"));
		blendMap = new Texture(loader.loadTexture("src/main/java/texture/blendMap.png"));
		model = generateTerrain(loader);
	}

	private RawModel generateTerrain(Loader loader) {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[3 * count], textureCoords = new float[2 * count], normals = new float[3 * count];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int pointer = 0;
		for(int i = 0; i < VERTEX_COUNT; i++){
			for(int j = 0 ;j < VERTEX_COUNT; j++){
				vertices[pointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[pointer * 3 + 1] = 0;
				vertices[pointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				normals[pointer * 3] = 0;
				normals[pointer * 3 + 1] = 1;
				normals[pointer * 3 + 2] = 0;
				textureCoords[pointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[pointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
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
		return loader.loadToVAO(indices, vertices, textureCoords, normals);
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
