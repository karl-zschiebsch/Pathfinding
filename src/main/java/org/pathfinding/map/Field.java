package org.pathfinding.map;

import java.util.ArrayList;
import java.util.List;

import org.pathfinding.App;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Field extends Rectangle {

	private final Map map;
	private final int posX, posY;

	private StateType state;

	public Field(Map map, int x, int y) {
		this.map = map;
		this.posX = x;
		this.posY = y;

		setState(StateType.EMPTY);
		setLayoutX(x * 50);
		setLayoutY(y * 50);
		setHeight(50);
		setWidth(50);
		setStroke(Color.BLACK);
		setStrokeWidth(2);
		addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, e -> {
			if (App.STARTED == false) {
				if (e.getButton() == MouseButton.PRIMARY)
					setState((isEmpty()) ? StateType.BLOCKED : StateType.EMPTY);
				if (e.getButton() == MouseButton.SECONDARY)
					setState((TURN_MARKET.isEmpty()) ? StateType.START : StateType.END);
			}
		});
	}

	public static List<Field> TURN_MARKET = new ArrayList<>();

	public void mark() {
		if (App.STOPPED == true) {
			return;
		}

		for (Field field : getNeighbours()) {
			if (field.isEmpty()) {
				field.setTraceback(this).setState(StateType.FILLED);

				TURN_MARKET.add(field);
			}
			if (field.isEnd()) {
				App.STOPPED = true;
				traceback();
				return;
			}
		}
	}

	private List<Field> getNeighbours() {
		List<Field> neighbours = new ArrayList<>();

		for (int x = posX - 1; x <= posX + 1; x += 2) {
			if (map.isOutside(x, posY))
				continue;
			neighbours.add(map.getField(x, posY));
		}

		for (int y = posY - 1; y <= posY + 1; y += 2) {
			if (map.isOutside(posX, y))
				continue;
			neighbours.add(map.getField(posX, y));
		}

		for (int x = posX - 1; x <= posX + 1; x += 2) {
			for (int y = posY - 1; y <= posY + 1; y += 2) {
				if (map.isOutside(x, y))
					continue;
				neighbours.add(map.getField(x, y));
			}
		}

		return neighbours;
	}

	private Field traceback;

	public void traceback() {
		if (!isStart()) {
			setState(StateType.TRACE);
			traceback.traceback();
		}
	}

	public Field setTraceback(Field field) {
		traceback = field;
		return this;
	}

	public boolean isStart() {
		return state == StateType.START;
	}

	public boolean isEnd() {
		return state == StateType.END;
	}

	public boolean isEmpty() {
		return state == StateType.EMPTY;
	}

	public boolean isFilled() {
		return state == StateType.FILLED;
	}

	public boolean isBlocked() {
		return state == StateType.BLOCKED;
	}

	public Field setState(StateType state) {
		if (this.state == StateType.START)
			TURN_MARKET.remove(this);

		this.state = state;
		setFill(switch (state) {
		case START -> Color.BLUE;
		case END -> Color.ORANGE;
		case EMPTY -> Color.WHITE;
		case FILLED -> Color.LIGHTBLUE;
		case BLOCKED -> Color.DARKBLUE;
		case TRACE -> Color.CORAL;
		});

		if (state == StateType.START)
			TURN_MARKET.add(this);

		return this;
	}

	enum StateType {
		START, END, EMPTY, FILLED, BLOCKED, TRACE
	}
}
