package com.gmail.jdesmond10.simplebet;

import java.util.Objects;
import java.util.Random;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public final class GuiUtils {
	private GuiUtils() {
		throw new UnsupportedOperationException();
	}

	public static void runSafe(final Runnable runnable) {
		Objects.requireNonNull(runnable, "runnable");
		if (Platform.isFxApplicationThread()) {
			runnable.run();
		} else {
			Platform.runLater(runnable);
		}
	}

	public static void setPaneBackgroundColorRandomly(Pane pane) {
		Random r = new Random();
		pane.setBackground(new Background(new BackgroundFill(
				new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1),
				CornerRadii.EMPTY, Insets.EMPTY)));
	}
}