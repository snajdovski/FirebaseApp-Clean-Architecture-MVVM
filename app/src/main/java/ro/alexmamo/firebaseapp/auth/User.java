package ro.alexmamo.firebaseapp.auth;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    public String uid;
    public String name;
    @SuppressWarnings("WeakerAccess")
    public String email;
    public String photoUrl;
    @ServerTimestamp
    public Date createdAt;
    @Exclude
    public boolean isAuthenticated;
    @Exclude
    boolean isNew, isCreated;

    public User() {}

    User(String uid, String name, String email, String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public String getName() {
        return name;
    }

    @Exclude
    public String getEmail() {
        return email;
    }
}