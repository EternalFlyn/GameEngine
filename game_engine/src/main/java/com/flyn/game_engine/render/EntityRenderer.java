package com.flyn.game_engine.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.entity.Entity;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.shader.TexturedShader;

public class EntityRenderer {
	
	private TexturedShader shader;
	
	public EntityRenderer(TexturedShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.enable();
		shader.setProjection(projectionMatrix);
		shader.disable();
	}
	
	public void render(HashMap<TexturedModel, ArrayList<Entity>> entities) {
		Iterator<Entry<TexturedModel, ArrayList<Entity>>> iter = entities.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<TexturedModel, ArrayList<Entity>> entry = iter.next();
			TexturedModel model = entry.getKey();
			prepareTexturedModel(model);
			for(Entity entity : entry.getValue()) {
				shader.setTransformation(entity.getTransformationMatirx());
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, rawModel.getIndicesID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		Texture texture = model.getTexture();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
		shader.setShineVariables(texture.getShineDamper(), texture.getReflectivity());
	}
	
	private void unbindTexturedModel() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

}
