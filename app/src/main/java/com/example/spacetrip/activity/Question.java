package com.example.spacetrip.activity;

public class Question {
    private String question;
    private String[] answers;
    private int correctAnswerIndex;
    private int imageResId;

    public Question(String question, String[] answers, int correctAnswerIndex, int imageResId) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
        this.imageResId = imageResId;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public int getImageResId() {
        return imageResId;
    }
}
