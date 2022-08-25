package com.sih.disaster;

public class Versions {
    private String questions,answer;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Versions(String questions,String answer) {
        this.questions = questions;
        this.answer = answer;
        this.expandable = false;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Versions{" +
                "questions='" + questions + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
