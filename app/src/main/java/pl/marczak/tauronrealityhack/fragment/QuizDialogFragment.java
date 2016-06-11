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

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.marczak.tauronrealityhack.Constants;
import pl.marczak.tauronrealityhack.L;
import pl.marczak.tauronrealityhack.R;
import pl.marczak.tauronrealityhack.adapter.QuizAnswersAdapter;
import pl.marczak.tauronrealityhack.model.AnswerShortResponse;
import pl.marczak.tauronrealityhack.model.QuizResponse;


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
    QuizResponse data;
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
        Dialog alertDialog = new Dialog(getActivity()) {
            @Override
            public void onBackPressed() {

                Toast.makeText(getContext(), "Wybierz odpowiedź", Toast.LENGTH_LONG).show();
            }
        };


        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(root);

        return alertDialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = Hawk.get(Constants.QUIZ);
        L.d("QuizDialogFragment.onActivityCreated() " + data.toString());

        shuffle(data);


        handler = new Handler();
        if (null != data) {
            mTvQuizDialogHeader.setText(data.getHeader());
            mTvQuizDialogQuestionPrimary.setText(data.getQuestion());
            adapter = new QuizAnswersAdapter(getActivity(), data);
            mLvQuizDialogAnswerOptions.setAdapter(adapter);
        } else {
            dismiss();
        }

    }

    private void shuffle(QuizResponse data) {

        List<AnswerShortResponse> answersList = data.getAnswers();

        Collections.shuffle(answersList, new Random(System.nanoTime()));

        data.setAnswers(answersList);

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
                        putAnswer();
                        adapter.invalidateCheckboxes();
                        isClicked = true;
                        Toast.makeText(getContext(), "Wysyłam odpowiedź", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Wybierz odpowiedź", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    private void putAnswer() {


    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }





}
