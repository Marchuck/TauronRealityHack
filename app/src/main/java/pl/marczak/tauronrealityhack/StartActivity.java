package pl.marczak.tauronrealityhack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.estimote.sdk.internal.utils.L;
import com.facebook.AccessToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity {

    private static final int SPLASH_FRAGMENT  = 0;
    private static final int LOGIN_FRAGMENT  = 1;
    private static final String TAG = "PING!";



    private int fragmentType  =  SPLASH_FRAGMENT;

    @Bind(R.id.startContainerFl)
    FrameLayout fragmentContainer;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);


        handler = new Handler();
        initActivity();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initLogin();


            }
        },500);

    }

    private void initLogin() {

        if (!isLoggedIn()){

            fragmentType = LOGIN_FRAGMENT;
            changeFragment();
        }else{

            onLoginSuccess();
        }
    }

    private boolean isLoggedIn(){
        AccessToken token = AccessToken.getCurrentAccessToken();

        L.d("StartActivity.isLoggedIn: "+String.valueOf(token!=null));
        return token != null;
    }

    private void initActivity() {
        getSupportFragmentManager().beginTransaction().add(R.id.startContainerFl, SplashFragment.newInstance()).commit();
    }


    private void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess: ");
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    private void changeFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.startContainerFl,getFragment()).commit();
    }

    private Fragment getFragment(){
        switch (fragmentType){
            case LOGIN_FRAGMENT:
                return LoginFragment.newInstance();
            case SPLASH_FRAGMENT:
                return SplashFragment.newInstance();

            default:
                return SplashFragment.newInstance();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Object event){
        if (event instanceof LoginResultEvent){
            if (((LoginResultEvent) event).isLoginSuccesful()){

                onLoginSuccess();
            }
        }
    }

}

