package com.flyn.game_engine.font;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.basic.MasterRenderer;
import com.flyn.game_engine.basic.RawModel;
import com.flyn.game_engine.input.WindowResizeInput;
import com.flyn.game_engine.math.Matrix4f;
import com.flyn.game_engine.math.Vector3f;
import com.flyn.game_engine.utils.Loader;

public class TextRenderer {
	
	private final long window;
	private final RawModel quad;
	
	private TextShader shader = new TextShader();
	
	public TextRenderer(long window) {
		this.window = window;
		float[] vertices = {
				0, -2,
				0, 0,
				2, -2,
				2, 0
		};
		quad = Loader.loadToVAO(vertices, 2);
	}
	
	public void render(ArrayList<Text> texts) {
		shader.enable();
		MasterRenderer.disableCulling();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		int[] size = WindowResizeInput.getSize(window);
		for(Text text : texts) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			Matrix4f mainTransformation = text.getTransformationMatrix();
			shader.setTextColor(text.getTextColor());
			float offsetY = 0;
			float lineHeight = 2 * text.getTextHeight() / (float) size[1];
			float displayLength = 2 * text.getMaxTextLength() / text.getScale();
			for(Glyph[] glyphs : text.getText()) {
				float offsetX = 0;
				if(text.alignment > 0) {
					float textLength = 0;
					for(Glyph g : glyphs) {
						float textWidth = 2 * g.getWidth() / (float) size[0];
						if(textLength + textWidth > displayLength) break;
						textLength += textWidth;
					}
					if(text.alignment == 1) offsetX = (displayLength - textLength) / 2;
					else if(text.alignment == 2) offsetX = displayLength - textLength - 0.001f;
				}
				for(int i = 0; i < glyphs.length; i++) {
					float textWidth = 2 * glyphs[i].getWidth() / (float) size[0];
					if(offsetX + textWidth >  displayLength) {
						offsetX = 0;
						offsetY -= lineHeight;
						if(text.alignment > 0) {
							float textLength = 0;
							for(int j = i; j < glyphs.length; j++) {
								float textWidth2 = 2 * glyphs[j].getWidth() / (float) size[0];
								if(textLength + textWidth2 > displayLength) break;
								textLength += textWidth2;
							}
							if(text.alignment == 1) offsetX = (displayLength - textLength) / 2;
							else if(text.alignment == 2) offsetX = displayLength - textLength - 0.001f;
						}
					}
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyphs[i].getTextureID());
					Matrix4f glyphTranslate = Matrix4f.translate(new Vector3f(offsetX, offsetY, 0));
					Matrix4f glyphScale = Matrix4f.zoom(glyphs[i].getWidth() / (float) size[0], text.getTextHeight() / (float) size[1], 0);
					shader.setTransformation((Matrix4f) mainTransformation.multiply(glyphTranslate).multiply(glyphScale));
					GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
					offsetX += textWidth;
				}
				offsetY -= lineHeight;
			}
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
