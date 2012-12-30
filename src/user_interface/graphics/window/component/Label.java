package user_interface.graphics.window.component;

import java.awt.Graphics;

import user_interface.graphics.window.Window_component;

public class Label implements Window_component {
	String label;
	

	int component_x, component_y;
	
	public Label(String c, int x, int y) {
		label = c;
		
		component_x = x;
		component_y = y;
	}
	
	public void draw_content(Graphics g, int origin_x, int origin_y) {
		g.drawString(label, origin_x + component_x, origin_y + component_y);
	}
}