package info.seravee.wallmanager.beans.profile;

import java.awt.Rectangle;

public class Screen implements Cloneable {
	// Base data for the screen
	private int id;

	// Dimension of the scrren
	private int x;
	private int y;
	private int width;
	private int height;

	public Screen() {
	}
	
	public Screen(int id, Rectangle bounds) {
		this.id = id;
		this.x = bounds.x;
		this.y = bounds.y;
		this.width = bounds.width;
		this.height = bounds.height;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
