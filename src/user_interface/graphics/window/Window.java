package user_interface.graphics.window;

import game.handler.content_handler;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Date;


public class Window {
	
	content_handler content;
	
	String caption;
	
	Color window_color, window_border_color;
	int window_x, window_y, window_width, window_height;
	
	
	Color status_bar_color_in_focus = Color.blue;
	Color status_bar_color_de_focus = new Color(0,10,102);
	int status_bar_x = 0;
	int status_bar_y = 0;
	int status_bar_width;
	int status_bar_height = 20;
	boolean has_status_bar = true; // Includes ability to move/close window
	
	Color close_button_color = Color.gray;
	int close_button_x;
	int close_button_y = 0;
	int close_button_width = 20;
	int close_button_height = 20;
	
	
	int move_x, move_y;
	int move_x_tmp, move_y_tmp, drag_start_x, drag_start_y, old_move_x, old_move_y;
	
	boolean dispose_me = false;
	boolean in_focus = true;
	
	int time_to_live = -42;
	int time_start = -42;
	
	ArrayList<Window_component> elements = new ArrayList<>();
	
	public Window(content_handler con, String cap, int x, int y, int w, int h) {
		content = con;
		
		caption = cap;
		
		window_color = Color.orange;
		window_x = x;
		window_y = y;
		window_width = w;
		window_height = h;
		window_border_color = Color.black;
		
		status_bar_width = window_width;
		close_button_x = status_bar_width - close_button_width;
	}
	
	public void draw_me(Graphics g) {
		int window_xpos = window_x + move_x;
		int window_ypos = window_y + move_y;
		
		// draw window
		g.setColor(window_color);
		g.fillRect(
				window_xpos, 
				window_ypos, 
				window_width, 
				window_height
			);
		
		// draw window borders
		g.setColor(window_border_color);
		((Graphics2D)g).setStroke(new BasicStroke(4));
		g.drawRect(
				window_xpos, 
				window_ypos, 
				window_width, 
				window_height
			);
		
		if(this.has_status_bar) {
			// draw status bar
			if(this.in_focus)
				g.setColor(status_bar_color_in_focus);
			else
				g.setColor(status_bar_color_de_focus);
			g.fillRect(
					window_xpos+status_bar_x, 
					window_ypos+status_bar_y, 
					status_bar_width, 
					status_bar_height
				);
			
			// draw close button
			g.setColor(close_button_color);
			g.fillRect(
					window_xpos+close_button_x, 
					window_ypos+close_button_y, 
					close_button_width, 
					close_button_height
				);
			
			// draw title
			g.setColor(Color.black);
			g.drawString(caption, window_xpos, window_ypos + 15);
		}
		
		// draw elements
		for(Window_component wc : elements) {
			wc.draw_content(g, window_xpos, window_ypos + status_bar_height*2);
			
			if(this.in_focus) {
				// check for "tooltip" help
				// TODO Draw tooltips on top of all other elements
				if(wc.get_rect().contains(content.mouse_x, content.mouse_y)) {
					if(wc.popup_set()) {
						String txt_2draw = wc.get_popup_text();
						int[] i = content.get_string_information(txt_2draw);
						
						g.setColor(Color.cyan);
						g.fillRect(content.mouse_x-1, content.mouse_y-i[1]+2, i[0]+2, i[1]);
						g.setColor(Color.black);
						g.drawString(txt_2draw, content.mouse_x, content.mouse_y);
					}
				}
			}
		}
		
		// check time to live
		if(this.time_to_live != -42) {
			if(((int) new Date().getTime()) - this.time_start > this.time_to_live) {
				this.dispose_me = true;
			}
		}
	}
	
	public void add_element(Window_component wc) {
		elements.add(wc);
	}
	
	// ttl in milliseconds
	public void set_time_to_live(int ttl) {
		this.time_to_live = (ttl<0)?0:ttl;
		time_start = (int) new Date().getTime();
	}
	
	public void remove_status_bar() {
		this.has_status_bar = false;
		
		// Draw elements higher
		status_bar_height /= 2;
	}
	public void add_status_bar() {
		this.has_status_bar = true;
		
		// Draw elements lower
		status_bar_height *= 2;
	}
	
	// Resizes window, so it contains all added elements
	public void set_size_2_elements() {
		// Fit y
		int max_y = 0; 
		int index = 0, i = 0;
		for(Window_component wc : elements) {
			int cur_y = wc.getY();
			if(cur_y > max_y) {
				max_y = cur_y;
				index = i;
			}
			i++;
		}
		
		int last_height = elements.get(index).getHeight();
		
		this.window_height = max_y + last_height + 7; // + some offset to look nice

		
		// Fit x
		int max_x = 0; 
		index = 0; i = 0;
		for(Window_component wc : elements) {
			int cur_x = wc.getX();
			if(cur_x > max_x) {
				max_x = cur_x;
				index = i;
			}
			i++;
		}
		
		int last_width = elements.get(index).getWidth();
		
		this.window_width = max_x + last_width + 8; // + some offset to look nice
	}
	
	public Rectangle get_rect() {
		return new Rectangle(move_x+window_x, move_y+window_y, window_width, window_height);
	}
	private Rectangle get_status_bar_rect() {
		return new Rectangle(
				move_x+window_x+status_bar_x, 
				move_y+window_y+status_bar_y, 
				status_bar_width, 
				status_bar_height
			);
	}
	private Rectangle get_close_button_rect() {
		return new Rectangle(
				move_x+window_x+close_button_x, 
				move_y+window_y+close_button_y, 
				close_button_width, 
				close_button_height
			);
	}
	
	public boolean dispose() {
		return dispose_me;
	}
	
	public void focus() {
		in_focus = true;
	}
	public void defocus() {
		in_focus = false;
	}
	public boolean in_focus() {
		return in_focus;
	}
	
	public void on_click(String button) {
		if(this.has_status_bar && get_close_button_rect().contains(content.mouse_x,content.mouse_y)) {
			dispose_me = true;
		}
	}
	public void on_drag(String button) {
		if (content.is_dragging_window) {
			move_x_tmp = (content.mouse_x - drag_start_x);
			move_y_tmp = (content.mouse_y - drag_start_y);
			move_x = move_x_tmp + old_move_x;
			move_y = move_y_tmp + old_move_y;
		}
	}
	public void on_press(String button) {
		in_focus = true;
		
		// Only drag if status bar is involved
		if(this.has_status_bar &&  get_status_bar_rect().contains(content.mouse_x, content.mouse_y)) {
			content.dragged_window = this;
    		content.is_dragging_window = true;
    		
			drag_start_x = content.mouse_x;
			drag_start_y = content.mouse_y;
		}
    }
	public void on_release(String button) {
		if (content.is_dragging_window) {
			old_move_x = move_x;
			old_move_y = move_y;
			content.is_dragging_window = false;
		}
	}
	
	public Window_component get_by_id(String id) {
		for(Window_component wc : elements) {
			if(wc.get_id().equals(id))
				return wc;
		}
		return null;
	}
	
	public void setWidth(int w) {
		this.window_width = w;
	}
	public int getWidth() {
		return this.window_width;
	}
	public int getHeight() {
		return this.window_height;
	}
	public void setHeight(int h) {
		this.window_height = h;
	}
	public void setX(int x) {
		this.window_x = x;
	}
	public void setY(int y) {
		this.window_y = y;
	}
}