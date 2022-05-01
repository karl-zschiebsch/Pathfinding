package org.pathfinding.map;

import javafx.scene.Group;

public final class Map extends Group {

	private final Field[] fields;
	private final int height, width;

	public Map(int width, int height) {
		this.fields = new Field[width * height];
		this.width = width;
		this.height = height;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				fields[getFieldNumber(x, y)] = new Field(this, x, y);
			}
		}
		getChildren().addAll(fields);
	}

	public boolean isOutside(int posX, int posY) {
		if (posX >= getMapWidth() || posX < 0) {
			return true;
		}
		if (posY >= getMapHeight() || posY < 0) {
			return true;
		}
		return false;
	}

	public Field getField(int x, int y) {
		return fields[getFieldNumber(x, y)];
	}

	private int getFieldNumber(int posX, int posY) {
		return posY * getMapWidth() + posX;
	}

	public int getMapHeight() {
		return height;
	}

	public int getMapWidth() {
		return width;
	}
}
