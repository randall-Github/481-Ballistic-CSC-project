package com.example.main;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CompletedDisplay extends StackPane {
    public CompletedDisplay() {
        Label label = new Label("Trial Completed");
        label.setFont(new Font(30));
        Button newButton = new Button();
        newButton.setStyle("""
                -fx-border-color: transparent;
                -fx-border-width: 0;
                -fx-border-radius: 0;
                -fx-background-color: transparent;
                """);
        VBox startBox = new VBox(label, newButton);
        startBox.setPrefSize(this.widthProperty().floatValue(),this.heightProperty().floatValue());
        startBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(startBox, Priority.ALWAYS);
        HBox.setHgrow(startBox, Priority.ALWAYS);

        this.getChildren().add(startBox);
    }
}
