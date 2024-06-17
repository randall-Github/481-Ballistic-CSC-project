package com.example.main;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView);

        scene.setCursor(Cursor.NONE);
        stage.setTitle("481 Project");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}