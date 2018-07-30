package com.flipshope.app1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameView;
    private EditText mPasswordView;
    private String mUsername;
    private String mPassword;
    private Button mLogin;
    private Button mGoogleLogin;
    private LoginButton mFBLogin;
    private CallbackManager callbackManager;
    private String mEmail;
    private String gender;
    private URL profile_pic;
    private String mobile;
    private Button register_login;
    private String token;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url;
    private JSONObject jsonBody;
    private String accessToken;
    private String userID;
    private ProgressBar loaderLogin;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private static int SPLASH_TIME_OUT = 4000;
    private ImageView imageView;
    private ScrollView scrollView;
    private TextView forgotPassButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        startHeavyProcessing();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
//        checkPermissions();

        // to put value in shared preferences
//        SharedPreferences.Editor editor = getSharedPreferences("login_auth", MODE_PRIVATE).edit();
//        editor.putBoolean("auth", false);
//        editor.putString("token", null);
//        editor.apply();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        imageView = findViewById(R.id.splash);
        scrollView = findViewById(R.id.mainLayout);

        // to retrive data


        mUsernameView =  findViewById(R.id.log_username);
        mPasswordView = findViewById(R.id.log_password);
        loaderLogin = findViewById(R.id.loaderLogin);

        System.out.println("username = " + mUsername + "password = " + mPassword);
        mLogin = findViewById(R.id.loginButton);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click api call

                Boolean usernameSet = true;
                Boolean passwordSet = true;
                mUsername = mUsernameView.getText().toString();
                mPassword = mPasswordView.getText().toString();
                if (mUsername.equals("")){
                    mUsernameView.setError("Please Enter Email");
                    usernameSet = false;
                }
                if (mPassword.equals("")){
                    mPasswordView.setError("Please Enter Password");
                    passwordSet = false;
                }
                if (usernameSet && passwordSet) {
                    loaderLogin.setVisibility(View.VISIBLE);
                    sendRequest();
                }

            }
        });

        forgotPassButton = findViewById(R.id.forgot_pass_button);
        forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassMobile.class);
                startActivity(intent);
            }
        });

        mGoogleLogin = findViewById(R.id.googleSignIn);
        mGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click api call
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        mFBLogin = findViewById(R.id.fb_login);
        mFBLogin.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        mFBLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
