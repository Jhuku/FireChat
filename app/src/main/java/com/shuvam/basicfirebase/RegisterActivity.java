package com.shuvam.basicfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.shuvam.basicfirebase.model.User;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shuvam Ghosh on 8/6/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etUsernameRegister)
    EditText etUsername;
    @BindView(R.id.etPasswordRegister)
    EditText etPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    ArrayList<String> users;
    ProgressDialog pd;

    int flag = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Registering");
                pd.show();
                checkIfUserExists(etUsername.getText().toString());
            }
        });

    }

    private void init() {

        users = new ArrayList<>();
        pd = new ProgressDialog(this);
        Firebase.setAndroidContext(this);
    }

    private void checkIfUserExists(final String usname) {
        flag = 0;
        String url = "https://basicfirebase-e1506.firebaseio.com/Users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("null")){
                    Toast.makeText(RegisterActivity.this, "user not found", Toast.LENGTH_LONG).show();
                }
                else
                {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("User before inserting",obj.toString());
                        Iterator i = obj.keys();


                        while (i.hasNext()) {
                            users.add((String)i.next());
                        }

                        Log.d("size",""+users.size());
                        if(users.contains(usname))
                        {
                            Toast.makeText(RegisterActivity.this, "User already exits", Toast.LENGTH_SHORT).show();
                            pd.cancel();
                        }
                        else
                        {
                            pd.cancel();
                            Firebase reference = new Firebase("https://basicfirebase-e1506.firebaseio.com/Users");
                             reference.child(etUsername.getText().toString()).child("password").setValue(etPassword.getText().toString());
                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,Login.class));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(request);


    }
}
