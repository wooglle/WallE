package com.woog.walle.ai;

public class CutOneTree extends ActionBase {
	@Override
	public String getToolsKeyword() {
		return "";
	}
	
	@Override
	public String getActName() {
		return "Cut One Tree";
	}
	
	@Override
	public boolean condition() {
		return (mc.world != null) && doing && (!pause);
	}
	
	@Override
	public void action() {
		
	}
}
