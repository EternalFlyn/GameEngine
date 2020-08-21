package com.flyn.game_engine.entity.object;

import org.lwjgl.glfw.GLFW;

import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.basic.Texture;
import com.flyn.game_engine.basic.Window;
import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.input.KeyInput;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.terrain.Terrain;

public class Player extends Entity {
	
	private static final float WALK_SPEED = 5, TURN_SPEED = 180, JUMP_POWER = 3, GRAVITY = -10;
	
	private float currentSpeed = 0, currentTurnSpeed = 0, ySpeed = 0;
	private boolean isJump = false;

	public Player(RawModel model, Texture texture, Vector3f position, Vector3f rotation, Vector3f scale) {
		super(model, texture, position, rotation, scale);
	}
	
	public void move(Terrain terrain) {
		checkInputs();
		float frameTime = Window.getFrameTimeSeconds();
		rotate(0, currentTurnSpeed * frameTime, 0);
		float distance = currentSpeed * frameTime, pitch = (float) Math.toRadians(getRotation().y);
		ySpeed += GRAVITY * frameTime;
		move(distance * (float) Math.sin(pitch), ySpeed * frameTime, distance * (float) Math.cos(pitch));
		float terrainHeight = terrain.getHeight(getPosition().x, getPosition().z);
		if(getPosition().y < terrainHeight){
			ySpeed = 0;
			isJump = false;
			setPosition(new Vector3f(getPosition().x, terrainHeight, getPosition().z));
		}
	}
	
	private void checkInputs() {
		if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_W)) currentSpeed = WALK_SPEED;
		else if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_S)) currentSpeed = -WALK_SPEED;
		else currentSpeed = 0;
		
		if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_A)) currentTurnSpeed = TURN_SPEED;
		else if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_D)) currentTurnSpeed = -TURN_SPEED;
		else currentTurnSpeed = 0;
		
		if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_SPACE) && !isJump) {
			ySpeed = JUMP_POWER;
			isJump = true;
		}
	}

}
