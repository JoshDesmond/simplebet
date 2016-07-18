package com.gmail.jdesmond10.simplebet.views;

import java.util.Arrays;
import java.util.Objects;

import com.gmail.jdesmond10.simplebet.App;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * The set of buttons on the bottom of the screen allowing you control your
 * actions during a betting game.
 * 
 * @author Josh Desmond
 *
 */
public class ActionButtonsView extends HBox {

	private App app;
	Button fold, check, bet;
	Button min, pot, max;
	Slider slider;
	TextField textField;

	public ActionButtonsView(App app) {
		this.app = Objects.requireNonNull(app);
		initViews();
	}

	private void initViews() {
		// Initialize all the actual controls
		fold = new Button("Fold");
		check = new Button("Check");
		bet = new Button("Bet");
		min = new Button("Min");
		pot = new Button("Pot");
		max = new Button("Max");
		slider = new Slider();
		textField = new TextField();

		// Initializes the grid for all the controls related to amount.
		GridPane amountGrid = new GridPane();
		amountGrid.add(min, 0, 0);
		amountGrid.add(pot, 1, 0);
		amountGrid.add(max, 2, 0);
		amountGrid.add(slider, 0, 1, 3, 1);
		amountGrid.add(textField, 0, 2, 3, 1);
		// Configures the grid a little more
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(33);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(33);
		ColumnConstraints column3 = new ColumnConstraints();
		column2.setPercentWidth(33);
		amountGrid.getColumnConstraints().addAll(column1, column2, column3);
		amountGrid.setPadding(new Insets(0, 0, 0, 5));
		amountGrid.setVgap(3);

		// Makes the width of the min, pot, and max buttons as long as they need
		// to be so they can be stretched.
		for (Button b : Arrays.asList(min, pot, max)) {
			b.setMaxWidth(Double.MAX_VALUE);
		}

		// Alters the three large buttons to be standard width and also have
		// expandable height.
		for (Button b : Arrays.asList(fold, check, bet)) {
			b.setPrefWidth(90);
			b.setMaxWidth(120);
			b.setMinWidth(60);
			b.setMaxHeight(100);
			b.setPadding(new Insets(5, 5, 5, 5));
		}

		this.setPadding(new Insets(5, 5, 5, 5));

		// Adds everything to the overall Pane
		this.getChildren().addAll(fold, check, bet, amountGrid);
		this.setAlignment(Pos.TOP_CENTER);

	}

}
