package com.flipshope.app1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    Context mContext;
    private FragmentManager fragmentManager;
    private String token;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url;
    private TextView navHeaderUsername;
    Boolean isFirstLaunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startService(new Intent(this, BackgroundService.class));
        SharedPreferences prefs = getSharedPreferences("login_auth", MODE_PRIVATE);
        Boolean auth = prefs.getBoolean("auth", false);
        isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);
        if (!auth) {
            token = null;
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new HomeFragment())
                .commit();
        setTitle("Home");
        setTitleColor(R.color.com_facebook_button_text_color);
        navHeaderUsername = header.findViewById(R.id.login_user_name);

        token = prefs.getString("token", null);
//      token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjViMGNmNTE3YTUxZDVjMDlkYWZlNWNjMCIsImlhdCI6MTUyNzU3NTgzMSwiZXhwIjoxNTI3NjYyMjMxfQ.NVF4DW4Rs5apg33houKJMKcApzXFpLkhtNphZndbqaI";

        sendRequest(this);


    }
//get request
    public void sendRequest(final Context context) {
        url = "http://139.59.86.66:4000/api/auth/me";
        mRequestQueue = Volley.newRequestQueue(context);
        SharedPreferences prefs = context.getSharedPreferences("login_auth", MODE_PRIVATE);
        token = prefs.getString("token", null);

        mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject parentObject = new JSONObject(response);
                    navHeaderUsername.setText("Hello, " + parentObject.getString("name") + "!");
                    SharedPreferences.Editor editor = getSharedPreferences("login_auth", MODE_PRIVATE).edit();
                    editor.putString("name",parentObject.getString("name"));
                    editor.putString("email",parentObject.getString("email"));
                    editor.putString("id", parentObject.getString("_id"));
                    editor.putString("mobile", parentObject.getString("number"));
                    editor.putBoolean("isEmailVerified", parentObject.getBoolean("isVerified"));
                    editor.putBoolean("isNumVerified", parentObject.getBoolean("isNumVerified"));
                    editor.putBoolean("isSite", parentObject.getBoolean("isSite"));
                    editor.apply();
                    System.out.println("response = " + response);
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
                logout(getApplicationContext());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-access-token", token);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.account) {
            // Handle the camera action
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,new AccountFragment())
                    .commit();
            setTitle("Your Account");
            setTitleColor(R.color.white);


        } else if (id == R.id.about) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AboutFragment())
                    .commit();
            setTitle("About Us");
            setTitleColor(R.color.white);

        }
        else if (id == R.id.howTOBuy) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new HowToBuyFragment())
                    .commit();
            setTitle("How To Buy");
            setTitleColor(R.color.white);
        }
        else if (id == R.id.howTOUse) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new HowToUseFragment())
                    .commit();
            setTitle("How To Use");
            setTitleColor(R.color.white);
        }
        else if (id == R.id.faq) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new FAQFragment())
                    .commit();
            setTitle("FAQ");
            setTitleColor(R.color.white);
        }
        else if (id == R.id.logout) {


            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            logout(mContext);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
            String titleText = "Logout!";

            // Initialize a new spannable string builder instance
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

            // Apply the text color span
            ssBuilder.setSpan(
                    foregroundColorSpan,
                    0,
                    titleText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            // Set the alert dialog title using spannable string builder
            dialog.setTitle(ssBuilder);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

        } else if (id == R.id.home) {
            // Handle the camera action
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,new HomeFragment())
                    .commit();
            setTitleColor(R.color.com_facebook_button_text_color);
            setTitle("Home");


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(Context context){
        new DBHandler(getApplicationContext(), null, null, DBHandler.DATABASE_VERSION).onLogoutClearDB();
        final Intent intent = new Intent(context, LoginActivity.class);
        SharedPreferences prefs = getSharedPreferences("login_auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        LoginManager.getInstance().logOut();
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // ...
//                        startActivity(intent);
//                        finish(); // finish activity
//                    }
//                });
        startActivity(intent);
        finish(); // finish activity
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.home) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new HomeFragment())
                    .commit();
            setTitle("Home");
            setTitleColor(R.color.com_facebook_button_text_color);

            NavigationView navigationView =  findViewById(R.id.nav_view);
            int size = navigationView.getMenu().size();
            for (int i = 0; i < size; i++) {
                navigationView.getMenu().getItem(i).setChecked(false);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (exit) {
            this.moveTaskToBack(true);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

}
