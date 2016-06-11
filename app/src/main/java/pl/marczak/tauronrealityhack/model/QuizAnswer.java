package pl.marczak.tauronrealityhack.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016-06-11.
 */
public class QuizAnswer implements Serializable {

    String text;
    boolean correct;



    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuizAnswer(boolean correct, String text) {

        this.correct = correct;
        this.text = text;
    }
}
