package com.example.anders.hapticgass;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import Adaptors.FartListAdaptor;
import model.User;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int LOGIN_RESULT_CODE = 1001;
    private static final int SEND_RESULT_CODE = 1002;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseUser currentUser;

    private ListView friendList;
    private Button btnSend;

    private ArrayList<User> friends;
    private FartListAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        friendList = (ListView) findViewById(R.id.friendsListView);
        btnSend = (Button) findViewById(R.id.button);

        //Set button click listener for starting SendActivity
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start new explicit activity
                Intent intentSend = new Intent(MainActivity.this, SendActivity.class);
                startActivityForResult(intentSend, SEND_RESULT_CODE);
            }
        });

        database = FirebaseDatabase.getInstance();
        //Check if logged in
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            //user signed in
            currentUser = auth.getCurrentUser();
            startNotificationService();
        }
        else {
            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(AuthUI.FACEBOOK_PROVIDER).build(),
                    LOGIN_RESULT_CODE);
        }
        friends = new ArrayList<>();
        getUserList();
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
                //User signed in through FB
                currentUser = auth.getCurrentUser();

                createNewUser();

                startNotificationService();
            }else {
                //Login failed
                Log.d(TAG, "Login failed");
            }
        }
    }

    //Start listening for notifications in background service
    public void startNotificationService(){
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);

    }
    private void createNewUser() {
        reference = database.getReference("userlist");

        //Checks if user does not exist in the database
        reference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    User newUser = new User(currentUser.getDisplayName(),
                            currentUser.getEmail());
                    reference.child(auth.getCurrentUser().getUid()).setValue(newUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Get the list of users and add them to you friend view
    public void getUserList() {
        reference = database.getReference("userlist");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                friends.add(user);
                adaptor = new FartListAdaptor(MainActivity.this, friends);
                friendList.setAdapter(adaptor);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
