package pl.marczak.tauronrealityhack.model;

import java.io.Serializable;

public class UserData implements Serializable {

    private User user;
    private String imageURL;
    private String externalToken;

    public UserData(User user, String imageURL, String externalToken) {
        this.user = user;
        this.imageURL = imageURL;
        this.externalToken = externalToken;
    }

    public UserData(User user, String imageURL) {
        this.user = user;
        this.imageURL = imageURL;
    }

    public UserData() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getExternalToken() {
        return externalToken;
    }

    public void setExternalToken(String externalToken) {
        this.externalToken = externalToken;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
