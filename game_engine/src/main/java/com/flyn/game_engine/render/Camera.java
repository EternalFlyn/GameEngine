package com.flyn.game_engine.render;

import java.awt.event.KeyEvent;

import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.window.Window;

public class Camera {
	
	private Vector3f position = new Vector3f(), rotation = new Vector3f();
	
	public void move() {
		if(Window.input.isKeyPressed(KeyEvent.VK_W)) position.z -= 0.02f;
		if(Window.input.isKeyPressed(KeyEvent.VK_S)) position.z += 0.02f;
		if(Window.input.isKeyPressed(KeyEvent.VK_A)) position.x -= 0.02f;
		if(Window.input.isKeyPressed(KeyEvent.VK_D)) position.x += 0.02f;
		if(Window.input.isKeyPressed(265)) position.y += 0.02f;
		if(Window.input.isKeyPressed(264)) position.y -= 0.02f;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getRoll() {
		return rotation.x;
	}
	
	public float getPitch() {
		return rotation.y;
	}
	
	public float getYaw() {
		return rotation.z;
	}
	
	public Matrix4f createViewMatrix() {
		Matrix4f translate = Matrix4f.translate(new Vector3f(-position.x, -position.y, -position.z));
		Matrix4f rotate = Matrix4f.rotate(rotation.x, rotation.y, rotation.z);
		return translate.multiply(rotate);
	}

}
