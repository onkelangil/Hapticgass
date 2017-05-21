package Adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.anders.hapticgass.R;

import java.util.ArrayList;

import model.User;

/**
 * Created by Anders on 20-05-2017.
 * This class is inspired by Kasper's demo on adaptors
 */

public class FriendListAdaptor extends BaseAdapter {

    private ArrayList<User> friendList;
    private Context context;
    private User u;

    public FriendListAdaptor(Context context, ArrayList<User> friendList){

        this.context = context;
        this.friendList = friendList;
    }

    @Override
    public int getCount() {

        if (friendList  == null) {
            return 0;
        }
        else return friendList.size();
    }

    @Override
    public User getItem(int i) {
        if(friendList!=null && friendList.size() > i){
            return friendList.get(i);
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
            view = inflater.inflate(R.layout.friend_list_item, null);
        }

        if(friendList != null && friendList.size() > i) {
            u = friendList.get(i);

            TextView username = (TextView) view.findViewById(R.id.userNameTV);
            username.setText(u.username);

            CheckBox check = (CheckBox) view.findViewById(R.id.checkBox);

            check.setOnClickListener(new View.OnClickListener() {
                final User user = u;
                @Override
                public void onClick(View v) {

                    Log.i("Adaptor", "" + user.username);
                }
            });

            return view;
        }
        return null;
    }
}
