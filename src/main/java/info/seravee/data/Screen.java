package info.seravee.data;

import java.awt.Rectangle;

public class Screen {
	private final int id;
	private final Rectangle bounds;
	
	public Screen(int id, Rectangle bounds) {
		this.id = id;
		this.bounds = bounds;
	}

	public int getId() {
		return id;
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public int getX() {
		return bounds.x;
	}
	
	public int getY() {
		return bounds.y;
	}
	
	public int getWidth() {
		return bounds.width;
	}
	
	public int getHeight() {
		return bounds.height;
	}
	
}
