package com.runecore.rsi.internal;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.List;

import com.runecore.rsi.api.widget.CanvasWidget;
import com.runecore.rsi.env.Context;
import com.runecore.rsi.env.widget.WidgetRepository;

public class ClientCanvas extends Canvas {
	
private static final BufferedImage gameImage = new BufferedImage(765, 503, 2);
	
	@Override
	public Graphics getGraphics() {
		Graphics2D g = (Graphics2D) gameImage.getGraphics();
		Font current = g.getFont();
		g.setColor(Color.YELLOW);
		g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	Context context = Context.resolve();
    	List<CanvasWidget> widgets = WidgetRepository.get().getActiveCanvasWidgets();
    	for(CanvasWidget widget : widgets) {
    		try {
    			widget.draw(g);
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	/*
    	g.drawString("FPS: "+context.getClient().getFPS(), 460, 20);
    	g.drawString("State: "+context.getClient().getClientState(), 460, 35);
    	*/
    	super.getGraphics().drawImage(gameImage, 0, 0, null);
    	return g;
	}

}