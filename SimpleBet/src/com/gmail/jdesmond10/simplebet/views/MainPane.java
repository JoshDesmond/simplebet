package com.gmail.jdesmond10.simplebet.views;

import java.util.Objects;

import com.gmail.jdesmond10.simplebet.App;
import com.gmail.jdesmond10.simplebet.GuiUtils;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * This is the main screen of a gambling game.
 * 
 * It contains a side bar on the right with a console, and an ActionButtonsView
 * on the bottom.
 * 
 * @author Josh Desmond
 *
 */
public class MainPane extends BorderPane {

	ActionButtonsView actionButtons;
	SidePane sidePane;
	private App app;

	public MainPane(App app) {
		super();
		this.app = Objects.requireNonNull(app);
		initViews();
	}

	private void initViews() {
		actionButtons = new ActionButtonsView(app);
		sidePane = new SidePane(app);

		StackPane contentPane = new StackPane();
		BorderPane middlePane = new BorderPane();

		this.setRight(sidePane);
		this.setCenter(middlePane);
		middlePane.setCenter(contentPane);
		middlePane.setBottom(actionButtons);

		// Sets minimum sizes of panes
		contentPane.setMinSize(300, 300);
		
		// Sets the color of panels for visual clarity
		{
			GuiUtils.setPaneBackgroundColorRandomly(contentPane);
			GuiUtils.setPaneBackgroundColorRandomly(sidePane);
			GuiUtils.setPaneBackgroundColorRandomly(actionButtons);
		}
	}

}
