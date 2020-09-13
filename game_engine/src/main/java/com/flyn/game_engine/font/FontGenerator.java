package com.flyn.game_engine.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FontGenerator {
	
	private static HashMap<String, Font> fontTypes = new HashMap<>();
	private static HashMap<String, FontMetrics> metrics = new HashMap<>();
	private static HashMap<FontMapKey, Glyph> fonts = new HashMap<>();
	
	public static void addFontFile(String fontName, String fontFile) {
		try {
			Font f = Font.createFont(Font.TRUETYPE_FONT, new File(fontFile));
			fontTypes.put(fontName, f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static FontMetrics creatFontMetrics(String fontName, int fontType) {
		Font f = fontTypes.get(fontName).deriveFont(fontType, 30);
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setFont(f);
		FontMetrics metric = g.getFontMetrics();
		g.dispose();
		metrics.put(fontName + fontType, metric);
		return metric;
	}
	
	public static Glyph getGlyph(String fontName, int fontType, char character) {
		FontMapKey key = new FontMapKey(fontName, fontType, character);
		if(fonts.containsKey(key)) return fonts.get(key);
		FontMetrics m;
		if(metrics.containsKey(fontName + fontType)) m = metrics.get(fontName + fontType);
		else m = creatFontMetrics(fontName, fontType);
		int w = m.charWidth(character), h = m.getHeight();
		BufferedImage image = null;
		if(w > 0) {
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setPaint(Color.WHITE);
		g.setFont(m.getFont());
		g.drawString(String.valueOf(character), 0, m.getAscent());
		g.dispose();
		}
		Glyph result = new Glyph(image, w, h);
		fonts.put(key, result);
		return result;
	}
	
	public static Glyph[] getGlyphs(String fontName, int fontType, String text) {
		char[] cs = text.toCharArray();
		Glyph[] result = new Glyph[cs.length];
		for(int i = 0; i < cs.length; i++) {
			result[i] = getGlyph(fontName, fontType, cs[i]);
		}
		return result;
	}
	
	private static class FontMapKey {
		
		String fontName;
		int fontType;
		char character;
		
		public FontMapKey(String fontName, int fontType, char character) {
			this.fontName = fontName;
			this.fontType = fontType;
			this.character = character;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(super.equals(obj)) return true;
			if(obj instanceof FontMapKey) {
				FontMapKey k = (FontMapKey) obj;
				return fontName.equals(k.fontName) && fontType == k.fontType && character == k.character;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			int hash = fontName.hashCode();
			hash = 31 * hash + fontType;
			hash = 31 * hash + character;
			return hash;
		}
		
	}

}
