package com.example.anders.hapticgass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Adaptors.FriendListAdaptor;
import model.User;

public class SendActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseUser currentUser;

    private ListView friendList;
    private Button btnSend;

    private ArrayList<User> friends;
    private FriendListAdaptor adaptor;

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

        friends = new ArrayList<>();
        getUserList();
    }

    public void getUserList() {
        reference = database.getReference("userlist");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
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
}
