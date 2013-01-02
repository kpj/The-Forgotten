package user_interface.graphics.window.component;

import java.awt.Graphics;
import java.awt.Rectangle;

import user_interface.graphics.window.Window_component;

public class Label implements Window_component {
	String label;
	

	int component_x, component_y;
	
	String popup_text = "";
	
	int origin_x, origin_y;
	
	public Label(String c, int x, int y) {
		label = c;
		
		component_x = x;
		component_y = y;
	}
	
	@Override
	public void draw_content(Graphics g, int origin_x, int origin_y) {
		this.origin_x = origin_x;
		this.origin_y = origin_y;
		
		g.drawString(label, origin_x + component_x, origin_y + component_y);
	}
	
	public Rectangle get_rect() {
		// TODO: calculate width/height of text
		return new Rectangle(origin_x+component_x, origin_y+component_y, 20, 20);
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
}