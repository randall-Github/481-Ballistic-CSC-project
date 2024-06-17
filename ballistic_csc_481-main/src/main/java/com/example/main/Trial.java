package com.example.main;

public class Trial {
    private final double fittsId;
    private final double completionTime;
    private final int errors;
    private final int correctionsUsed;

    public Trial(double fittsId, double completionTime, int errors, int correctionsUsed) {
        this.fittsId = fittsId;
        this.completionTime = completionTime;
        this.errors = errors;
        this.correctionsUsed = correctionsUsed;
    }

    public double getCompletionTime() {
        return completionTime;
    }

    public double getFittsId() {
        return fittsId;
    }

    public int getCorrectionsUsed() {
        return correctionsUsed;
    }

    public int getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return String.format("Fitts ID: %s | Completion Time: %s | Errors: %s | Corrections Used: %s",
                                fittsId, completionTime, errors, correctionsUsed);
    }

}
