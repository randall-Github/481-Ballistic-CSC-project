package com.example.main;

import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Controller {
    private final MainView mainView;
    private Boolean onTrial, onStart, onCompleted, inBallistic;
    private Item prevItem, curSelectedItem, fCorrectionItem, bCorrectionItem;
    private Integer row_pred, col_pred;
    private Double prevX, prevY, speed;
    private CorrectionStack correctionStack;
    private double dx, dy;
    private int currTrial;
    private final int numTrials;
    private final int PERIOD_MS = 2;
    private final float BALLISTIC_SPEED = 20;
    private Instant startTime;
    private int corrections;
    private int errors;
    private List<Trial> trials;

    public Controller(MainView mainView){
        this.mainView = mainView;
        onStart = true;
        onTrial = false;
        onCompleted = false;
        inBallistic = false;

        prevX = null;
        prevY = null;

        prevItem = null;
        curSelectedItem = new Item(5, 5, (float) this.mainView.getSimCursor().oriX, (float) this.mainView.getSimCursor().oriY);

        this.currTrial = 0;
        this.numTrials = 15;

        correctionStack = new CorrectionStack();

        this.bindEvents();
    }

    private void updateItemPointersForPrediction(Item newCurItem) {
        prevItem = curSelectedItem;
        if (prevItem != null) {
            prevItem.setItem();
        }

        curSelectedItem = newCurItem;
        if (curSelectedItem != null) {
            curSelectedItem.setSelected();
        }
    }

    private void updateView() {
        if (curSelectedItem != null) {
            curSelectedItem.determineView();
        }
        if (prevItem != null) {
            prevItem.determineView();
        }
    }

    private void updateToNextTrial() {
        this.startTime = Instant.now();
        this.errors = 0;
        this.corrections = 0;

        this.setTarget(this.mainView.getItems());
        this.currTrial += 1;
        this.mainView.getSimCursor().resetCursorPosition();

        if (this.curSelectedItem != null) {
            this.curSelectedItem.setItem();
        }



        this.resetTrialState();
    }

    private void endTrials() {
        this.onTrial = false;
        this.onCompleted = true;
        System.out.println("Printing trials");
        try {
            FileWriter myWriter = new FileWriter("481TrialData.csv", false);
            myWriter.write("Trial, Fitts ID, Completion Time (ms), Errors, Corrections Used\n");
            int trialNum = 1;
            for (Trial trial : trials) {
                myWriter.write(String.format("%s, %s, %s, %s, %s\n", trialNum, trial.getFittsId(), trial.getCompletionTime(), trial.getErrors(), trial.getCorrectionsUsed()));
                trialNum += 1;
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Fail to write file to file");
            e.printStackTrace();
        }
        this.trials.forEach(trial -> System.out.println(trial));
        this.mainView.setCompletedDisplay();
    }

    public void setOnTrial(Boolean bool){
        this.onTrial = bool;
    }

    public void setTarget(ArrayList<Item> items){
        Random random = new Random();
        int x = random.nextInt(items.size());

        this.setOnTrial(true);

        for (int i = 0; i < items.size(); i++){
            items.get(i).setTarget(i == x);
        }
    }

    public void updateBackCorrectionItem() {
        if (correctionStack.peekStack() != null) {
            bCorrectionItem = correctionStack.peekStack();
            if (bCorrectionItem.getItemState() != Item.State.SELECTED) {
                bCorrectionItem.setBPredicted();
                bCorrectionItem.determineView();
            }
        }
    }

    public void resetBackCorrectionItem() {
        if (bCorrectionItem != null && bCorrectionItem.getItemState() != Item.State.SELECTED) {
            bCorrectionItem.setItem();
            bCorrectionItem.determineView();
        }
    }

    private double euclideanDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    private void saveTrial(Item item) {
        // TODO change the ? 0 to the origin coordinates instead
        float prevX = prevItem == null ? 0 : prevItem.getXPos();
        float prevY = prevItem == null ? 0 : prevItem.getYPos();
        double D = euclideanDistance(prevX, prevY, item.getXPos(), item.getYPos());
        double W = item.getItemWidth();
        double fittsId = Math.log((D/W) +1) / Math.log(2);
        double completionTime = Duration.between(startTime, Instant.now()).toMillis();
        this.trials.add(new Trial(fittsId, completionTime, errors, corrections));
    }

    //Selects item with cursor, updates display
    public void mouseClickItem(){
        boolean check = false;
        for(Item item: mainView.getItems()){
            if (fCorrectionItem != item) {
                item.setItem();
            }
            if(this.onTrial && item.mouseOnItem(mainView.getSimCursor().getX(), mainView.getSimCursor().getY())) {
                check = true;
                if (item.isTarget()) {
                    saveTrial(item);
                    updateToNextTrial();

                    if (this.currTrial >= numTrials){
                        endTrials();
                    }
                } else {
                    // TODO: Increment errors
                    this.errors += 1;
                    updateItemPointersForPrediction(item);
                    this.predictItem(item);
                }
            }
            updateBackCorrectionItem();
            item.determineView();
        }
        //Checking if the background was clicked instead
        //Since if the background is clicked then the predicted item stays
        //If we want the predicted item to stay, then just delete the check
        if (!check && fCorrectionItem != null){
            fCorrectionItem.setItem();
            fCorrectionItem.determineView();
            fCorrectionItem = null;
        }
    }


    public void jumpToPredicted(){
        if (fCorrectionItem != null) {
            updateItemPointersForPrediction(fCorrectionItem);

            if (curSelectedItem.isTarget()) {
                saveTrial(curSelectedItem);
                updateToNextTrial();

                if (this.currTrial >= numTrials) {
                    endTrials();
                }
            }
            else{
                // ADD to the correction count
                this.corrections += 1;
                this.predictItem(fCorrectionItem);
            }
            correctionStack.push(curSelectedItem);
            resetBackCorrectionItem();
            updateBackCorrectionItem();
            updateView();
        }

    }

    //Prediction function
    public void predictItem(Item currI) {
        if (prevItem != null && curSelectedItem != null) {
            if (fCorrectionItem != null) {
                fCorrectionItem.setItem();
                curSelectedItem.setSelected();
                fCorrectionItem.determineView();
            }
            double a = calculateRadians(curSelectedItem.getXPos() - prevItem.getXPos(), curSelectedItem.getYPos() - prevItem.getYPos());
            col_pred = (int) Math.round(Math.cos(a));
            row_pred = -(int) Math.round(Math.sin(a));

            int row = currI.getCoords().getValue1();
            int col = currI.getCoords().getValue2();

            fCorrectionItem = mainView.getItemIndex().get(new Pair<>(row + row_pred, col + col_pred));
            if (fCorrectionItem != null) {
                fCorrectionItem.setFPredicted();
                fCorrectionItem.determineView();
            }
        }
    }
    public void moveCursor(double dx, double dy, double factor){
        mainView.getSimCursor().move(dx, dy, factor);
    }

    private void bindEvents() {
        final LongProperty timeOfLastExecute = new SimpleLongProperty(System.currentTimeMillis());
        final Robot robot = new Robot();

        //KEY PRESSED EVENTS------------------------------------------
        mainView.getStartDisplay().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            System.out.println("Key pressed");
            if(e.getCode() == KeyCode.SPACE){
                this.startTime = Instant.now();
                this.errors = 0;
                this.corrections = 0;
                this.trials = new ArrayList<>();
                if (onStart && !onTrial && !onCompleted) {
                    this.mainView.setDisplay();
                    this.onStart = false;
                    this.mainView.getSimCursor().resetCursorPosition();
                    this.setTarget(mainView.getItems());
                }
            }
        });

        mainView.getCompletedDisplay().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE){
                Platform.exit();
            }
        });

        mainView.getTrialDisplay().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE){
                Platform.exit();
            }
            else if (e.getCode() == KeyCode.E && onTrial){
                if (fCorrectionItem != null) {
                    this.mainView.getSimCursor().setPosition(fCorrectionItem.getXPos(), fCorrectionItem.getYPos());
                }
                this.jumpToPredicted();
                this.mainView.draw();
            } else if (e.getCode() == KeyCode.Q && onTrial) {
                // TODO increase correction count
                this.corrections += 1;
                if (bCorrectionItem != null && correctionStack.peekStack() != null) {
                    Item previousItem = correctionStack.pop();
                    assert(bCorrectionItem == previousItem);
                    bCorrectionItem.setItem();
                    bCorrectionItem.setSelected();
                    if (curSelectedItem != null) {
                        curSelectedItem.setItem();
                        curSelectedItem.determineView();
                    }
                    curSelectedItem = bCorrectionItem;
                    curSelectedItem.determineView();
                    this.mainView.getSimCursor().setPosition(curSelectedItem.getXPos(), curSelectedItem.getYPos());
                }
                bCorrectionItem = correctionStack.peekStack();
                if (bCorrectionItem != null) {
                    bCorrectionItem.setBPredicted();
                    bCorrectionItem.determineView();
                }
                this.mainView.draw();
            }
        });


        //Mouse moved event
        mainView.getTrialDisplay().addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            if (onTrial) {
                if (System.currentTimeMillis() - timeOfLastExecute.get() > PERIOD_MS) {
                    if (e.getX() >= mainView.getTrialWidth() - 300 || e.getX() <= 300 || e.getY() >= mainView.getTrialHeight() - 200 || e.getY() <= 200) {
                        int half_rows = mainView.getTrialRow() / 2;
                        int half_cols = mainView.getTrialCol() / 2;
                        Item tp_item = mainView.getItemIndex().get(new Pair<>(half_rows, half_cols));
                        Point2D screenCoords = mainView.getMyCanvas().localToScreen(tp_item.getXPos(), tp_item.getYPos());

                        robot.mouseMove(screenCoords);
                        prevX = null;
                        prevY = null;
                    } else {
                        double curX = e.getX();
                        double curY = e.getY();

                        if (prevX == null || prevY == null) {
                            prevX = curX;
                            prevY = curY;
                        }

                        dx = (curX - prevX) / PERIOD_MS;
                        dy = (curY - prevY) / PERIOD_MS;

                        speed = Math.sqrt((dx * dx) + (dy * dy));

                        inBallistic = speed > BALLISTIC_SPEED;

                        prevX = curX;
                        prevY = curY;
                    }

                    timeOfLastExecute.set(System.currentTimeMillis());
                }

                //For dealing with ballistic motion and factor when cursor enters an item
                //As well as tracking the items that the ballistic motion passed over
                double factor = 1;
                for (Item item : mainView.getItems()) {
                    if (item.mouseOnItem(mainView.getSimCursor().getX(), mainView.getSimCursor().getY())) {
                        if (!inBallistic) {
                            factor = 0.05;
                        }

                        // LOGIC FOR REMOVING PREVIOUS CORRECTION ITEM
                        resetBackCorrectionItem();
                        // LOGIC FOR UPDATING TO CURRENT CORRECTION ITEM
                        updateBackCorrectionItem();
                        correctionStack.push(item);
                    }
                }
                this.moveCursor(dx, dy, factor);
                this.mainView.draw();
                //-------------------------------------------------------------------------
            }
        });

        //Mouse click event----------------------------------------
        mainView.getTrialDisplay().addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (onTrial){
                this.mouseClickItem();
                this.mainView.draw();
            }
        });
        //When mouse leaves the display, do not show the sim cursor
        mainView.getTrialDisplay().addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            if (onTrial) {
                this.mainView.getGC().clearRect(0, 0, mainView.getMyCanvas().getWidth(), mainView.getMyCanvas().getHeight());
            }
        });
    }

    public void resetTrialState() {
        this.correctionStack.resetStack();

        if (fCorrectionItem != null) {
            fCorrectionItem.setItem();
            fCorrectionItem.determineView();
        }
        resetBackCorrectionItem();

        this.prevItem = null;
        this.fCorrectionItem = null;
        this.curSelectedItem = new Item(5, 5, (float) this.mainView.getSimCursor().oriX, (float) this.mainView.getSimCursor().oriY);
        this.bCorrectionItem = null;
    }

    public static double calculateRadians(double x, double y) {
        double angle = Math.atan2(-y, x);
        return angle > 0 ? angle : 2*Math.PI + angle;
    }
}