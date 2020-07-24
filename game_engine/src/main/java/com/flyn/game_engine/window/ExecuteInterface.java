package com.flyn.game_engine.window;

public interface ExecuteInterface {
	
	public static boolean REPEAT_EXECUTE = false, SINGLE_EXECUTE = true;
	
	public boolean execute(long time);

}
