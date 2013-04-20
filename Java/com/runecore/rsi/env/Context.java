package com.runecore.rsi.env;

import java.applet.Applet;

import com.runecore.rsi.internal.Client;

public class Context {
	
	private static Context context;
	private Client client;
	
	public Context(Client client) {
		this.client = client;
		Context.context = this;
	}
	
	public static Context resolve() {
		synchronized(context) {
			return context;
		}
	}
	
	public Client getClient() {
		return client;
	}
	
	public Applet getApplet() {
		return (Applet)client;
	}
	
	public static Context get() {
		return context;
	}
	
}