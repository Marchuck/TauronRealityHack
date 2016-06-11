package pl.marczak.tauronrealityhack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016-06-11.
 */
public class QuizQuestion {
    private int id;
    private String question;

    public List<QuizAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAnswer> answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private List<QuizAnswer> answers = new ArrayList<>();

}
