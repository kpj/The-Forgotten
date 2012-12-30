package user_interface.graphics.window;

import game.handler.content_handler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;


public class Window {
	
	content_handler content;
	
	String caption;
	
	Color window_color, window_border_color;
	int window_x, window_y, window_width, window_height;
	
	
	Color status_bar_color = Color.blue;
	int status_bar_x = 0;
	int status_bar_y = 0;
	int status_bar_width;
	int status_bar_height = 20;
	
	Color close_button_color = Color.gray;
	int close_button_x;
	int close_button_y = 0;
	int close_button_width = 20;
	int close_button_height = 20;
	
	
	int move_x, move_y;
	int move_x_tmp, move_y_tmp, drag_start_x, drag_start_y, old_move_x, old_move_y;
	
	boolean dispose_me = false;
	boolean in_focus = true;
	
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
		g.drawRect(
				window_xpos, 
				window_ypos, 
				window_width, 
				window_height
			);
		
		// draw status bar
		g.setColor(status_bar_color);
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
		
		// draw elements
		for(Window_component wc : elements) {
			wc.draw_content(g, window_xpos, window_ypos + status_bar_height*2);
		}
	}
	
	public void add_element(Window_component wc) {
		elements.add(wc);
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
		if(get_close_button_rect().contains(content.mouse_x,content.mouse_y)) {
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
		if(get_status_bar_rect().contains(content.mouse_x, content.mouse_y)) {
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
}