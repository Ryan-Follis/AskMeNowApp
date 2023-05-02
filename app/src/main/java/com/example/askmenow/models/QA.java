package com.example.askmenow.models;

import java.util.List;

public class QA {
    private String Question;
    private List<String> Answers;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public List<String> getAnswers() {
        return Answers;
    }

    public void setAnswers(List<String> answers) {
        Answers = answers;
    }
}