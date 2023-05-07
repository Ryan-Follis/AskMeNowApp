package com.example.askmenow.models;

import java.util.List;
import java.util.Map;

public class QA {
    private String qId;
    private String Question;
    private String questionAccess;
    private String toUser;
    private List<Map<String, String>> Answers;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public List<Map<String, String>> getAnswers() {
        return Answers;
    }

    public void setAnswers(List<Map<String, String>> answers) {
        Answers = answers;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getQuestionAccess() {
        return questionAccess;
    }

    public void setQuestionAccess(String questionAccess) {
        this.questionAccess = questionAccess;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
}