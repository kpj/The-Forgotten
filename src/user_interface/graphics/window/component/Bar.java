package user_interface.graphics.window.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import user_interface.graphics.window.Window_component;

public class Bar implements Window_component{

	Color bar_color;
	int bar_x, bar_y;
	int bar_width, bar_height;
	
	int percent_filled;
	
	boolean horizontal;
	
	String popup_text = "";
	
	int origin_x, origin_y;
	
	String id;
	
	public Bar(int x, int y, int w, int h, boolean hori) {
		bar_color = Color.red;
		
		bar_x = x;
		bar_y = y;
		bar_width = w;
		bar_height = h;
		horizontal = hori;
		
		percent_filled = 100;
	}
	public Bar(int x, int y, int w, int h, boolean hori, int p) {
		bar_color = Color.red;
		
		bar_x = x;
		bar_y = y;
		bar_width = w;
		bar_height = h;
		horizontal = hori;
		
		if(p < 0) {
			percent_filled = 0;
		} else if (p > 100) {
			percent_filled = 100;
		} else {
			percent_filled = p;
		}
	}
	
	@Override
	public void draw_content(Graphics g, int origin_x, int origin_y) {
		g.setColor(this.bar_color);
		
		this.origin_x = origin_x;
		this.origin_y = origin_y;
		
		int x = origin_x + bar_x;
		int y = origin_y + bar_y;
		
		/*if(horizontal) {
			g.fillRect(x, y, (int) (bar_width/100.0*percent_filled), bar_height);
		} else {
			g.fillRect(x, y, bar_width, (int) (bar_height/100.0*percent_filled));
		}*/
		if(horizontal) {
			g.fillRect(x, y, (int) (bar_width/100.0*percent_filled), bar_height);
		} else {
			int tmpy = y + 100 - ((int) (bar_height/100.0*percent_filled));
			g.fillRect(x, tmpy, bar_width, ((int) (bar_height/100.0*percent_filled)));
		}
		
		g.drawRect(x, y, bar_width, bar_height);
		
		check_boundary();
	}

	private void check_boundary() {
		if (percent_filled < 0) {
			percent_filled = 0;
		} else if (percent_filled > 100) {
			percent_filled = 100;
		}
	}
	
	/*
	 * Defines how much the bar is filled.
	 * Input is given in percent.
	 */
	public void set_level(int perc) {
		percent_filled = perc;
	}
	public void change_level(int perc) {
		percent_filled += perc;
	}
	

	public Rectangle get_rect() {
		return new Rectangle(origin_x+bar_x, origin_y+bar_y, bar_width, bar_height);
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
		return bar_x;
	}
	public int getY() {
		return bar_y;
	}
	public int getWidth() {
		return bar_width;
	}
	public int getHeight() {
		return bar_height;
	}
	
	public void set_id(String id) {
		this.id = id;
	}
	public String get_id() {
		return id;
	}
}
