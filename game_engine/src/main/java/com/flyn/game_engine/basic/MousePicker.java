package com.flyn.game_engine.basic;

import com.flyn.game_engine.input.MouseMotionInput;
import com.flyn.game_engine.input.MouseMotionInterface;
import com.flyn.game_engine.input.WindowResizeInput;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector2f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.math.Vector4f;

public class MousePicker implements MouseMotionInterface {
	
	private long window;
	
	private Vector2f mouse = new Vector2f();
	private Matrix4f projection;
	
	public MousePicker(long window, Matrix4f projectionMatrix) {
		this.window = window;
		projection = projectionMatrix;
		MouseMotionInput.addMouseMotionListener(this);
	}
	
	public Vector3f getCurrentRay(Matrix4f view) {
		Vector2f normalizedCoordinate = getNormalizedCoordinate();
		Vector4f clipCoordiate = new Vector4f(normalizedCoordinate.x(), normalizedCoordinate.y(), -1, 1);
		Vector4f eyeCoordiate = toEyeCoordinate(clipCoordiate);
		Vector3f worldRay = toWorldCoordiate(view, eyeCoordiate);
		return worldRay;
	}
	
	private Vector3f toWorldCoordiate(Matrix4f view, Vector4f eyeCoordiate) {
		Matrix4f invertedView = view.inverse();
		Vector4f result = (Vector4f) invertedView.multiply(eyeCoordiate);
		Vector3f ray = (Vector3f) new Vector3f(result.x(), result.y(), result.z()).normalise();
		return ray;
	}
	
	private Vector4f toEyeCoordinate(Vector4f clipCoordiate) {
		Matrix4f invertedProjection = projection.inverse();
		Vector4f result = (Vector4f) invertedProjection.multiply(clipCoordiate);
		result.elements[2][0] = -1;
		result.elements[3][0] = 0;
		return result;
	}
	
	private Vector2f getNormalizedCoordinate() {
		int[] size = WindowResizeInput.getSize(window);
		float x = 2 * mouse.x() / (float) size[0] - 1;
		float y = 2 * mouse.y() / (float) size[1] - 1;
		return new Vector2f(x, y);
	}

	@Override
	public void mouseMotion(int x, int y) {
		mouse.setXY(x, y);
	}

}
