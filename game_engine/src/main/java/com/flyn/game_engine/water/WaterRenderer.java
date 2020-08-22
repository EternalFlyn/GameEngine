package com.flyn.game_engine.water;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.utils.Loader;

public class WaterRenderer {
	
	private static float[] vertices = {-1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
	
	private RawModel quad;
	private WaterShader shader;
	
	public WaterRenderer(WaterShader shader) {
		this.shader = shader;
		quad = Loader.loadToVAO(vertices, 2);
	}
	
	public void render(ArrayList<WaterTile> water) {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		for(WaterTile tile : water) {
			shader.setTransformation(tile.getTransformationMatirx());
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
