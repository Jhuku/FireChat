package com.shuvam.basicfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shuvam.basicfirebase.utils.ServerHelper;
import com.shuvam.basicfirebase.utils.SharedPrefsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Shuvam Ghosh on 8/5/2017.
 */

public class Login extends AppCompatActivity {


    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.tvRegister)
    TextView tvRegister;

    TextView tvLogin;
    ProgressDialog pd;
    SharedPrefsUtils spu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();

        if(spu.getBooleanPreference(getApplicationContext(),"LoggedIn",false)==true)
        {
            Intent in = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(in);
            finish();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkUserExists(etUsername.getText().toString(),etPassword.getText().toString());
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Login.this,RegisterActivity.class);
                startActivity(in);
            }
        });
    }

    private void checkUserExists(final String username, final String password) {

        new ServerHelper(this).serverGetRequest("https://basicfirebase-e1506.firebaseio.com/Users.json","Logging in...", new ServerHelper.ServerCallback() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("null")){
                    Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                }
                else
                {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if(!obj.has(username)){
                            Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                        }
                        else if(obj.getJSONObject(username).getString("password").equals(password)){

                            spu.setBooleanPreference(Login.this,"LoggedIn",true);
                            spu.setStringPreference(Login.this,"Username",username);
                            spu.setStringPreference(Login.this,"Password",password);
                            startActivity(new Intent(Login.this,MainActivity.class));
                        }
                        else {
                            Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.cancel();
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void init() {

        tvLogin = (TextView)findViewById(R.id.tvLogin);
        setFont(tvLogin, "fonts/Montserrat-Regular.ttf");
        pd = new ProgressDialog(this);
        spu = new SharedPrefsUtils();
    }


    private void setFont(TextView tvLogin, String s) {

        Typeface custom_font = Typeface.createFromAsset(getAssets(),s);
        tvLogin.setTypeface(custom_font);
    }
}