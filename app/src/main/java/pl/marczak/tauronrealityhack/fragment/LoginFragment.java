package pl.marczak.tauronrealityhack.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estimote.sdk.internal.utils.L;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.marczak.tauronrealityhack.Constants;
import pl.marczak.tauronrealityhack.LoginResultEvent;
import pl.marczak.tauronrealityhack.R;


public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.startFbLoginBtn)
    LoginButton startFbLoginBtn;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CallbackManager callbackManager;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        initFbLogin();


        return view;
    }

    private void initFbLogin() {
        startFbLoginBtn.setReadPermissions(Arrays.asList("email","user_friends","public_profile"));
        startFbLoginBtn.setFragment(this);

        callbackManager = CallbackManager.Factory.create();
        startFbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Hawk.put(Constants.FACEBOOK_LOGIN_RESULT,loginResult);
                Profile profile = Profile.getCurrentProfile();

                if (null != profile){

                L.d("LoginFragment.onSuccess: "+profile.getLinkUri());
                L.d("LoginFragment.onSuccess: "+profile.getId());
                L.d("LoginFragment.onSuccess: "+profile.getProfilePictureUri(300,300));
                }

                EventBus.getDefault().post(new LoginResultEvent(true));
            }

            @Override
            public void onCancel() {
                EventBus.getDefault().post(new LoginResultEvent(true));

            }

            @Override
            public void onError(FacebookException error) {
                EventBus.getDefault().post(new LoginResultEvent(true));

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null !=callbackManager){

            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.startFbLoginBtn)
    public void onClick() {
    }
}
