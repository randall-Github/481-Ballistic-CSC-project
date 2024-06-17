package com.example.main;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class StartDisplay extends StackPane {
    public StartDisplay(){
        this.setPrefSize(1300, 790);

        Label startLabel = new Label("""
                On This Program, Your Goal Is To Select The Highlighted Target(YELLOW)
                The Cursor Will Be A Small Red Dot And Will Start On The Bottom Left Corner
                

                The Items Will Have A Sticky Property Which Will Make The Cursor Move Much Slower When Inside Of It
                The Cursor Has A Ballistic Motion Technique Which Will Allow You To Jump Over The Items
                To Activate This Technique, Simply Move The Mouse In A "Flick" Like Motion
                
                
                If An Item Which Is Not The Target Is Selected(RED),
                There Could Be A FORWARD Predicted Item(SKY BLUE),
                There Could Be A BACKWARD Predicted Item(Dark GRAY),
                If The Target Is A Predicted Item, It's Border Will Be Highlighted(LIGHT GREEN)
                
                
                
                To Jump To The FORWARD Prediction Item, Press "E"
                To Jump To The BACKWARD Prediction Item, Press "Q"
                To Exit Mid Trial, Press "ESC"
                
                You Must Complete Fifteen Consecutive Trials
                               
                To Start the Trials: Press "Spacebar"
                """);
        startLabel.setFont(new Font(20));
        startLabel.setTextAlignment(TextAlignment.CENTER);

        Button newButton = new Button();
        newButton.setStyle("""
                -fx-border-color: transparent;
                -fx-border-width: 0;
                -fx-border-radius: 0;
                -fx-background-color: transparent;
                """);
        VBox startBox = new VBox(startLabel, newButton);
        startBox.setPrefSize(this.widthProperty().floatValue(),this.heightProperty().floatValue());
        startBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(startBox, Priority.ALWAYS);
        HBox.setHgrow(startBox, Priority.ALWAYS);

        this.getChildren().add(startBox);

    }
}
