package com.shuvam.basicfirebase.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuvam.basicfirebase.R;
import com.shuvam.basicfirebase.model.User;
import java.util.ArrayList;
/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {



    ArrayList<User> users;

    public MyAdapter(ArrayList<User> usrs){
        this.users= usrs;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder itemViewHolder, int position) {

        itemViewHolder.tvUser.setText(users.get(position).getUsername());
        //Here you can fill your row view
       // itemViewHolder.tvUser.setText(users.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUser;
        public ViewHolder(View itemView) {
            super(itemView);
            tvUser = (TextView)itemView.findViewById(R.id.tvUser);

            Typeface custom_font = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-Regular.ttf");
            tvUser.setTypeface(custom_font);
        }
    }
    private void setFont(TextView tvLogin, String s) {


    }
}
