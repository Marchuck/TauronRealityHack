package pl.marczak.tauronrealityhack.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Admin on 2016-06-11.
 */
public class QuizQuestion {
    private int id;
    private String question;
    private Set<QuizAnswer> answers = new HashSet<QuizAnswer>();

}
