package user_interface.graphics.window;

import game.handler.content_handler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import user_interface.graphics.window.component.Image;
import user_interface.graphics.window.component.Label;

public class window_manager {
	
	content_handler content;
	
	ArrayList<Window> windows = new ArrayList<>();
	ArrayList<Window> to_rm = new ArrayList<>();
	
	public window_manager(content_handler con) {
		content = con;
	}
	
	public void add_window(String caption) {
		defocus_all();
		
		Window w = new Window(content, caption, 10, 10, 200, 200);
		w.add_element(new Label("Test Text", 0, 0));
		w.add_element(new Image(content.iml.get_img("/data/pics/non_walkable.png"), 0, 3, 100, 100));
		
		windows.add(w);
	}
	
	private void defocus_all() {
		for(Window w : windows) {
			w.defocus();
		}
	}
	
	public void draw_windows(Graphics g) {
		int len = windows.size();
		Window move_to_end = null;
		
		for(Window w : windows) {
			// draw window
			w.draw_me(g);
			
			// check if it should be deleted
			if (w.dispose_me) {
				to_rm.add(w);
			}
			
			// check for changing focus
			if(w.in_focus && windows.indexOf(w) != len-1) {
				defocus_all();
				w.focus();
				move_to_end = w;
			}
		}
		
		// Delete closed windows
		for(Window w : to_rm) {
			windows.remove(windows.indexOf(w));
		}
		to_rm.clear();

		// Check focus stuff
		if(move_to_end != null) {
			delete_and_append(move_to_end);
		}
	}
	
	private void delete_and_append(Window cur) {
		windows.remove(cur);
		windows.add(cur);
	}
	
	public ArrayList<Window> get_windows() {
		return windows;
	}
}