package com.runecore.rsi.env.widget;

import java.io.File;

import com.runecore.rsi.api.widget.WidgetManifest;
import com.runecore.rsi.env.DynamicLoader;

public class WidgetInformation {
	
	private final File file;
	private final String mainClass;
	private boolean enabled;
	private WidgetManifest manifest;
	
	/**
	 * WidgetInformation instance
	 * @param file The jar containing the widget
	 * @param mainClass The main class to load
	 * @param enabled Is the widget enabled?
	 */
	public WidgetInformation(File file, String mainClass, boolean enabled) {
		this.file = file;
		this.mainClass = mainClass;
		this.setEnabled(enabled);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init() throws Exception {
		Class widgetClass = Class.forName(getMainClass());
		if(widgetClass.isAnnotationPresent(WidgetManifest.class)) {
			WidgetManifest manifest = (WidgetManifest) widgetClass.getAnnotation(WidgetManifest.class);
			this.manifest = manifest;
		}
	}

	public File getFile() {
		return file;
	}

	public String getMainClass() {
		return mainClass;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public WidgetManifest getManifest() {
		return manifest;
	}

	public void setManifest(WidgetManifest manifest) {
		this.manifest = manifest;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}