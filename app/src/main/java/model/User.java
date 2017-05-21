package model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Anders on 20-05-2017.
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String uid;

    public User() {

    }

    public User(String userName, String email, String uid) {
        username = userName;
        this.email = email;
        this.uid = uid;

    }
}
