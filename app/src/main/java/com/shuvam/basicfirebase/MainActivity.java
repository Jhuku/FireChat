package com.shuvam.basicfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shuvam.basicfirebase.model.User;
import com.shuvam.basicfirebase.model.UserDetails;
import com.shuvam.basicfirebase.utils.MyAdapter;
import com.shuvam.basicfirebase.utils.RecyclerItemClickListener;
import com.shuvam.basicfirebase.utils.SharedPrefsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.recView)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager lm;

    MyAdapter adapter;
    ArrayList<User> users;

    SharedPrefsUtils spu;
    ProgressDialog pd;
    HashMap<String, Integer> hm;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        setinit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                spu.setBooleanPreference(MainActivity.this,"LoggedIn",false);
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setinit() {

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        UserDetails.chatWith = users.get(position).getUsername();
                        UserDetails.username = spu.getStringPreference(MainActivity.this,"Username");
                        UserDetails.password = spu.getStringPreference(MainActivity.this,"Password");
                        Intent i = new Intent(MainActivity.this,ChatActivity.class);
                        startActivity(i);
                    }
                })
        );
    }

    private void init() {
        realm = Realm.getDefaultInstance();
        pd = new ProgressDialog(this);
        lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        users = new ArrayList<>();
        populateRecView();
        pd.setMessage("Loading");
        pd.show();
        hm = new HashMap<>();
        seeMyMessages();

    }

    private void seeMyMessages() {

        String url = "https://basicfirebase-e1506.firebaseio.com/messages.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("null")){
                    Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                }
                else
                {
                    int count = 0;
                    int c =0;
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("User Messages",obj.toString());
                        Iterator i = obj.keys();

                        while (i.hasNext()) {
                            c=0;
                            String[] msgdet = i.next().toString().split("_");
                            if(msgdet[0].equals(spu.getStringPreference(MainActivity.this,"Username")))
                            {
                                JSONObject innerJObject = obj.getJSONObject(msgdet[0]+"_"+msgdet[1]);
                                Iterator x = innerJObject.keys();
                                while (x.hasNext()) {
                                    count++;
                                    String innerKkey = x.next().toString();
                                    JSONObject inner2JObject = innerJObject.getJSONObject(innerKkey);
                                    Log.d("message_got", inner2JObject.getString("message"));
                                    if(!inner2JObject.get("user").equals(spu.getStringPreference(MainActivity.this,"Username")))
                                    {
                                        hm.put(inner2JObject.get("user").toString(),++c);
                                    }

                                }
                            }
                           // User u = new User(i.next().toString());
                            //if(!u.getUsername().equals(spu.getStringPreference(MainActivity.this,"Username")))
                               // users.add(u);
                        }
                        Log.d("Number of messages",""+count);
                        for (String name: hm.keySet()){
                            String value = hm.get(name).toString();
                            System.out.println(name + " " + value);
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

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
        setRealmObject();
    }

    private void setRealmObject() {


    }

    private void populateRecView() {


        String url = "https://basicfirebase-e1506.firebaseio.com/Users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("null")){
                    Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                }
                else
                {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("User ",obj.toString());
                        Iterator i = obj.keys();

                        while (i.hasNext()) {

                            User u = new User(i.next().toString());
                            if(!u.getUsername().equals(spu.getStringPreference(MainActivity.this,"Username")))
                            users.add(u);
                        }
                        pd.cancel();
                        adapter = new MyAdapter(users);
                        recyclerView.setAdapter(adapter);

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

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
