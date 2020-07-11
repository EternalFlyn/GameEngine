package com.flyn.game_engine.entity;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.TexturedModel;

public class Entity {
	
	private TexturedModel model;
	private Vector3f position, rotation, scale;
	private Matrix4f transformationMatirx;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		updateMatrix();
	}
	
	private void updateMatrix() {
		Matrix4f translate = Matrix4f.translate(position);
		Matrix4f rotation = Matrix4f.rotate(this.rotation.x, this.rotation.y, this.rotation.z);
		Matrix4f scale = Matrix4f.zoom(this.scale.x, this.scale.y, this.scale.z);
		transformationMatirx = translate.multiply(rotation).multiply(scale);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		updateMatrix();
	}
	
	public void move(float dx, float dy, float dz) {
		position.x += dx;
		position.y += dy;
		position.z += dz;
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
		rotation.x += dx;
		rotation.y += dy;
		rotation.z += dz;
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
		scale.x += dx;
		scale.y += dy;
		scale.z += dz;
		updateMatrix();
	}
	
	public TexturedModel getModel() {
		return model;
	}

	public Matrix4f getTransformationMatirx() {
		return transformationMatirx;
	}
	
}
