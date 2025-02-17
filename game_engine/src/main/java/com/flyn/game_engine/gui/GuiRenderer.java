package com.flyn.game_engine.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.basic.MasterRenderer;
import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.utils.Loader;

public class GuiRenderer {
	
	private final RawModel quad;
	
	private GuiShader shader = new GuiShader();
	
	public GuiRenderer() {
		float[] vertices = {
				0, -2,
				0, 0,
				2, -2,
				2, 0
		};
		quad = Loader.loadToVAO(vertices, 2);
	}
	
	public void render(ArrayList<GuiTexture> guis) {
		shader.enable();
		MasterRenderer.disableCulling();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(GuiTexture gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTextureID());
			shader.setTransformation(gui.getTransformationMatrix());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		MasterRenderer.enableCulling();
		shader.disable();
	}
	
	public void remove() {
		shader.remove();
	}

}
