package com.flyn.game_engine.terrain;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.math.Matrix4f;
public class TerrainRenderer {
	
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projection) {
		this.shader = shader;
		shader.enable();
		shader.connectTextureUnit();
		shader.setProjection(projection);
		shader.disable();
	}

	public void render(ArrayList<Terrain> terrains) {
		for(Terrain terrain : terrains) {
			prepareTerrainModel(terrain);
			shader.setTransformation(terrain.getTransformationMatirx());
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrainModel();
		}
	}
	
	public void setProjectionMatrix(Matrix4f projection) {
		shader.enable();
		shader.setProjection(projection);
		shader.disable();
	}
	
	private void prepareTerrainModel(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, rawModel.getIndicesID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.setShineVariables(1, 0);
		shader.setGrassColor(terrain.getGrassColor());
	}
	
	private void bindTextures(Terrain terrain) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBackgroundTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getRTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getGTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getID());
	}
	
	private void unbindTerrainModel() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
}
