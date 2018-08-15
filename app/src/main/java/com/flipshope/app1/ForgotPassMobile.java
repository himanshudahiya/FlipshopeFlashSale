package com.flipshope.app1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ForgotPassMobile extends AppCompatActivity {

    private EditText mobileNoRes;
    private Button sendOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_mobile);
        mobileNoRes = findViewById(R.id.mobileNoPass);
        sendOTP = findViewById(R.id.sendOTPResButton);
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mobileNoRes.getText().toString().trim();
                if(number.length()<10 || number.length()>13){
                    Toast.makeText(ForgotPassMobile.this, "Enter a valid mobile number!!", Toast.LENGTH_LONG).show();
                }
                else if(number.length()==11 || number.length()==12){
                    Toast.makeText(ForgotPassMobile.this, "Enter a valid mobile number!!", Toast.LENGTH_LONG).show();
                }
                else if(number.length() == 10) {
                    checkForUserMobile(number);
                }
                else{
                    number = number.substring(3);
                    checkForUserMobile(number);
                }
            }
        });

    }

    private void checkForUserMobile(final String mobile){
        String url = "http://139.59.86.66:4000/api/users/getuser?number=" + mobile;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                sendOTPFunc(mobile);
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println("error111 = " + error);
                Toast.makeText(ForgotPassMobile.this, "No user exists with given mobile number!!", Toast.LENGTH_LONG).show();

            }
        });
        mRequestQueue.add(mStringRequest);
    }
    private void sendOTPFunc(final String mobile){
        String url = "http://139.59.86.66:4000/api/auth/rsendotp?number="+mobile;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Intent intent = new Intent(ForgotPassMobile.this, OTP2Activity.class);
                intent.putExtra("number", mobile);
                startActivity(intent);
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        mRequestQueue.add(mStringRequest);

    }

}
