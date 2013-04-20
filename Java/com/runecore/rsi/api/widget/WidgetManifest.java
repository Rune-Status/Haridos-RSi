package com.runecore.rsi.api.widget;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WidgetManifest {
	
	public boolean rsiRequired() default false;
	public String name();
	public String author();
	public String description();
	public double version();

}
