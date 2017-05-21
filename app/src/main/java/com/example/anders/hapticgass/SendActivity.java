package com.example.anders.hapticgass;

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

import java.util.ArrayList;

import Adaptors.FriendListAdaptor;
import model.User;

public class SendActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

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
        reference = database.getReference("userlist");

        friends = new ArrayList<>();
        getUserList();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<User> sendTo = new ArrayList<>();

                if(adaptor != null) {
                    sendTo = adaptor.getCheckedFriends();
                }

                for (int i = 0; i < sendTo.size(); i++) {

                    sendFart(auth.getCurrentUser().getUid() + "", sendTo.get(i).uid);
                }

                //Ends activity
                finish();
            }
        });
    }

    //Send a fart to the relevant friends
    private void sendFart(String sID, String rID) {
        DatabaseReference referenceFart = database.getReference("farts");

        //Get a unique key for the db
        String key = referenceFart.push().getKey();

        //Insert sender
        referenceFart.child(key).child("sender").setValue(sID);
        referenceFart.child(key).child("receiver").setValue(rID);
        referenceFart.child(key).child("seen").setValue(false);
    }

    public void getUserList() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);

                DatabaseReference ref = dataSnapshot.getRef();

                user.uid = ref.getKey();
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
