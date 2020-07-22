package com.flyn.game_engine.entity;

import java.awt.Color;

import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.RawModel;
import com.flyn.game_engine.render.Texture;
import com.flyn.game_engine.utils.FileUtils;
import com.flyn.game_engine.utils.Loader;

public class Dragon extends Entity {
	
	private static RawModel model = FileUtils.loadObjFile("src/main/java/model/dragon.obj");
	
	private Texture texture;
	
	public Dragon(Vector3f position, Vector3f rotation, Vector3f scale) {
		super(model, position, rotation, scale);
		setColor(Color.yellow);
	}

	public void setColor(Color color) {
		texture = new Texture(Loader.loadColorTexture(color));
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		setTexture(texture);
	}
	
}
