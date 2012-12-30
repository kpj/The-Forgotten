package user_interface.graphics.window.component;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import user_interface.graphics.window.Window_component;

public class Image implements Window_component{

	BufferedImage image;
	int image_x, image_y;
	int image_width, image_height;
	
	public Image(BufferedImage img, int x, int y, int w, int h) {
		image = img;
		image_x = x;
		image_y = y;
		image_width = w;
		image_height = h;
	}
	
	@Override
	public void draw_content(Graphics g, int origin_x, int origin_y) {
		g.drawImage(image, origin_x+image_x, origin_y+image_y, image_width, image_height, null);
	}
	
	
}
