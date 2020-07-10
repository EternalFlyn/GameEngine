package com.flyn.game_engine.render;

public class TexturedModel {
	
	private RawModel model;
	private Texture texture;
	
	public TexturedModel(RawModel model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}

	public RawModel getModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}

}
