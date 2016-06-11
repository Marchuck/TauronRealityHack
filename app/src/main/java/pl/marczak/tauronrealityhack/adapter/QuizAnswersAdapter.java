package pl.marczak.tauronrealityhack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import pl.marczak.tauronrealityhack.L;
import pl.marczak.tauronrealityhack.R;
import pl.marczak.tauronrealityhack.model.QuizQuestion;

/**
 * Created by jpluta on 11.04.16.
 */
public class QuizAnswersAdapter extends ArrayAdapter<String> {

    QuizQuestion data;
    private LayoutInflater inflater;
    Context mContext;

    List<CheckBox> answersCheckboxes = new ArrayList<>();


    public QuizAnswersAdapter(Context context, QuizQuestion data) {
        super(context, R.layout.layout_quiz_answer_option);
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.getAnswers().size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        String answer = data.getAnswers().get(position).getAnswer();


        final LinearLayout answerLayout = (LinearLayout) inflater.inflate(R.layout.layout_quiz_answer_option, parent, false);
        final CheckBox cbAnswer = (CheckBox) answerLayout.findViewById(R.id.option);
        cbAnswer.setText(answer);
        answersCheckboxes.add(cbAnswer);

        cbAnswer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                L.d("QuizAnswersAdapter.onCheckedChanged() ");
                for (CheckBox c: answersCheckboxes){

                    c.setChecked(false);
                }
                if (isChecked){
                    buttonView.setChecked(true);
                }
            }
        });


     return answerLayout;
    }


    public int getCheckedAnswerId(){
        for (CheckBox a: answersCheckboxes){
            if (a.isChecked()){
                L.d("QuizAnswersAdapter.getCheckedAnswer() "+answersCheckboxes.indexOf(a));
                return answersCheckboxes.indexOf(a);
            }
        }
        return -1;
    }

    public void invalidateCheckboxes(){
        for (CheckBox c:answersCheckboxes){
            c.setClickable(false);
            c.setFocusable(false);
        }
    }


}
