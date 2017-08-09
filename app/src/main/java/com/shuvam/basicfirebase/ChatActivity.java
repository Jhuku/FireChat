package com.shuvam.basicfirebase;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.shuvam.basicfirebase.model.UserDetails;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shuvam Ghosh on 8/6/2017.
 */

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.sendButton)
    ImageView btnSend;
    @BindView(R.id.messageArea)
    EditText messageArea;
    @BindView(R.id.scrollView)ScrollView scrollView;
    @BindView(R.id.layout1)LinearLayout layout;
    @BindView(R.id.linearLayout)LinearLayout ll;

    float dX;
    float dY;
    int lastAction;
    Firebase reference1, reference2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        //Log.d("Details","User="+UserDetails.username+" chat with "+UserDetails.chatWith);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://basicfirebase-e1506.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://basicfirebase-e1506.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                }
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = ll.getX() - event.getRawX();
                        dY = ll.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        ll.setY(event.getRawY() + dY);
                       // ll.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            Toast.makeText(ChatActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });

    }


    public void addMessageBox(String message, int type){
        TextView textView = new TextView(this);
        textView.setText(message);
//        scrollView.fullScroll(View.FOCUS_DOWN);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 2.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setTextColor(Color.WHITE);
            textView.setPadding(16,16,16,16);
            textView.setBackgroundResource(R.drawable.roundrectchat);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.roundrectchat);
        }
        textView.setLayoutParams(lp2);
        textView.setPadding(32,32,32,32);
        textView.setTextColor(Color.WHITE);
        textView.setVisibility(View.INVISIBLE);

        layout.addView(textView);
        textView.setVisibility(View.VISIBLE);
        Animation an = AnimationUtils.loadAnimation(this,R.anim.reveal);
        an.setInterpolator(new OvershootInterpolator());
        textView.setAnimation(an);
        an.start();
        // scrollView.fullScroll(View.FOCUS_DOWN);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}
