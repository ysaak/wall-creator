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
	
}
