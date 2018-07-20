package com.flipshope.app1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AccountActivityEdit extends AppCompatActivity {

    private EditText mUsernameEdit;
    private EditText mEmailEdit;
    private EditText mMobileNumberEdit;
    private Button mSaveButton;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private String id;
private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        mUsernameEdit = findViewById(R.id.usernameEditEdit);
        mEmailEdit = findViewById(R.id.emailEditEdit);
        mMobileNumberEdit = findViewById(R.id.mobileNoEditEdit);
        mSaveButton = findViewById(R.id.saveUpdated);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            mUsernameEdit.setText("");
            mEmailEdit.setText("");
            mMobileNumberEdit.setText("");
        } else {
            mUsernameEdit.setText(extras.getString("username"));
            mEmailEdit.setText(extras.getString("email"));
            mMobileNumberEdit.setText(extras.getString("mobile"));
            id = extras.getString("id");
            token = extras.getString("token");
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUsernameEdit.getText().toString();
                System.out.println("username111 = " + username);
                email = mEmailEdit.getText().toString();
                mobile = mMobileNumberEdit.getText().toString();

                sendRequest();
                // api call
            }
        });
    }


    private void sendRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.86.66:4000/api/users/update?id=" + id;

        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                SharedPreferences.Editor editor = getSharedPreferences("login_auth", MODE_PRIVATE).edit();
                editor.putString("name",username);
                editor.putString("email",email);
                editor.putString("mobile", mobile);
                editor.apply();

                Toast.makeText(getApplicationContext(), "Account details successfully updated!!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", username);
                params.put("email", email);
                params.put("number", mobile);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("x-access-token", token);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
