package pl.marczak.tauronrealityhack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.facebook.AccessToken;
import com.facebook.FacebookServiceException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.marczak.tauronrealityhack.L;
import pl.marczak.tauronrealityhack.R;
import pl.marczak.tauronrealityhack.fragment.QuizDialogFragment;
import pl.marczak.tauronrealityhack.model.User;
import pl.marczak.tauronrealityhack.model.UserData;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.profile_image)
    ImageView profileImage;
    @Bind(R.id.profile_name)
    RobotoTextView profileName;
    @Bind(R.id.play_button)
    Button playButton;

    private static final int UNAUTHORIZED = 190;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUser();
        fetchUserFriends();

    }

    private void initUser() {
        Profile profile = Profile.getCurrentProfile();

        User singleUser = new User();
        singleUser.setFirstName(profile.getFirstName());
        singleUser.setLastName(profile.getLastName());


        UserData user = new UserData();
        user.setUser(singleUser);
        user.setImageURL(profile.getProfilePictureUri(200,200).toString());

        fillProfile(user);
    }


    private void fetchUserFriends() {

        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError()!=null){
                    FacebookServiceException exception = (FacebookServiceException) response.getError().getException();
                    if (exception.getRequestError().getErrorCode()==UNAUTHORIZED){
                        logoutAndGotoLogin();
                    }
                    L.e("MainActivity.onCompleted: ", response.getError().getException());
                }else{
                    parseJSON(object);
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","friends");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void parseJSON(JSONObject object) {
        try {
            L.d("MainActivity.parseJSON all: "+object.toString());
            JSONObject json = object.getJSONObject("friends");
            L.d("MainActivity.parseJSON friends: "+json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void fillProfile(UserData data){
        Picasso.with(this).load(data.getImageURL()).into(profileImage);
        profileName.setText(data.getUser().getFirstName()+" "+data.getUser().getLastName());
    }


    @OnClick(R.id.play_button)
    public void onClick() {
        QuizDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }


    private void logoutAndGotoLogin() {

        LoginManager manager = LoginManager.getInstance();
        manager.logOut();
        startActivity(new Intent(MainActivity.this, StartActivity.class));
        finish();
    }
}
