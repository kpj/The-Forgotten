package user_interface.graphics.window.component;

import java.awt.Graphics;
import java.awt.Rectangle;

import user_interface.graphics.window.Window_component;

public class Label implements Window_component {
	String label;
	

	int component_x, component_y;
	int component_width, component_height;
	
	String popup_text = "";
	
	int origin_x, origin_y;
	
	String id;
	
	// Needs width/height as input, because fonts are scary
	public Label(String c, int x, int y, int w, int h) {
		label = c;
		
		component_x = x;
		component_y = y;
		component_width = w;
		component_height = h;
	}
	
	@Override
	public void draw_content(Graphics g, int origin_x, int origin_y) {
		this.origin_x = origin_x;
		this.origin_y = origin_y;
		
		g.drawString(label, origin_x + component_x, origin_y + component_y);
	}
	
	public Rectangle get_rect() {
		// TODO strange positioning for tooltip
		return new Rectangle(
				origin_x+component_x, 
				origin_y+component_y-component_height+3, 
				component_width, 
				component_height
			);
	}
	
	public void update_label(String s) {
		this.label = s;
	}
	
	public void set_popup_text(String f) {
		popup_text = f;
	}
	public String get_popup_text() {
		return this.popup_text;
	}
	public boolean popup_set() {
		return !(popup_text.equals(""));
	}
	
	public int getX() {
		return component_x;
	}
	public int getY() {
		return component_y;
	}
	public int getWidth() {
		return component_width;
	}
	public int getHeight() {
		return component_height;
	}
	
	public void set_id(String id) {
		this.id = id;
	}
	public String get_id() {
		return id;
	}
}