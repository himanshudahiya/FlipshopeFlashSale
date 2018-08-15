package com.flipshope.app1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class ChangePassword extends AppCompatActivity {

    private EditText oldPass;
    private EditText newPass;
    private EditText reNewPass;
    private Button submit;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        oldPass = findViewById(R.id.enterOldPass);
        newPass = findViewById(R.id.enterNewPass);
        reNewPass = findViewById(R.id.rEnterNewPass);
        submit = findViewById(R.id.resetPass);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPass.getText().toString().trim().equals(reNewPass.getText().toString().trim())) {
                    String newPasstrinned = newPass.getText().toString().trim();
                    if (newPasstrinned.length() < 6) {
                        Toast.makeText(ChangePassword.this, "Password should be of minimum 6 characters!!", Toast.LENGTH_LONG).show();
                    } else {
                        changePassword(email, oldPass.getText().toString().trim(), newPass.getText().toString().trim());
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "New Password and re-enter new password do not match!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void changePassword(String email, String oldPass, String newPass){
        String url = "http://139.59.86.66:4000/api/auth/changepassword";
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("oldpass", oldPass);
        params.put("newpass", newPass);

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ChangePassword.this, "Password successfully updated. Login with new password.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePassword.this, "Error occured during changing your password!! Try again later!!", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        mRequestQueue.add(req);

    }
}
