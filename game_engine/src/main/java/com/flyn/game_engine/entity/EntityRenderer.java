package com.flyn.game_engine.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.basic.MasterRenderer;
import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.basic.Texture;
import com.flyn.game_engine.math.Matrix4f;

public class EntityRenderer {
	
	private EntityShader shader;
	
	public EntityRenderer(EntityShader shader) {
		this.shader = shader;
	}
	
	public void render(HashMap<RawModel, HashMap<Texture, ArrayList<Entity>>> entities) {
		Iterator<Entry<RawModel, HashMap<Texture, ArrayList<Entity>>>> modelIter = entities.entrySet().iterator();
		while(modelIter.hasNext()) {
			Entry<RawModel, HashMap<Texture, ArrayList<Entity>>> modelEntry = modelIter.next();
			RawModel model = modelEntry.getKey();
			prepareModel(model);
			Iterator<Entry<Texture, ArrayList<Entity>>> textureIter = modelEntry.getValue().entrySet().iterator();
			while(textureIter.hasNext()) {
				Entry<Texture, ArrayList<Entity>> textureEntry = textureIter.next();
				prepareTexture(textureEntry.getKey());
				for(Entity entity : textureEntry.getValue()) {
					shader.setTransformation(entity.getTransformationMatirx());
					shader.setTextureOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
					GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
			}
			unbind();
		}
	}
	
	private void prepareModel(RawModel model) {
		GL30.glBindVertexArray(model.getVaoID());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.getIndicesID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
	}
	
	private void prepareTexture(Texture texture) {
		shader.setTextureAmount(texture.getColumn(), texture.getRow());
		if(texture.hasTransparency()) MasterRenderer.disableCulling();
		shader.setUsedFakeLight(texture.isUsedFakeLight());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
		shader.setShineVariables(texture.getShineDamper(), texture.getReflectivity());
	}
	
	private void unbind() {
		MasterRenderer.disableCulling();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

}
