package com.flyn.game_engine.entity;

import java.awt.Color;

import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.basic.Texture;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.utils.Loader;

public class Entity {
	
	private int texturedIndex = 0;
	private RawModel model;
	private Texture texture;
	private Vector3f position, rotation, scale;
	private Matrix4f transformationMatirx;
	
	public Entity(RawModel model, Texture texture, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.model = model;
		this.texture = texture;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		updateMatrix();
	}
	
	public Entity(RawModel model, Texture texture, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.texture = texture;
		this.position = position;
		this.rotation = rotation;
		this.scale = new Vector3f(scale, scale, scale);
		updateMatrix();
	}
	
	protected Entity(RawModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.model = model;
		this.texture = new Texture(Loader.loadColorTexture(Color.white));
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		updateMatrix();
	}

	public float getTextureXOffset() {
		int col = texturedIndex % texture.getColumn();
		return (float) col / (float) texture.getColumn();
	}
	
	public float getTextureYOffset() {
		int row = texturedIndex % texture.getRow();
		return (float) row / (float) texture.getRow();
	}
	
	public void setTextureIndex(int index) {
		texturedIndex = index;
	}
	
	private void updateMatrix() {
		Matrix4f translate = Matrix4f.translate(position);
		Matrix4f rotation = Matrix4f.rotate(this.rotation.x(), this.rotation.y(), this.rotation.z());
		Matrix4f scale = Matrix4f.zoom(this.scale.x(), this.scale.y(), this.scale.z());
		transformationMatirx = (Matrix4f) translate.multiply(scale).multiply(rotation);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		updateMatrix();
	}
	
	public void move(float dx, float dy, float dz) {
		position.addXYZ(dx, dy, dz);
		updateMatrix();
	}
	
	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
		updateMatrix();
	}
	
	public void rotate(float dx, float dy, float dz) {
		rotation.addXYZ(dx, dy, dz);
		updateMatrix();
	}

	public Vector3f getScale() {
		return scale;
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
		updateMatrix();
	}
	
	public void zoom(float dx, float dy, float dz) {
		scale.addXYZ(dx, dy, dz);
		updateMatrix();
	}
	
	public RawModel getModel() {
		return model;
	}
	
	public Texture getTexture() {
		return texture;
	}

	protected void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Matrix4f getTransformationMatirx() {
		return transformationMatirx;
	}
	
}
