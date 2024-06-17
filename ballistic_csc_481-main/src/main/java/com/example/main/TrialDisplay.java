package com.example.main;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.*;

public class TrialDisplay extends StackPane {
    private int rows = 0, cols = 0;
    private final ArrayList<Item> items;
    private final Map<Pair<Integer, Integer>, Item> itemsIndex;
    public TrialDisplay(){
        this.setPrefSize(1300, 790);

        this.items = new ArrayList<>();
        this.itemsIndex = new HashMap<>();

        ArrayList<HBox> hBoxes = new ArrayList<>();
        Random random = new Random();

        HBox paddingHB = new HBox();
        paddingHB.setPrefSize(100,this.heightProperty().floatValue());
        paddingHB.setAlignment(Pos.BOTTOM_LEFT);
        VBox.setVgrow(paddingHB, Priority.ALWAYS);

        HBox originHBox = new HBox();
        originHBox.setMaxHeight(60);
        originHBox.setPrefWidth(60);
        originHBox.setStyle("-fx-border-color: black\n");
        originHBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,null,null)));
        paddingHB.getChildren().add(originHBox);

        HBox alignHBoxes = new HBox();
        HBox.setHgrow(alignHBoxes,Priority.ALWAYS);
        VBox.setVgrow(alignHBoxes,Priority.ALWAYS);

        if (rows == 0 && cols == 0) {
            rows = random.nextInt(6, 8);
            cols = random.nextInt(8, 10);
        }

        //items.clear();
        for (int i = 0; i <= rows; i++) {
            HBox newHBox = new HBox();
            newHBox.setPrefSize(this.widthProperty().floatValue() - 100, (this.heightProperty().floatValue() / rows) - (70.0/rows));
            HBox.setHgrow(newHBox, Priority.ALWAYS);
            VBox.setVgrow(newHBox, Priority.ALWAYS);
            for (int j = 0; j <= cols; j++) {
                VBox newVBox = new VBox();
                newVBox.setPrefSize(newHBox.widthProperty().floatValue() / cols, newHBox.heightProperty().floatValue());
                newVBox.setAlignment(Pos.CENTER);
                VBox.setVgrow(newVBox, Priority.ALWAYS);
                HBox.setHgrow(newVBox, Priority.ALWAYS);

                Item newItem = new Item((float) (newVBox.widthProperty().floatValue() / 1.3), (float) (newVBox.heightProperty().floatValue() / 1.3));
                newVBox.getChildren().add(newItem.getButton());

                int finalI = i;
                int finalJ = j;
                newVBox.widthProperty().addListener((e) -> {
                    newItem.setItemWidth((float) (newVBox.widthProperty().floatValue() / 1.3));
                    newItem.setXPos(((newVBox.widthProperty().floatValue() * (finalJ + 1)) - (newVBox.widthProperty().floatValue() / 2)) + 100);
                });
                newVBox.heightProperty().addListener((e) -> {
                    newItem.setItemHeight((float) (newVBox.heightProperty().floatValue() / 1.3));
                    newItem.setYPos((newHBox.heightProperty().floatValue() * (finalI + 1)) - (newHBox.heightProperty().floatValue() / 2));
                });

                this.items.add(newItem);
                Pair<Integer, Integer> coordinate = new Pair<>(finalI, finalJ);
                newItem.setCoords(coordinate);
                this.itemsIndex.put(coordinate, newItem);
                newHBox.getChildren().add(newVBox);
            }
            hBoxes.add(newHBox);
        }
        VBox alignBody = new VBox();
        alignBody.setPrefSize(this.getWidth(), this.getHeight());
        for (HBox hBox : hBoxes) {
            alignBody.getChildren().add(hBox);
        }
        alignBody.setStyle("-fx-border-color: black");
        HBox.setHgrow(alignBody, Priority.ALWAYS);
        VBox.setVgrow(alignBody,Priority.ALWAYS);

        HBox bottomPadding = new HBox();
        bottomPadding.setPrefSize(this.widthProperty().floatValue() - 50,70);
        HBox.setHgrow(bottomPadding,Priority.ALWAYS);

        VBox alignAll = new VBox(alignBody,bottomPadding);
        HBox.setHgrow(alignAll,Priority.ALWAYS);
        VBox.setVgrow(alignAll,Priority.ALWAYS);

        alignHBoxes.getChildren().addAll(paddingHB,alignAll);

        this.getChildren().add(alignHBoxes);
    }
    public int getRows(){
        return this.rows;
    }
    public int getCols(){
        return this.cols;
    }
    public Map<Pair<Integer, Integer>, Item> getItemsIndex(){
        return this.itemsIndex;
    }
    public ArrayList<Item> getItems(){
        return this.items;
    }
}
