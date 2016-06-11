package pl.marczak.tauronrealityhack;

/**
 * Created by JOHANNES on 5/14/2016.
 */
public class LoginResultEvent {
    boolean isLoginSuccesful;

    public LoginResultEvent(boolean isLoginSuccesful) {
        this.isLoginSuccesful = isLoginSuccesful;
    }

    public boolean isLoginSuccesful() {
        return isLoginSuccesful;
    }
}
