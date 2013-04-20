package com.runecore.rsi.env.widget;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.runecore.rsi.api.widget.CanvasWidget;
import com.runecore.rsi.api.widget.Widget;
import com.runecore.rsi.api.widget.WidgetManifest;
import com.runecore.rsi.env.db.DatabaseConnection;
import com.runecore.rsi.env.db.DatabaseManager;
import com.runecore.rsi.env.db.Synchronizable;

public class WidgetRepository implements Synchronizable {
	
	private static final WidgetRepository INSTANCE = new WidgetRepository();
	private final List<WidgetInformation> registeredWidgets;
	private final List<Widget> activeApplicationWidgets;
	private final List<CanvasWidget> activeCanvasWidgets;
	
	private WidgetRepository() {
		registeredWidgets = new LinkedList<WidgetInformation>();
		activeApplicationWidgets = new LinkedList<Widget>();
		activeCanvasWidgets = new LinkedList<CanvasWidget>();
	}
	
	public void init() {
		File widgetDB = new File(System.getProperty("user.home")+"/.runeloader/db/widgets.db");
		DatabaseConnection db = DatabaseConnection.create(widgetDB.getAbsolutePath());
		try {
			DatabaseManager.get().add(db, "widgets");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!widgetDB.exists()) {
			PreparedStatement ps = db.getStatement("create table info (name string, archive string, main string, enabled boolean)");
			try {
				ps.executeUpdate();
				ps.close();
				db.killConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				PreparedStatement ps = db.getStatement("select * from `info`");
				ResultSet rs = ps.executeQuery();
				List<WidgetInformation> info = new ArrayList<WidgetInformation>();
				while(rs.next()) {
					WidgetInformation wi = new WidgetInformation(new File(rs.getString("archive")), rs.getString("main"), rs.getBoolean("enabled"));
					info.add(wi);
				}
				rs.close();
				for(WidgetInformation wi : info) {
					register(wi, false);
				}
				System.out.println("[WidgetRepository]: Loaded "+info.size()+" widgets from database!");
			} catch(Exception e) {
				e.printStackTrace();
				//throw new Exception("Unable to load widgets!");
			}
		}
	}
	
	public boolean register(WidgetInformation wi, boolean sync) throws Exception {
		if(registeredWidgets.contains(wi)) {
			return false;
		}
		Widget widget = (Widget) Class.forName(wi.getMainClass()).newInstance();
		getRegisteredWidgets().add(wi);
		if(wi.isEnabled()) {
			if(widget instanceof CanvasWidget) {
				getActiveCanvasWidgets().add((CanvasWidget) widget);
			} else {
				getActiveApplicationWidgets().add(widget);
			}
			if(widget instanceof KeyEventDispatcher) {
				KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEventDispatcher) widget);
			}
			widget.register();
			widget.checkForUpdates();
		}
		if(sync)
			synchronize();
		return true;
	}
	
	public synchronized void toggle(Widget w) {
		if(w == null)
			return;
		w.deregister();
		WidgetInformation wi = getWidgetInfo(w.getClass().getAnnotation(WidgetManifest.class).name());
		wi.setEnabled(!wi.isEnabled());
		if(!wi.isEnabled()) {
			if(w instanceof CanvasWidget) {
				getActiveCanvasWidgets().remove(w);
			} else {
				getActiveApplicationWidgets().remove(w);
			}
			if(w instanceof KeyEventDispatcher) {
				KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher((KeyEventDispatcher) w);
			}
		}
		synchronize();
	}
	
	public synchronized WidgetInformation getWidgetInfo(String name) {
		for(WidgetInformation wi : getRegisteredWidgets()) {
			if(wi.getManifest().name().equalsIgnoreCase(name)) {
				return wi;
			}
		}
		return null;
	}

	public synchronized Widget getWidget(String name) {
		for(Widget w : getActiveApplicationWidgets()) {
			if(w.getClass().getAnnotation(WidgetManifest.class).name().equalsIgnoreCase(name)) {
				return w;
			}
		}
		for(Widget w : getActiveCanvasWidgets()) {
			if(w.getClass().getAnnotation(WidgetManifest.class).name().equalsIgnoreCase(name)) {
				return w;
			}
		}
		return null;
	}
	
	public static WidgetRepository get() {
		return INSTANCE;
	}
	
	public synchronized List<WidgetInformation> getRegisteredWidgets() {
		return registeredWidgets;
	}

	public synchronized List<Widget> getActiveApplicationWidgets() {
		return activeApplicationWidgets;
	}

	public synchronized List<CanvasWidget> getActiveCanvasWidgets() {
		return activeCanvasWidgets;
	}

	@Override
	public void synchronize() {
		try {
			DatabaseConnection db = DatabaseManager.get().get("widgets");
			PreparedStatement ps = db.getStatement("drop table if exists info");
			ps.executeUpdate();
			ps = db.getStatement("create table info (name string, archive string, main string, enabled boolean)");
			ps.executeUpdate();
			ps = db.getStatement("insert into info values(?, ?, ?, ?)");
			for(WidgetInformation wi : getRegisteredWidgets()) {
				ps.setString(1, wi.getManifest().name());
				ps.setString(2, wi.getFile() == null? "" : wi.getFile().getAbsolutePath());
				ps.setString(3, wi.getMainClass());
				ps.setBoolean(4, wi.isEnabled());
				ps.execute();
			}
			ps.close();
			db.killConnection();
			System.out.println("[WidgetRepository]: Updated database sucessfully with "+getRegisteredWidgets().size()+"!");
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("[WidgetRepository]: Failed to update database!");
		}
	}

}