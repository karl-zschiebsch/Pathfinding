package org.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.pathfinding.map.Field;
import org.pathfinding.map.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

	public static boolean STARTED = false;
	public static boolean STOPPED = false;

	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

	@Override
	public void init() throws Exception {
		service.scheduleAtFixedRate(() -> {
			if (STARTED ^ STOPPED) {

				List<Field> marks = new ArrayList<>(Field.TURN_MARKET);
				Field.TURN_MARKET.clear();
				marks.forEach(c -> c.mark());

				if (Field.TURN_MARKET.isEmpty())
					STOPPED = true;
			}
		}, 0, 500, TimeUnit.MILLISECONDS);
	}

	private BorderPane pane = new BorderPane(new Map(27, 14));

	@Override
	public void start(Stage stage) throws Exception {
		stage.setOnCloseRequest(e -> {
			service.shutdown();
		});
		stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.SPACE)
				STARTED = true;
			if (STARTED ^ STOPPED)
				return;
			if (e.getCode() == KeyCode.R) {
				pane.setCenter(new Map(27, 14));

				STARTED = false;
				STOPPED = false;

				Field.TURN_MARKET.clear();
			}
		});
		stage.setTitle("A*-Pathfinding");
		stage.setScene(new Scene(pane));
		stage.setResizable(false);
		stage.show();
	}

}
