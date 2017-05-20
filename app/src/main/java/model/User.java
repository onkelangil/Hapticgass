package model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Anders on 20-05-2017.
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;


    public User() {

    }

    public User(String userName, String email) {
        username = userName;
        this.email = email;

    }

}
