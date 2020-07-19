package com.flyn.game_engine.entity;

import java.awt.event.KeyEvent;

import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.render.TexturedModel;
import com.flyn.game_engine.window.Window;

public class Player extends Entity {
	
	private static final float WALK_SPEED = 20, TURN_SPEED = 160, JUMP_POWER = 20, GRAVITY = -50;
	
	private float currentSpeed = 0, currentTurnSpeed = 0, ySpeed = 0;
	private boolean isJump = false;

	public Player(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		super(model, position, rotation, scale);
	}
	
	public void move() {
		checkInputs();
		float frameTime = Window.getFrameTimeSeconds();
		rotate(0, currentTurnSpeed * frameTime, 0);
		float distance = currentSpeed * frameTime, pitch = (float) Math.toRadians(getRotation().y);
		ySpeed += GRAVITY * frameTime;
		move(distance * (float) Math.sin(pitch), ySpeed * frameTime, distance * (float) Math.cos(pitch));
		if(getPosition().y < 0){
			ySpeed = 0;
			isJump = false;
			setPosition(new Vector3f(getPosition().x, 0, getPosition().z));
		}
	}
	
	private void checkInputs() {
		if(Window.input.isKeyPressed(KeyEvent.VK_W)) currentSpeed = WALK_SPEED;
		else if(Window.input.isKeyPressed(KeyEvent.VK_S)) currentSpeed = -WALK_SPEED;
		else currentSpeed = 0;
		
		if(Window.input.isKeyPressed(KeyEvent.VK_A)) currentTurnSpeed = TURN_SPEED;
		else if(Window.input.isKeyPressed(KeyEvent.VK_D)) currentTurnSpeed = -TURN_SPEED;
		else currentTurnSpeed = 0;
		
		if(Window.input.isKeyPressed(KeyEvent.VK_SPACE) && !isJump) {
			ySpeed = JUMP_POWER;
			isJump = true;
		}
	}

}
