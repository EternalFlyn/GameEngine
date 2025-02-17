package com.flyn.game_engine.basic;

import org.lwjgl.glfw.GLFW;

import com.flyn.game_engine.entity.object.Player;
import com.flyn.game_engine.input.MouseInput;
import com.flyn.game_engine.input.MouseMotionInput;
import com.flyn.game_engine.input.MouseMotionInterface;
import com.flyn.game_engine.input.WheelInput;
import com.flyn.game_engine.input.WheelInterface;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.math.Vector4f;

public class Camera implements MouseMotionInterface, WheelInterface {
	
	private int mousePosX = -1, mousePosY = -1;
	private float distanceFromPlayer = 0.5f;
	private Vector3f position = new Vector3f(0, 1, 1), rotation = new Vector3f();
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
		MouseMotionInput.addMouseMotionListener(this);
		WheelInput.addWheelListener(this);
	}
	
	public void move() {
		float angleX = (float) Math.toRadians(rotation.y()), angleY = (float) Math.toRadians(rotation.x());
		float horizontalDistance = distanceFromPlayer * (float) Math.cos(angleY);
		float playerPitch = (float) Math.toRadians(player.getRotation().y());
		float x = horizontalDistance * (float) Math.sin(angleX + playerPitch);
		float y = distanceFromPlayer * (float) Math.sin(angleY);
		float z = horizontalDistance * (float) Math.cos(angleX + playerPitch);
		position.setXYZ(player.getPosition().x() - x, player.getPosition().y() + y + 1.5f, player.getPosition().z() - z);
	}

	public Matrix4f createViewMatrix() {
		Matrix4f translate = Matrix4f.translate(new Vector3f(-position.x(), -position.y(), -position.z()));
		Matrix4f roll = Matrix4f.roll(rotation.x());
		Matrix4f pitch = Matrix4f.pitch(180 - (player.getRotation().y() + rotation.y()));
		return (Matrix4f) roll.multiply(pitch).multiply(translate);
	}
	
	public Matrix4f createReflectViewMatrix(Vector4f plane) {
		Matrix4f translate = Matrix4f.translate(new Vector3f(-position.x(), -position.y(), -position.z()));
		Matrix4f roll = Matrix4f.roll(rotation.x());
		Matrix4f pitch = Matrix4f.pitch(180 - (player.getRotation().y() + rotation.y()));
		return (Matrix4f) roll.multiply(pitch).multiply(translate).multiply(Matrix4f.reflect(plane));
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	@Override
	public void wheelScroll(int x, int y) {
		distanceFromPlayer -= y * 0.1f;
		if(distanceFromPlayer < 0.5f) distanceFromPlayer = 0.5f;
		else if(distanceFromPlayer > 3) distanceFromPlayer = 3;
	}

	@Override
	public void mouseMotion(int x, int y) {
		if(MouseInput.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_3)) {
			if(mousePosX == -1) {
				mousePosX = x;
				mousePosY = y;
			}
			else {
				rotation.add("y", (x - mousePosX) * -0.1f);
				mousePosX = x;
				if(rotation.y() > 180) rotation.add("y", -360);
				else if(rotation.y() < -180) rotation.add("y", 360);
				rotation.add("x", (y - mousePosY) * -0.3f);
				mousePosY = y;
				if(rotation.x() > 90) rotation.set("x", 90);
				else if(rotation.x() < -90) rotation.set("x", -90);
			}
		}
		else mousePosX = -1;
	}

}
