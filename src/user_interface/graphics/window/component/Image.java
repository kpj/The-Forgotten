package user_interface.graphics.window.component;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import user_interface.graphics.window.Window_component;

public class Image implements Window_component{

	BufferedImage image;
	int image_x, image_y;
	int image_width, image_height;
	
	String popup_text = "";
	
	int origin_x, origin_y;
	
	String id = "";
	
	public Image(BufferedImage img, int x, int y, int w, int h) {
		image = img;
		image_x = x;
		image_y = y;
		image_width = w;
		image_height = h;
	}
	public Image(BufferedImage img, int x, int y) {
		image = img;
		image_x = x;
		image_y = y;
		image_width = img.getWidth();
		image_height = img.getHeight();
	}
	
	@Override
	public void draw_content(Graphics g, int origin_x, int origin_y) {
		this.origin_x = origin_x;
		this.origin_y = origin_y;
		
		g.drawImage(image, origin_x+image_x, origin_y+image_y, image_width, image_height, null);
	}
	
	public Rectangle get_rect() {
		return new Rectangle(origin_x+image_x, origin_y+image_y, image_width, image_height);
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
	
	public void update_image(BufferedImage new_img) {
		this.image = new_img;
	}
	
	public int getX() {
		return image_x;
	}
	public int getY() {
		return image_y;
	}
	public int getWidth() {
		return image_width;
	}
	public int getHeight() {
		return image_height;
	}
	
	public void set_id(String id) {
		this.id = id;
	}
	public String get_id() {
		return id;
	}
}
