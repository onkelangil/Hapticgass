package com.example.anders.hapticgass;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int LOGIN_RESULT_CODE = 1337;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if logged in
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            //user signed in
            writeUser();
        }
        else {
            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(AuthUI.FACEBOOK_PROVIDER).build()
                    , LOGIN_RESULT_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_RESULT_CODE){
            if (resultCode == RESULT_OK){
                //User logged in
                writeUser();
            }else {
                //Login failed
                Log.d(TAG, "Login failed");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
       // mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        /*
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }*/
    }

    public void writeUser(){
        Toast.makeText(this, auth.getCurrentUser().getEmail() + " " +
                auth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();

    }

}
