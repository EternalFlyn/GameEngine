package com.flyn.game_engine.font;

import java.awt.image.BufferedImage;

public class Glyph {
	
	private final BufferedImage glyphImage;
	
	public Glyph(BufferedImage image) {
		glyphImage = image;
	}

	public BufferedImage getGlyphImage() {
		return glyphImage;
	}
	
}
