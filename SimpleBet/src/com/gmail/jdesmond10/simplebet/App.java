package com.gmail.jdesmond10.simplebet;

import com.gmail.jdesmond10.simplebet.views.MainPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(new MainPane(this)));
		primaryStage.show();
	}

}
