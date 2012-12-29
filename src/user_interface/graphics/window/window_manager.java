package user_interface.graphics.window;

import game.handler.content_handler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class window_manager {
	
	content_handler content;
	
	ArrayList<Window> windows = new ArrayList<>();
	ArrayList<Window> to_rm = new ArrayList<>();
	
	public window_manager(content_handler con) {
		content = con;
		
		windows.add(new Window(content, "Test123", 10, 20, 100, 200));
		windows.add(new Window(content, "Maulwurf", 100, 70, 600, 200));
		windows.add(new Window(content, "Hammar", 500, 20, 100, 30));
		windows.add(new Window(content, "Tennis", 300, 600, 500, 500));
	}
	
	public void draw_windows(Graphics g) {
		for(Window w : windows) {
			w.draw_me(g);
			if (w.dispose_me) {
				to_rm.add(w);
			}
		}
		
		// Delete closed windows
		for(Window w : to_rm) {
			windows.remove(windows.indexOf(w));
		}
		to_rm.clear();
	}
	
	public ArrayList<Window> get_windows() {
		return windows;
	}
}