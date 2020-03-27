package com.flyn.game_engine;

import com.flyn.game_engine.window.Window;

public class App {
	
	private static Window window = new Window();
	
    public static void main( String[] args ) {
    	new Thread(() -> {
    		window.setWindow("Test", 800, 600);
    		window.showWindow();
    	}).start();
    }
    
}
