package com.runecore.rsi.api.widget;

public interface Widget {
	
	public void checkForUpdates();
	public void register();
	public void deregister();
	public void onClose();

}
