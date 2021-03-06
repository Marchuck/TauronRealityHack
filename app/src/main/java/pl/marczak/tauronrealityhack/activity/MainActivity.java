package pl.marczak.tauronrealityhack.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.robototextview.widget.RobotoTextView;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.facebook.AccessToken;
import com.facebook.FacebookServiceException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.marczak.tauronrealityhack.Constants;
import pl.marczak.tauronrealityhack.L;
import pl.marczak.tauronrealityhack.R;
import pl.marczak.tauronrealityhack.SectorDetectedEvent;
import pl.marczak.tauronrealityhack.fragment.QuizDialogFragment;
import pl.marczak.tauronrealityhack.model.QuizQuestion;
import pl.marczak.tauronrealityhack.model.User;
import pl.marczak.tauronrealityhack.model.UserData;
import pl.marczak.tauronrealityhack.monitoring.BleHelper;
import pl.marczak.tauronrealityhack.networking.ApiClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();


    @Bind(R.id.profile_image)
    ImageView profileImage;
    @Bind(R.id.profile_name)
    RobotoTextView profileName;
    @Bind(R.id.play_button)
    Button playButton;
    @Bind(R.id.play2_button)
    Button musicButton;
    @Bind(R.id.play3_button)
    Button resultsButton;

    @Bind(R.id.sector)
    TextView sector;


    String sectorName;
    private static final int UNAUTHORIZED = 190;

    BroadcastReceiver receiver;
    IntentFilter filter = new IntentFilter();
    BleHelper ble;
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Hawk.clear();
        ButterKnife.bind(this);

        beaconManager = new BeaconManager(getApplicationContext());
        ble = new BleHelper();
        ble.sectorCallback = new BleHelper.SectorCallback() {
            @Override
            public void onSectorChanged(final String major) {
                Log.e(TAG, "__________________________");
                Log.e(TAG, "onSectorChanged: " + major);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sector != null)
                            sectorName = major;
                            sector.setText("SEKTOR " + sectorName(major));
                    }
                });
            }
        };
        ble.start(beaconManager);

        fetchUserFriends();
        initUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ble.start(beaconManager);
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onPause() {
        ble.stop(beaconManager);
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        registerReceiver(receiver, filter);
        super.onDestroy();
    }

    public static String sectorName(String name) {
        if (name.contains("41504")) return "A";
        else if (name.contains("63534")) return "B";
        else   return "C";//38020
    }

    @Subscribe
    public void onEvent(final SectorDetectedEvent o) {
        Log.d(TAG, "onEvent: ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sector != null) sector.setText("SECTOR " + prettySector(o));
            }

            private String prettySector(SectorDetectedEvent event) {
                return event.major;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void initUser() {
        Profile profile = Profile.getCurrentProfile();
        User singleUser = new User();

        singleUser.setFirstName(profile.getFirstName());
        singleUser.setLastName(profile.getLastName());


        UserData user = new UserData();
        user.setUser(singleUser);
        user.setImageURL(profile.getProfilePictureUri(200, 200).toString());

        fillProfile(user);
    }


    private void fetchUserFriends() {

        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    FacebookServiceException exception = (FacebookServiceException) response.getError().getException();
                    if (exception.getRequestError().getErrorCode() == UNAUTHORIZED) {
                        logoutAndGotoLogin();
                    }
                    L.e("MainActivity.onCompleted: ", response.getError().getException());
                } else {
                    parseJSON(object);
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "friends");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void parseJSON(JSONObject object) {
        try {
            L.d("MainActivity.parseJSON all: " + object.toString());
            JSONObject json = object.getJSONObject("friends");
            L.d("MainActivity.parseJSON friends: " + json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void fillProfile(UserData data) {
        Picasso.with(this).load(data.getImageURL()).into(profileImage);
        profileName.setText(data.getUser().getFirstName() + " " + data.getUser().getLastName());
    }


    @OnClick({R.id.play_button, R.id.play2_button, R.id.play3_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_button: {
                List<QuizQuestion> question = Hawk.get(Constants.QUIZZES);
                if (null != question) {
                    int id = Hawk.get(Constants.QUIZ_ID, 0);
                    if (question.size() > id) {

                        QuizDialogFragment.newInstance().show(getSupportFragmentManager(), null);
                    } else {
                        Toast.makeText(this, "Koniec pytań", Toast.LENGTH_LONG).show();
                        int correctAnswers = Hawk.get(Constants.CORRECT_ANSWERS_COUNT, 0);
                        sendAnswers(sectorName,correctAnswers);
                    }
                }else{
                    fetchQuestions();
                }
                break;
            }
                case R.id.play2_button: {
                    openUrl(1);
                    break;
                }

                case R.id.play3_button: {
                    Intent intent = new Intent(this, ResultsActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }

    private void fetchQuestions() {
        ApiClient.getInstance(this).getQuestions(new Callback<List<QuizQuestion>>() {
            @Override
            public void success(List<QuizQuestion> quizQuestions, Response response) {

                for (QuizQuestion quizQuestion : quizQuestions) {
                    L.d("MainActivity.success: " + quizQuestion.toString());
                }
                Hawk.put(Constants.QUIZZES, quizQuestions);
                QuizDialogFragment.newInstance().show(getSupportFragmentManager(), null);

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void sendAnswers(String sector, int numberOfCorrect) {
        ApiClient.getInstance(this).sendAnswers(sector, numberOfCorrect, new Callback<List<QuizQuestion>>() {
            @Override
            public void success(List<QuizQuestion> quizQuestions, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    private void logoutAndGotoLogin() {

        LoginManager manager = LoginManager.getInstance();
        manager.logOut();
        startActivity(new Intent(MainActivity.this, StartActivity.class));
        finish();
    }

    private void openUrl(int sector) {
        switch (sector) {
            case 1: {
                String url = "https://main-66aaa.firebaseapp.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            }
            case 2: {
                String url = "https://project-8019276755424759634.firebaseapp.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            }
        }
    }
}
