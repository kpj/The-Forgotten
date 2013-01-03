package user_interface.graphics.window;

import game.Entity.Char;
import game.handler.content_handler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map;

import user_interface.graphics.window.component.Bar;
import user_interface.graphics.window.component.Image;
import user_interface.graphics.window.component.Label;

public class window_manager {
	
	content_handler content;
	
	ArrayList<Window> windows = new ArrayList<>();
	ArrayList<Window> to_rm = new ArrayList<>();
	
	public window_manager(content_handler con) {
		content = con;
	}
	
	public void add_window(Window win) {		
		if(content.is_dragging_window)
			win.defocus();
		else
			defocus_all();
		
		windows.add(win);
	}
	public void add_character_window(Char c) {
		Window w = new Window(content, c.name, 10, 10, 500, 200);
		if(content.is_dragging_window)
			w.defocus();
		else
			defocus_all();
		
		int upper = 0;
        for (Map.Entry<String, Integer> ob : c.property_current.entrySet()) {
        	String inp = ob.getKey() + " : " + Math.round(ob.getValue()/10);
        	int[] res = content.get_string_information(inp);
            w.add_element(new Label(inp, 3, upper, res[0], res[1]));
            upper += 14;
        }
        
        float curv = (float)c.property_current.get("lebenspunkte");
        float maxv = (float)c.property_max.get("lebenspunkte");
        Bar b = new Bar(400, 3, 20, 100, false, (int) (100/maxv*curv));
        b.set_popup_text("Lebenspunkte");
        w.add_element(b);
		
		windows.add(w);
	}
	public void add_notification(String noti) {
		int[] i = content.get_string_information(noti);
		
		Window w = new Window(content, "abc123", content.window_width/2+100-i[0]/2, 5, 200, 100);
		
		w.set_time_to_live(2000);
		w.remove_status_bar();
		w.add_element(new Label(noti, 0, 0, i[0], i[1]));
		w.set_size_2_elements();
		w.defocus();
		
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
		
		try {
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
		}
		catch (ConcurrentModificationException e) {}
		
		try {
			// Delete closed windows
			for(Window w : to_rm) {
				windows.remove(windows.indexOf(w));
			}
			to_rm.clear();
		}
		catch (ConcurrentModificationException e) {}

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
	
	public void update_positions() {
		//fight menu
		if(content.fight != null) {
			content.fight.information_window.setWidth(content.window_width);
			content.fight.information_window.setHeight(170);
			content.fight.information_window.setX(0);
			content.fight.information_window.setY(content.window_height-170);
		}
	}
}