package pl.marczak.tauronrealityhack.model;

/**
 * Created by Admin on 2016-06-11.
 */
public class QuizAnswer {

    String answer;
    boolean isCorrect;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
