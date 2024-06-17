package com.example.main;

import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.*;

public class MainView extends StackPane {
    private final Controller controller;
    private final TrialDisplay trialDisplay;
    private final StartDisplay startDisplay;
    private final CompletedDisplay completedDisplay;
    private final GraphicsContext gc;
    private final Canvas myCanvas;
    private SimCursor cursor;
    double originX, originY;
    public MainView() {
        this.setPrefSize(1300,790);

        this.trialDisplay = new TrialDisplay();
        this.startDisplay = new StartDisplay();
        this.completedDisplay = new CompletedDisplay();

        this.myCanvas = new Canvas(1300,790);
        this.gc = this.myCanvas.getGraphicsContext2D();

        this.originX = 30;
        this.originY = this.getPrefHeight()- 30;

        //Update the canvas and the sim cursor bound for when the display size changes
        this.widthProperty().addListener((e) -> {
            myCanvas.setWidth(this.widthProperty().doubleValue());
            cursor.maxX = this.widthProperty().doubleValue();
        });
        this.heightProperty().addListener((e) -> {
            myCanvas.setHeight(this.heightProperty().doubleValue());
            this.originY = this.heightProperty().doubleValue() - 30;
            this.cursor.maxY = this.heightProperty().doubleValue();
            this.cursor.setOriginY(this.originY);
        });
        this.myCanvas.setMouseTransparent(true);

        this.cursor = new SimCursor(this.originX, this.originY, 0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        this.controller = new Controller(this);

        this.setStartDisplay();
    }

    //Program displays ---------------------------------------------------------
    //The trail display
    public void setDisplay(){
        this.getChildren().clear();
        this.getChildren().addAll(trialDisplay,myCanvas);
    }
    //The startup display that would show the user the instructions
    public void setStartDisplay(){
        this.getChildren().clear();
        this.getChildren().addAll(startDisplay, myCanvas);
    }
    //The end display to show the end of the trial
    public void setCompletedDisplay(){
        System.out.println("Completed");
        this.getChildren().clear();
        this.getChildren().addAll(completedDisplay, myCanvas);
    }

    public int getTrialRow(){
        return trialDisplay.getRows();
    }
    public int getTrialCol(){
        return trialDisplay.getCols();
    }
    public double getTrialWidth(){
        return trialDisplay.getWidth();
    }
    public double getTrialHeight(){
        return trialDisplay.getHeight();
    }
    public ArrayList<Item> getItems(){
        return trialDisplay.getItems();
    }
    public Map<Pair<Integer, Integer>, Item> getItemIndex(){
        return trialDisplay.getItemsIndex();
    }
    public TrialDisplay getTrialDisplay(){
        return this.trialDisplay;
    }
    public StartDisplay getStartDisplay(){
        return this.startDisplay;
    }
    public CompletedDisplay getCompletedDisplay(){
        return this.completedDisplay;
    }
    public SimCursor getSimCursor(){
        return this.cursor;
    }
    public Canvas getMyCanvas(){
        return this.myCanvas;
    }

    public GraphicsContext getGC(){
        return this.gc;
    }
    public void draw(){
        gc.clearRect(0,0,myCanvas.getWidth(), myCanvas.getHeight());

        gc.setFill(Color.RED);
        gc.fillOval(cursor.getX() - cursor.getR(), cursor.getY() - cursor.getR(), cursor.getR() * 2, cursor.getR() * 2);

    }
}
