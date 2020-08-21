package com.flyn.game_engine.basic;

import org.lwjgl.glfw.GLFW;

import com.flyn.game_engine.input.MouseMotionInput;
import com.flyn.game_engine.input.MouseMotionInterface;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;

public class MousePicker implements MouseMotionInterface {
	
	private float mouseX = 0, mouseY = 0;
	private long window;
	private Matrix4f projection;
	
	public MousePicker(long window, Matrix4f projectionMatrix) {
		this.window = window;
		projection = projectionMatrix;
		MouseMotionInput.addMouseMotionListener(this);
	}
	
	public Vector3f getCurrentRay(Matrix4f view) {
		float[] normalizedCoordinate = getNormalizedCoordinate();
		float[] clipCoordiate = new float[] {normalizedCoordinate[0], normalizedCoordinate[1], -1, 1};
		float[] eyeCoordiate = toEyeCoordinate(clipCoordiate);
		Vector3f worldRay = toWorldCoordiate(view, eyeCoordiate);
		return worldRay;
	}
	
	private Vector3f toWorldCoordiate(Matrix4f view, float[] eyeCoordiate) {
		Matrix4f invertedView = view.inverse();
		float x = 0, y = 0, z = 0;
		for(int i = 0; i < 4; i++) {
			x += eyeCoordiate[i] * invertedView.elements[0 + i * 4];
			y += eyeCoordiate[i] * invertedView.elements[1 + i * 4];
			z += eyeCoordiate[i] * invertedView.elements[2 + i * 4];
		}
		Vector3f ray = new Vector3f(x, y, z).normalise();
		return ray;
	}
	
	private float[] toEyeCoordinate(float[] clipCoordiate) {
		Matrix4f invertedProjection = projection.inverse();
		float x = 0, y = 0;
		for(int i = 0; i < 4; i++) {
			x += clipCoordiate[i] * invertedProjection.elements[0 + i * 4];
			y += clipCoordiate[i] * invertedProjection.elements[1 + i * 4];
		}
		return new float[] {x, y, -1, 0};
	}
	
	private float[] getNormalizedCoordinate() {
		int[] width = new int[1], height = new int[1];
		GLFW.glfwGetWindowSize(window, width, height);
		float x = 2 * mouseX / (float) width[0] - 1;
		float y = 2 * mouseY / (float) height[0] - 1;
		return new float[] {x, y};
	}

	@Override
	public void mouseMotion(int x, int y) {
		mouseX = x;
		mouseY = y;
	}

}
