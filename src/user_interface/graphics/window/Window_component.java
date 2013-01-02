package user_interface.graphics.window;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface Window_component {
	
	public void draw_content(Graphics g, int origin_x, int origin_y);
	
	public Rectangle get_rect();
	
	public void set_popup_text(String text);
	public String get_popup_text();
	public boolean popup_set();
}
