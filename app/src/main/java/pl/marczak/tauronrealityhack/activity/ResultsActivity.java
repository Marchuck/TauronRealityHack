package pl.marczak.tauronrealityhack.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import pl.marczak.tauronrealityhack.R;

public class ResultsActivity extends AppCompatActivity {

    ProgressBar aProgressBar;
    ProgressBar bProgressBar;
    ProgressBar cProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        aProgressBar = (ProgressBar) findViewById(R.id.progress1);
        bProgressBar = (ProgressBar) findViewById(R.id.progress2);
        cProgressBar = (ProgressBar) findViewById(R.id.progress3);

        setProgressBarLevel(aProgressBar, 20);
        setProgressBarLevel(bProgressBar,50);
        setProgressBarLevel(cProgressBar,80);
    }

    private void setProgressBarLevel(ProgressBar progressBar, int progress){
        progressBar.setProgress(progress);
    }

    private void fetchReslults(){
        //TODO add api method
    }
}