//
                System.out.println("onSuccess");
                loaderLogin.setVisibility(View.VISIBLE);
                accessToken = loginResult.getAccessToken()
                        .getToken();
                userID = loginResult.getAccessToken().getUserId();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {@Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {

                            Log.i("LoginActivity",
                                    response.toString());
                            System.out.println(object);
                            try {
                                String id = object.getString("id");

                                mUsername = object.getString("name");
                                mEmail = object.getString("email");
                                checkForUser(mEmail);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
//                loaderLogin.setVisibility(View.GONE);

            }

            @Override
            public void onCancel() {
                loaderLogin.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Login attempt canceled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                loaderLogin.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Login attempt failed.", Toast.LENGTH_SHORT).show();
            }

        });

        Button mFBCustom = findViewById(R.id.fbCustom);
        mFBCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFBLogin.performClick();
            }
        });

        register_login = findViewById(R.id.register_login);
        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void startHeavyProcessing(){
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //some heavy processing resulting in a Data String

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }

                return "";
        }

        @Override
        protected void onPostExecute(String result) {
            SharedPreferences prefs = getSharedPreferences("login_auth", MODE_PRIVATE);
            Boolean auth = prefs.getBoolean("auth", false);
            if (!auth) {
                token = null;
                imageView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                getWindow().setBackgroundDrawableResource(R.color.white);

                LoginManager.getInstance().logOut();
                GoogleSignInClient mGoogleSignInClient;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                mGoogleSignInClient.signOut();
                getSupportActionBar().show();

            }
            else{
                token = prefs.getString("token", null);
                checkUserVerified(token);
            }

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void checkUserVerified(final String tokentoken){
        url = "http://139.59.86.66:4000/api/auth/me";
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject parentObject = new JSONObject(response);
                    if(parentObject.getBoolean("isNumVerified")){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        new DBHandler(getApplicationContext(), null, null, DBHandler.DATABASE_VERSION).onLogoutClearDB();
                        SharedPreferences prefs = getSharedPreferences("login_auth", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.apply();
                        LoginManager.getInstance().logOut();
                        GoogleSignInClient mGoogleSignInClient;
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build();
                        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                        mGoogleSignInClient.signOut();
                        imageView.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        getWindow().setBackgroundDrawableResource(R.color.white);

                        getSupportActionBar().show();
                    }
                    /* Here 'response' is a String containing the response you received from the website... */

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("response111 = " + response);
//

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println("error111 = " + error);
                new DBHandler(getApplicationContext(), null, null, DBHandler.DATABASE_VERSION).onLogoutClearDB();
                SharedPreferences prefs = getSharedPreferences("login_auth", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                LoginManager.getInstance().logOut();
                GoogleSignInClient mGoogleSignInClient;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                mGoogleSignInClient.signOut();
                imageView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                getWindow().setBackgroundDrawableResource(R.color.white);

                getSupportActionBar().show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-access-token", tokentoken);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void checkUserVerified2(final String tokentoken){
        url = "http://139.59.86.66:4000/api/auth/me";
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject parentObject = new JSONObject(response);
                    if(parentObject.getBoolean("isNumVerified")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        loaderLogin.setVisibility(View.GONE);
                        finish();
                    }
                    else{
                        sendOTP(parentObject.getString("email"), parentObject.getString("number"));

                    }
                    /* Here 'response' is a String containing the response you received from the website... */

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("response111 = " + response);
//

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println("error111 2= " + error);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-access-token", tokentoken);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void checkForUser(String email){
        url = "http://139.59.86.66:4000/api/users/getuser?email=" + email;
        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject parentObject1 = new JSONObject(response);
                    String parentObject = parentObject1.getString("token");
                    SharedPreferences.Editor editor = getSharedPreferences("login_auth", MODE_PRIVATE).edit();
                    editor.putBoolean("auth", true);
                    editor.putString("token", parentObject);
                    editor.apply();
                    checkUserVerified2(parentObject);


//                    navHeaderUsername.setText("Hello, " + parentObject.getString("name") + "!");
//                    SharedPreferences.Editor editor = getSharedPreferences("login_auth", MODE_PRIVATE).edit();
//                    editor.putString("name",parentObject.getString("name"));
//                    editor.putString("email",parentObject.getString("email"));
//                    editor.putString("id", parentObject.getString("_id"));
//                    editor.putString("mobile", parentObject.getString("number"));
//                    editor.apply();

                    /* Here 'response' is a String containing the response you received from the website... */

                } catch (JSONException e) {
                    e.printStackTrace();
                    loaderLogin.setVisibility(View.GONE);
                    LoginManager.getInstance().logOut();
                }

//

            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println("error111 = 1" + error);
                if(error.networkResponse.statusCode==404){
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("token", accessToken);
                    intent.putExtra("username", mUsername);
                    intent.putExtra("email", mEmail);
                    loaderLogin.setVisibility(View.GONE);
                    startActivity(intent);
                }

            }
        });
        mRequestQueue.add(mStringRequest);
    }

// post request
    private void sendRequest() {
        url = "http://139.59.86.66:4000/api/auth/login";
        loaderLogin.setVisibility(View.VISIBLE);
        mRequestQueue = Volley.newRequestQueue(this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", mUsername);
        params.put("password", mPassword);
        System.out.println("username = " + mUsername + "password = " + mPassword);

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    System.out.println("response1222 = " + response.toString());
                    SharedPreferences.Editor editor = getSharedPreferences("login_auth", MODE_PRIVATE).edit();
                    editor.putBoolean("auth", true);
                    editor.putString("token", response.getString("token"));
                    editor.putBoolean("isFirstLaunch", true);
                    editor.apply();
                    checkUserVerified2(response.getString("token"));

                    // VolleyLog.v("Response:%n %s", response.toString(4));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loaderLogin.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        mRequestQueue.add(req);
    }


    private void sendOTP(final String email, final String mobile){
        String url = "http://139.59.86.66:4000/api/auth/sendotp?email="+email+"&number="+mobile;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("email", email);
//        params.put("number", mobile);
        System.out.println("email + number = " + email + " " + mobile);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("number", mobile);
                startActivity(intent);
                loaderLogin.setVisibility(View.GONE);
                finish();
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            mUsername = account.getGivenName();
            mEmail = account.getEmail();
            accessToken = account.getIdToken();
            userID = account.getId();
            checkForUser(mEmail);
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("sign in failed", "signInResult:failed code=" + e.getStatusCode());

        }
    }


//    public boolean isLoggedIn() {
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        return accessToken != null;
//    }
//
//    private boolean checkPermissions() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            //do your check here
//            System.out.println("sdk 23");
//            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                //File write logic here
//                System.out.println("permission granted");
//
//                return true;
//
//            }
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            System.out.println("permission not granted");
//
//        }
//        System.out.println("permission granted sdk !23");
//
//        return true;
//        }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        loaderLogin.setVisibility(View.GONE);
//    }
}
