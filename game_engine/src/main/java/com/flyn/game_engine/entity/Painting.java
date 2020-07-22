package com.flyn.game_engine.entity;

import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.RawModel;
import com.flyn.game_engine.render.Texture;
import com.flyn.game_engine.utils.Loader;

public class Painting extends Entity {
	
	private static int[] indices = {0, 1, 2, 2, 3, 0};

	private static float[] vertices = {
			0.5f, 0.5f, 0,
			-0.5f, 0.5f, 0,
			-0.5f, -0.5f, 0,
			0.5f, -0.5f, 0
	};

	private static float[] textureCoords = {
		1, 0,
		0, 0,
		0, 1,
		1, 1
	};

	private static float[] normals = {
		0, 0, 1,
		0, 0, 1,
		0, 0, 1,
		0, 0, 1
	};
	
	private static RawModel model = Loader.loadToVAO(indices, vertices, textureCoords, normals);

	public Painting(Texture texture, Vector3f position, Vector3f rotation, Vector3f scale) {
		super(model, texture, position, rotation, scale);
	}
	
}