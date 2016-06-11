package pl.marczak.tauronrealityhack.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.facebook.AccessToken;
import com.facebook.FacebookServiceException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.marczak.tauronrealityhack.App;
import pl.marczak.tauronrealityhack.L;
import pl.marczak.tauronrealityhack.R;
import pl.marczak.tauronrealityhack.SectorDetectedEvent;
import pl.marczak.tauronrealityhack.fragment.QuizDialogFragment;
import pl.marczak.tauronrealityhack.model.QuizQuestion;
import pl.marczak.tauronrealityhack.model.User;
import pl.marczak.tauronrealityhack.model.UserData;
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
    @Bind(R.id.sector)
    TextView sector;

    private static final int UNAUTHORIZED = 190;

    BroadcastReceiver receiver;
    IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //initUser();
        fetchUserFriends();
//        filter.addAction("MAJOR");
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //do something based on the intent's action
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        sector.setText(App.getInstance(MainActivity.this).lastMajor);
//                    }
//                });
//            }
//        };
//        registerReceiver(receiver, filter);

    }

    @Override
    protected void onResume() {
        App.getInstance(this).startScan();
        super.onResume();
    }

    @Override
    protected void onPause() {
        App.getInstance(this).stopScan();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        registerReceiver(receiver, filter);
        super.onDestroy();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sector.setText(App.getInstance(MainActivity.this).lastMajor);
                    }
                });
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


    @OnClick(R.id.play_button)
    public void onClick() {
        QuizDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }

    private void fetchQuestions(){
        ApiClient.getInstance(this).getQuestions(new Callback<List<QuizQuestion>>() {
            @Override
            public void success(List<QuizQuestion> quizQuestions, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void sendAnswers(String sector, int numberOfCorrect){
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

    private void openUrl(int sector){
        switch(sector){
            case 1:{
                String url = "https://main-66aaa.firebaseapp.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            }
            case 2:{
                String url = "https://project-8019276755424759634.firebaseapp.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            }
        }
    }
}
