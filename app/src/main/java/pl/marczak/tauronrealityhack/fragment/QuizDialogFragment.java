package pl.marczak.tauronrealityhack.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linearlistview.LinearListView;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.marczak.tauronrealityhack.Constants;
import pl.marczak.tauronrealityhack.R;
import pl.marczak.tauronrealityhack.adapter.QuizAnswersAdapter;
import pl.marczak.tauronrealityhack.model.QuizQuestion;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizDialogFragment extends CustomDialog {




    @Bind(R.id.tvQuizDialogHeader)
    TextView mTvQuizDialogHeader;
    @Bind(R.id.tvQuizDialogQuestionPrimary)
    TextView mTvQuizDialogQuestionPrimary;
    @Bind(R.id.llQuizDialogQuestionContainer)
    LinearLayout mLlQuizDialogQuestionContainer;
    @Bind(R.id.slQuizDialog)
    ScrollView mSlQuizDialog;
    @Bind(R.id.lvQuizDialogAnswerOptions)
    LinearListView mLvQuizDialogAnswerOptions;

    @Bind(R.id.tvQuizAnswerReaction)
    TextView tvQuizAnswerReaction;

    Handler handler;
    List<QuizQuestion> data;
    QuizQuestion currentQuestrion;

    QuizAnswersAdapter adapter;

    private boolean isClicked = false;

    public QuizDialogFragment() {
        // Required empty public constructor
    }



    public static QuizDialogFragment newInstance() {
        return new QuizDialogFragment();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_quiz_dialog, null);
        ButterKnife.bind(this, root);
        Dialog alertDialog = new Dialog(getActivity());


        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(root);

        return alertDialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int id = Hawk.get(Constants.QUIZ_ID,0);


        data = Hawk.get(Constants.QUIZZES);

        if (data.size()>id){
            currentQuestrion = data.get(id);

            id+=1;
            Hawk.put(Constants.QUIZ_ID,id);



            handler = new Handler();
            if (null != currentQuestrion) {
                mTvQuizDialogQuestionPrimary.setText(currentQuestrion.getQuestion());
                adapter = new QuizAnswersAdapter(getActivity(), currentQuestrion);
                mLvQuizDialogAnswerOptions.setAdapter(adapter);
            } else {

            }

        }else{
            Toast.makeText(getActivity(),"Koniec pytań",Toast.LENGTH_LONG).show();
            dismiss();
        }

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rlQuizExitLayout, R.id.tvQuizDialogSendAnswer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlQuizExitLayout:

                dismiss();
                break;
            case R.id.tvQuizDialogSendAnswer:
                if (!isClicked) {
                    if (null != adapter && adapter.getCheckedAnswerId() != -1) {
                        int id = adapter.getCheckedAnswerId();
                        if (currentQuestrion.getAnswers().get(id).isCorrect()){
                            chngeCorrectAnswersNumber(1);
                            Toast.makeText(getContext(), "Prawidłowa odpowiedź", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else{
                            Toast.makeText(getContext(), "Błędna odpowiedź", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                        adapter.invalidateCheckboxes();
                        isClicked = true;



                    } else {
                        Toast.makeText(getContext(), "Wybierz odpowiedź", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    private void chngeCorrectAnswersNumber(int increment){
        int coorrectCount = Hawk.get(Constants.CORRECT_ANSWERS_COUNT,0);
        coorrectCount+=increment;

        coorrectCount = coorrectCount>=0?coorrectCount:0;

        Hawk.put(Constants.CORRECT_ANSWERS_COUNT,coorrectCount);

    }









}
