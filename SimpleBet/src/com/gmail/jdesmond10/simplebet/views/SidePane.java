package com.gmail.jdesmond10.simplebet.views;

import java.util.Objects;

import com.gmail.jdesmond10.simplebet.App;
import com.gmail.jdesmond10.simplebet.GuiUtils;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * The side pane when in an active gambling game. Contains a console for IO and
 * optional buttons at the top right for menu related stuff.
 * 
 * @author Josh Desmond
 *
 */
public class SidePane extends BorderPane {
	private App app;
	Console console;

	public SidePane(App app) {
		super();
		this.app = Objects.requireNonNull(app);
		initViews();
	}

	private void initViews() {
		// Initialize objects
		console = new Console();
		Pane placeHolderPane = new Pane();
		GuiUtils.setPaneBackgroundColorRandomly(placeHolderPane);
		placeHolderPane.setMinHeight(60);
		placeHolderPane.setMaxHeight(1000);
		console.setMinHeight(60);
		console.setMaxHeight(1000);
		console.setPrefHeight(250);
		placeHolderPane.setPrefHeight(150);
		
		// Add them to the pane
		this.setTop(placeHolderPane);
		this.setBottom(console);

		// Configure this pane's size
		this.setMinSize(150, 200);
		console.setPadding(new Insets(10, 10, 10, 10));
	}

}
