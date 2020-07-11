package com.flyn.game_engine.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.shader.ShaderProgram;

public class Renderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	
	private final float aspectRatio;
	
	private float hue = 0;
	private Matrix4f projectionMatrix;
	
	public Renderer(ShaderProgram shader, int width, int height) {
		aspectRatio = (float) width / (float) height;
		createProjectionMatrix();
		shader.enable();
		shader.setUniform4f("projection", projectionMatrix);
		shader.disable();
	}
	
	public void prepare() {
		hue += 0.01f;
		if(hue > 1) hue = 0;
		Color backgroundColor = Color.getHSBColor(hue, 1, 1);
		GL11.glClearColor(backgroundColor.getRed() / 255f, backgroundColor.getGreen() / 255f, backgroundColor.getBlue() / 255f, backgroundColor.getAlpha() / 255f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(RawModel model) {
		GL30.glBindVertexArray(model.getVaoID());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.getIndicesID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	public void render(TexturedModel texturedmodel) {
		RawModel model = texturedmodel.getModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.getIndicesID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedmodel.getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	public void render(Entity entity, ShaderProgram shader) {
		TexturedModel texturedmodel = entity.getModel();
		RawModel model = texturedmodel.getModel();
		Matrix4f transformation = entity.getTransformationMatirx();
		GL30.glBindVertexArray(model.getVaoID());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.getIndicesID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.setUniform4f("transformation", transformation);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedmodel.getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	private void createProjectionMatrix() {
		float yScale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))), xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE; //zm
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.elements[0 + 0 * 4] = xScale;
		projectionMatrix.elements[1 + 1 * 4] = yScale;
		projectionMatrix.elements[2 + 2 * 4] = -(FAR_PLANE + NEAR_PLANE) / frustumLength;
		projectionMatrix.elements[2 + 3 * 4] = -(2 * FAR_PLANE * NEAR_PLANE) / frustumLength;
		projectionMatrix.elements[3 + 2 * 4] = -1;
	}

}
