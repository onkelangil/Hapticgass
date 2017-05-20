package com.example.anders.hapticgass;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;


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
                            .setProviders(AuthUI.FACEBOOK_PROVIDER).build(),
                    LOGIN_RESULT_CODE);
        }
    }

    //Create menu bar to Log out
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Check if item on menu bar is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.logout:
            auth.signOut();
            finish();
            startActivity(getIntent());
            return(true);

    }
        return(super.onOptionsItemSelected(item));
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

    public void writeUser(){
        Toast.makeText(this, auth.getCurrentUser().getEmail() + " " +
                auth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();

    }

}
