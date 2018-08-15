package com.flipshope.app1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewPassEnter extends AppCompatActivity {

    private EditText enterPass;
    private EditText rEnterPass;
    private Button submit;
    private String id;
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass_enter);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        number = intent.getStringExtra("number");
        enterPass = findViewById(R.id.enterNewPass);
        rEnterPass = findViewById(R.id.rEnterNewPass);
        submit = findViewById(R.id.resetPass);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((enterPass.getText().toString().trim()).equals(rEnterPass.getText().toString().trim())){
                    if(enterPass.getText().toString().trim().length()<6){
                        Toast.makeText(NewPassEnter.this, "Password should be of minimum 6 characters!!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        resetPassword(id, enterPass.getText().toString().trim(), number);
                    }
                }
                else{
                    Toast.makeText(NewPassEnter.this, "Password and Re-Enter password do not match!!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void resetPassword(String id, String newPassword, String number){
        String url = "http://139.59.86.66:4000/api/auth/resetpassword";
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", id);
        params.put("pass", newPassword);
        params.put("number", number);

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(NewPassEnter.this, "Password successfully updated. Login with new password.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewPassEnter.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPassEnter.this, "Error occured during changing your password!! Try again later!!", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        mRequestQueue.add(req);

    }
}
