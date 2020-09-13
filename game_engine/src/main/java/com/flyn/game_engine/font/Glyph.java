package com.flyn.game_engine.font;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import com.flyn.game_engine.utils.BufferUtils;

public class Glyph {
	
	private final int width, height;
	private final BufferedImage glyphImage;
	
	private int textureID = 0;
	
	public Glyph(BufferedImage image, int width, int height) {
		glyphImage = image;
		this.width = width;
		this.height = height;
	}
	
	public int getTextureID() {
		if(textureID != 0) return textureID;
		int[] pixel = new int[width * height];;
		glyphImage.getRGB(0, 0, width, height, pixel, 0, width);
		for(int i = 0; i < width * height; i++) {
			int a = (pixel[i] & 0xFF000000) >> 24;
			int r = (pixel[i] & 0xFF0000) >> 16;
			int g = (pixel[i] & 0xFF00) >> 8;
			int b = (pixel[i] & 0xFF);

			pixel[i] = a << 24 | b << 16 | g << 8 | r;
		}
		textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(pixel));
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return textureID;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public BufferedImage getGlyphImage() {
		return glyphImage;
	}
	
}
