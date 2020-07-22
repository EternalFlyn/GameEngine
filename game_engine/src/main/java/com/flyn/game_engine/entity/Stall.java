package com.flyn.game_engine.entity;

import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.RawModel;
import com.flyn.game_engine.render.Texture;
import com.flyn.game_engine.utils.FileUtils;
import com.flyn.game_engine.utils.Loader;

public class Stall extends Entity {
	
	private static RawModel model = FileUtils.loadObjFile("src/main/java/model/stall.obj");
	private static Texture texture = new Texture(Loader.loadTexture("src/main/java/texture/stallTexture.png"));
	
	public Stall(Vector3f position, Vector3f rotation, Vector3f scale) {
		super(model, texture, position, rotation, scale);
	}

}
