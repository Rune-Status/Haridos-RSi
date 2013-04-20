package com.runecore.rsi.internal.node;

public interface Widget {
	
	public Widget getParent();
	public Widget[] getChildren();
	public String[] getActions();

}
