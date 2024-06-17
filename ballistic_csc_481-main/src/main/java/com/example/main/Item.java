package com.example.main;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Item {
    public enum State {
        ITEM,SELECTED, FPREDICTION, BPREDICTION;
    }
    private State itemState = State.ITEM;
    private final Background itemBG, targetItemBG, selectedItemBG, fPredictionItemBg, bPredictionItemBg;
    private final Button button;
    private Boolean target;
    private float itemWidth, itemHeight;
    private float xPos, yPos;
    private Pair<Integer, Integer> coordinate;

    public Item(float w, float h, float x, float y) {
        this(w, h);
        this.xPos = x;
        this.yPos = y;
    }

    public Item(float w, float h){
        this.itemWidth = w;
        this.itemHeight = h;
        this.xPos = 0;
        this.yPos = 0;

        this.target = false;

        this.button = new Button();
        this.button.setPrefSize(this.itemWidth,this.itemHeight);

        this.itemBG = new Background(new BackgroundFill(Color.LIGHTGRAY,new CornerRadii(20),null));
        this.targetItemBG = new Background(new BackgroundFill(Color.YELLOW,new CornerRadii(20),null));
        this.selectedItemBG = new Background(new BackgroundFill(Color.LIGHTCORAL,new CornerRadii(20),null));
        this.fPredictionItemBg = new Background(new BackgroundFill(Color.ALICEBLUE,new CornerRadii(20),null));
        this.bPredictionItemBg = new Background(new BackgroundFill(Color.DARKGRAY,new CornerRadii(20),null));

        this.button.setBackground(itemBG);
        this.determineView();
    }
    public void setTarget(Boolean bool){
        this.target = bool;
        determineView();
    }
    public Button getButton(){
        return this.button;
    }
    public Boolean isTarget(){
        return this.target;
    }
    public void setItemWidth(float width){
        this.itemWidth = width;
        this.getButton().setPrefWidth(this.itemWidth);
    }
    public float getItemWidth(){
        return this.itemWidth;
    }
    public void setItemHeight(float height){
        this.itemHeight = height;
        this.getButton().setPrefHeight(this.itemHeight);
    }
    public float getItemHeight(){
        return this.itemHeight;
    }
    public void setSelected() {
        this.itemState = State.SELECTED;
    }
    public void setItem(){this.itemState = State.ITEM;}
    public void setFPredicted(){this.itemState = State.FPREDICTION;}
    public void setBPredicted(){this.itemState = State.BPREDICTION;}
    public State getItemState(){return this.itemState;}

    public void determineView() {
        this.button.setStyle("""
                -fx-border-color: #000000;
                fx-border-width: 1;
                -fx-border-radius: 20;
                """);

        if (!this.target) {
            switch (itemState){
                case ITEM -> {
                    this.getButton().setBackground(this.itemBG);
                }
                case SELECTED -> {
                    this.getButton().setBackground(this.selectedItemBG);
                }
                case FPREDICTION -> {
                    this.getButton().setBackground(this.fPredictionItemBg);
                }
                case BPREDICTION -> {
                    this.getButton().setBackground(this.bPredictionItemBg);
                }
            }
        } else {
            switch (itemState){
                case FPREDICTION -> {
                    this.button.setStyle("""
                            -fx-border-color: #b3ff74;
                            fx-border-width: 10;
                            -fx-border-radius: 20;
                            """);
                }
                case BPREDICTION -> {
                    this.button.setStyle("""
                            -fx-border-color: #e5b8f5;
                            fx-border-width: 10;
                            -fx-border-radius: 20;
                            """);
                }
            }
            this.getButton().setBackground(targetItemBG);
        }

    }

    public Boolean mouseOnItem(double x, double y){
        return ((x > (this.getXPos() - this.getItemWidth()/2) && x < (this.getXPos() + this.getItemWidth()/2))
                && (y > (this.getYPos() - this.getItemHeight()/2) && y < (this.getYPos() + this.getItemHeight()/2)));
    }
    public void setXPos(float x){
        this.xPos = x;
    }
    public float getXPos(){
        return this.xPos;
    }
    public void setYPos(float y){
        this.yPos = y;
    }
    public float getYPos(){
        return this.yPos;
    }
    public void setCoords(Pair<Integer, Integer> coords) {
        this.coordinate = coords;
    }
    public Pair<Integer, Integer> getCoords() {
        return this.coordinate;
    }

    @Override
    public String toString() {
        return String.format("Item (%s, %s)", coordinate.getValue1(), coordinate.getValue2());
    }
}
