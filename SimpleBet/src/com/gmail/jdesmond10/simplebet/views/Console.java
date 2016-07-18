package com.gmail.jdesmond10.simplebet.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.gmail.jdesmond10.simplebet.GuiUtils;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

/**
 * This is an input output console. It was written by Skiwi and is taken from
 * StackExchange.
 * 
 * Source:
 * http://codereview.stackexchange.com/questions/52197/console-component-in-
 * javafx
 */
public class Console extends BorderPane {
	protected final TextArea textArea = new TextArea();
	protected final TextField textField = new TextField();

	protected final List<String> history = new ArrayList<>();
	protected int historyPointer = 0;

	private Consumer<String> onMessageReceivedHandler;

	public Console() {
		textArea.setEditable(false);
		setCenter(textArea);

		textField.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			switch (keyEvent.getCode()) {
			case ENTER:
				String text = textField.getText();
				textArea.appendText(text + System.lineSeparator());
				history.add(text);
				historyPointer++;
				if (onMessageReceivedHandler != null) {
					onMessageReceivedHandler.accept(text);
				}
				textField.clear();
				break;
			case UP:
				if (historyPointer == 0) {
					break;
				}
				historyPointer--;
				GuiUtils.runSafe(() -> {
					textField.setText(history.get(historyPointer));
					textField.selectAll();
				});
				break;
			case DOWN:
				if (historyPointer == history.size() - 1) {
					break;
				}
				historyPointer++;
				GuiUtils.runSafe(() -> {
					textField.setText(history.get(historyPointer));
					textField.selectAll();
				});
				break;
			default:
				break;
			}
		});
		setBottom(textField);

		this.setMaxHeight(500);
		this.setMinWidth(100);
		this.setMaxWidth(200);
		textArea.setMaxHeight(500);
		textArea.setPrefHeight(500);
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		textField.requestFocus();
	}

	public void setOnMessageReceivedHandler(
			final Consumer<String> onMessageReceivedHandler) {
		this.onMessageReceivedHandler = onMessageReceivedHandler;
	}

	public void clear() {
		GuiUtils.runSafe(() -> textArea.clear());
	}

	public void print(final String text) {
		Objects.requireNonNull(text, "text");
		GuiUtils.runSafe(() -> textArea.appendText(text));
	}

	public void println(final String text) {
		Objects.requireNonNull(text, "text");
		GuiUtils.runSafe(
				() -> textArea.appendText(text + System.lineSeparator()));
	}

	public void println() {
		GuiUtils.runSafe(() -> textArea.appendText(System.lineSeparator()));
	}
}