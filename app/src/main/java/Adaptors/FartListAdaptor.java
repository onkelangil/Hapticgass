package Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.anders.hapticgass.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import model.Fart;

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

    //Updates listview on main activity
    @Override
    public View getView(int i, View listview, ViewGroup viewGroup) {
        if (listview == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            listview = inflater.inflate(R.layout.fart_list_item, null);
        }

        //sort the fartslist so seen farts are in bottom
        Collections.sort(fartList, new Comparator<Fart>() {
            @Override
            public int compare(Fart f1, Fart f2) {
                boolean b1 = f1.seen;
                boolean b2 = f2.seen;
                return Boolean.compare(b1,b2);

            }
        });

        //gets the fart list and update the listview
        if(fartList != null && fartList.size() > i) {
            f = fartList.get(i);
            final TextView username = (TextView) listview.findViewById(R.id.userNameTV);

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

            //Sets colors on the listview
            if (f.seen){
                listview.setBackgroundColor(0xffe4e4e4);
            }else listview.setBackgroundColor(0xff6eebac);

            //Play button
            ImageButton play = (ImageButton) listview.findViewById(R.id.imageButton);

            play.setOnClickListener(new View.OnClickListener() {
                final Fart fart = f;
                @Override
                public void onClick(View view) {
                    DatabaseReference seenFart;
                    if (!fart.seen){
                        seenFart = FirebaseDatabase.getInstance().getReference("farts");
                        seenFart.child(fart.id).child("seen").setValue(true);
                    }

                }
            });

            return listview;
        }
        return null;
    }
}
