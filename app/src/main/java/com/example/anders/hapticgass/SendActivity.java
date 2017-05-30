package com.example.anders.hapticgass;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Adaptors.FriendListAdaptor;
import model.Fart;
import model.User;

public class SendActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ListView friendList;
    private Button btnSend;

    private ArrayList<User> friends;
    private FriendListAdaptor adaptor;

    private static final String BROADCAST_RESULT = "broadcast_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        //Define layout elements
        friendList = (ListView) findViewById(R.id.friendSend);
        btnSend = (Button) findViewById(R.id.btnSend);

        database = FirebaseDatabase.getInstance();
        //Check if logged in
        auth = FirebaseAuth.getInstance();
        reference = database.getReference("userlist");

        friends = new ArrayList<>();
        getUserList();

        //Send button listener
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<User> sendTo = new ArrayList<>();

                if(adaptor != null) {
                    sendTo = adaptor.getCheckedFriends();
                }
                if (sendTo.size() > 0) {
                    //Send to every friend checked off the list
                    for (int i = 0; i < sendTo.size(); i++) {
                        sendFart(auth.getCurrentUser().getUid() + "", sendTo.get(i).uid);
                    }
                    broadcastResult();
                    //Ends activity
                    finish();
                } else {
                    Toast.makeText(SendActivity.this, "No friends are checked off", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Send a fart to the relevant friends
    private void sendFart(String sID, String rID) {
        DatabaseReference referenceFart = database.getReference("farts");

        //Get a unique key for the db
        String key = referenceFart.push().getKey();

        //Insert sender
        Fart f = new Fart();
        f.receiver = rID;
        f.sender = sID;
        f.seen = false;

        referenceFart.child(key).setValue(f);

        //referenceFart.child(key).child("sender").setValue(sID);
        //referenceFart.child(key).child("receiver").setValue(rID);
        //referenceFart.child(key).child("seen").setValue(false);
    }

    //Retrieves the user list from the database
    public void getUserList() {
        //Creates a listener on the user list table
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //If a user is added save the user
                User user = dataSnapshot.getValue(User.class);

                //Get the unique key of the user
                DatabaseReference ref = dataSnapshot.getRef();
                user.uid = ref.getKey();

                //Add user to list and update the UI with updated adaptor
                friends.add(user);
                adaptor = new FriendListAdaptor(SendActivity.this, friends);
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
    private void broadcastResult(){
        Intent broadcastIntent = new Intent(BROADCAST_RESULT);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

    }

    //Saves data to onCreate if the process is killed and restarted.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.putString("name", name);

        super.onSaveInstanceState(savedInstanceState);
    }

    //Restores data from savedInstanceState
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
