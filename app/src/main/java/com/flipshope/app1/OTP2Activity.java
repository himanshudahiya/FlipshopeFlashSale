package com.flipshope.app1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mukesh.OtpListener;
import com.mukesh.OtpView;

public class OTP2Activity extends AppCompatActivity {
    private OtpView otpView;
    private TextView resendOTP;
    private TextView changeNumber;
    private TextView enterLabel;
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp2);
        Intent intent = getIntent();
        number = intent.getStringExtra("number");
        enterLabel = findViewById(R.id.enterLabel);
        enterLabel.setText(enterLabel.getText() + ":"+number);
        new CountDownTimer(120000, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval

            public void onTick(long millisUntilFinished) {
                TextView timer = findViewById(R.id.timer);
                long seconds = millisUntilFinished/1000;
                long p1 = seconds % 60;
                long p2 = seconds / 60;
                timer.setText(p2+":"+p1);

            }
            public void onFinish() {
                Toast.makeText(OTP2Activity.this, "OTP Expired!! Click on Resend OTP to send a new one.", Toast.LENGTH_LONG).show();
            }
        }.start();
        otpView = findViewById(R.id.otp_view);
        otpView.setListener(new OtpListener() {
            @Override public void onOtpEntered(String otp) {
                System.out.println("otp = " + otp);
                verifyOTP(number, otp);
            }
        });
        resendOTP = findViewById(R.id.notReceivedButton);
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP(number);

            }
        });
        changeNumber = findViewById(R.id.changeNumberButton);
        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(OTP2Activity.this);
                edittext.setInputType(InputType.TYPE_CLASS_PHONE);
                AlertDialog dialog = new AlertDialog.Builder(OTP2Activity.this)
                        .setTitle("Change number")
                        .setMessage("Enter new mobile number")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                number = edittext.getText().toString();
                                checkForUserMobile(number);

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setView(edittext)
                        .show();

                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
                String titleText = "Change number";

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

            }
        });

    }

    public void verifyOTP(final String number, String otp){
        String url = "http://139.59.86.66:4000/api/auth/rverifyotp?number="+number+"&otp="+otp;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){

                Intent intent = new Intent(OTP2Activity.this, NewPassEnter.class);
                intent.putExtra("id", response);
                System.out.println("response = " + response);
                intent.putExtra("number", number);
                startActivity(intent);
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(OTP2Activity.this, "Invalid OTP", Toast.LENGTH_LONG).show();
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void sendOTP(String mobile){
        String url = "http://139.59.86.66:4000/api/auth/rsendotp?number="+mobile;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
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


    private void checkForUserMobile(final String mobile){
        String url = "http://139.59.86.66:4000/api/users/getuser?number=" + mobile;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                sendOTP(mobile);
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println("error111 = " + error);
                Toast.makeText(OTP2Activity.this, "No user exists with given mobile number!!", Toast.LENGTH_LONG).show();

            }
        });
        mRequestQueue.add(mStringRequest);
    }
}
