package com.flipshope.app1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private String mUsername;
    private String mPassword;
    private String mUserID;
    private String mToken;
    private String mEmail;
    private String mMobile;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText mobile;
    private Button register;
    private String url;
    private RequestQueue mRequestQueue;
    Boolean usernameSet = true;
    Boolean emailSet = true;
    Boolean mobileSet = true;
    Boolean passwordSet = false;
    private String mobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        username = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobilenumber);
        password = findViewById(R.id.password_reg);
        Intent intent = getIntent();
        if (intent.hasExtra("userID") && intent.hasExtra("token") && intent.hasExtra("username")&& intent.hasExtra("email")) {
            mUsername = intent.getStringExtra("username");
            mUserID = intent.getStringExtra("userID");
            mToken = intent.getStringExtra("token");
            mEmail = intent.getStringExtra("email");
            username.setText(mUsername);
            email.setText(mEmail);
            password.setVisibility(View.GONE);
            password.setText("");
            passwordSet = true;
            email.setInputType(InputType.TYPE_NULL);
        }

        register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // from api


                if (username.getText().toString().equals("")){
                    username.setError("Please Enter Name");
                    usernameSet = false;
                }
                if (email.getText().toString().equals("")){
                    email.setError("Please Enter Email");
                    emailSet = false;
                }
                if (mobile.getText().toString().equals("")){
                    mobile.setError("Please Enter Mobile");
                    mobileSet = false;
                }
                if(!passwordSet || password.getText().toString().trim().length()<6){
                    Toast.makeText(RegisterActivity.this, "Password must of atleast 6 characters!!", Toast.LENGTH_LONG).show();
                }
                else if(!passwordSet && password.getText().toString().trim().length()>=6){
                    passwordSet = true;
                }
                if (usernameSet && emailSet && mobileSet && passwordSet){
                    mobileNumber = mobile.getText().toString().trim();
                    if(mobileNumber.length()<10 || mobileNumber.length()>13){
                        Toast.makeText(RegisterActivity.this, "Enter a valid mobile number!!", Toast.LENGTH_LONG).show();
                    }
                    else if(mobileNumber.length()==11 || mobileNumber.length()==12){
                        Toast.makeText(RegisterActivity.this, "Enter a valid mobile number!!", Toast.LENGTH_LONG).show();
                    }
                    else if(mobileNumber.length() == 13) {
                        mobileNumber = mobileNumber.substring(3);
                    }

                    final Pattern VALID_EMAIL_ADDRESS_REGEX =
                            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email.getText().toString().trim().toLowerCase());
                    if(matcher.find()) {
                        checkForUser(email.getText().toString().trim().toLowerCase());
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Enter a valid email address!!", Toast.LENGTH_SHORT).show();
                    }
//                    sendRequest();

                }

            }
        });

    }

    private void sendRequest() {
        url = "http://139.59.86.66:4000/api/auth/register";
        mRequestQueue = Volley.newRequestQueue(this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email.getText().toString().trim().toLowerCase());
        params.put("name", username.getText().toString().trim());
        params.put("password", password.getText().toString().trim());
        params.put("number", mobile.getText().toString().trim());
        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    System.out.println("response1222 = " + response.toString());
                    SharedPreferences.Editor editor = getSharedPreferences("login_auth", MODE_PRIVATE).edit();
                    editor.putBoolean("auth", true);
                    editor.putString("token", response.getString("token"));
                    editor.apply();

                    sendOTP(email.getText().toString(), mobile.getText().toString());

                    Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("number", mobile.getText().toString());
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        mRequestQueue.add(req);
    }

    private void sendOTP(String email, String mobile){
        url = "http://139.59.86.66:4000/api/auth/sendotp?email="+email+"&number="+mobile;
        mRequestQueue = Volley.newRequestQueue(this);
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("email", email);
//        params.put("number", mobile);
System.out.println("email + number = " + email + " " + mobile);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){

            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        mRequestQueue.add(mStringRequest);

    }

    private void checkForUser(String email){
        url = "http://139.59.86.66:4000/api/users/getuser?email=" + email;
        mRequestQueue = Volley.newRequestQueue(this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Toast.makeText(RegisterActivity.this, "Email already registered!!", Toast.LENGTH_LONG).show();
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println("error111 = " + error);
                checkForUserMobile(mobileNumber);
            }
        });
        mRequestQueue.add(mStringRequest);
    }
    private void checkForUserMobile(String mobile){
        url = "http://139.59.86.66:4000/api/users/getuser?number=" + mobile;
        mRequestQueue = Volley.newRequestQueue(this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Toast.makeText(RegisterActivity.this, "Number already registered!!", Toast.LENGTH_LONG).show();
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println("error111 = " + error);
                sendRequest();
            }
        });
        mRequestQueue.add(mStringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LoginManager.getInstance().logOut();
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
    }
}
