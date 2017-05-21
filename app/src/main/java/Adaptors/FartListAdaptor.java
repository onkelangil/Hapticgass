package Adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.anders.hapticgass.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Fart;
import model.User;

/**
 * Created by Anders on 20-05-2017.
 * This class is inspired by Kasper's demo on adaptors
 */

public class FartListAdaptor extends BaseAdapter {

    private ArrayList<Fart> fartList;
    private Context context;
    private Fart f;
    private DatabaseReference reference;
    private String senderUsername;
    private final static String TAG = "adaptor class";

    public FartListAdaptor(Context context, ArrayList<Fart> fartList){

        this.context = context;
        this.fartList = fartList;
        reference = FirebaseDatabase.getInstance().getReference("userlist");
    }

    @Override
    public int getCount() {

        if (fartList == null) {
            return 0;
        }
        else return fartList.size();
    }

    @Override
    public Fart getItem(int i) {
        if(fartList !=null && fartList.size() > i){
            return fartList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.fart_list_item, null);
        }

        //gets the fart list and
        if(fartList != null && fartList.size() > i) {
            f = fartList.get(i);
            final TextView username = (TextView) view.findViewById(R.id.userNameTV);

            //Put the sender name on the list view
            reference.child(f.sender).child("username").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    senderUsername = dataSnapshot.getValue() + "";
                    username.setText(senderUsername);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            if (f.seen){
                //username.setBackgroundColor();
            }
            final ImageButton play = (ImageButton) view.findViewById(R.id.imageButton);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(, "ID: " + view.getId(), Toast.LENGTH_SHORT).show();
                    Log.d("addaptor", "onClick: " + play.getId());
                    /*
                    if (isPlayed){

                        play.setImageResource(android.R.drawable.ic_media_pause);
                        isPlayed = false;
                    }else{
                        play.setImageResource(android.R.drawable.ic_media_play);
                        isPlayed = true;
                    }*/
                }
            });

            return view;
        }
        return null;
    }
}
