package com.flipshope.app1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment {

    private TextView mUsername;
    private TextView mEmail;
    private TextView mMobileNumber;
    View myView;
    private String name;
    private String email;
    private String id;
    private String mobile;
    private String token;
    private EditText userInput;
    private Boolean isEmailVerified;
    private Boolean isNumVerified;
    private TextView mPassword;
    View view;
    private Boolean isSite;
    private TextView verifyEmailButton;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_account, container, false);
        view = inflater.inflate(R.layout.activity_main, container, false);
        setHasOptionsMenu(true);
        SharedPreferences prefs = getActivity().getSharedPreferences("login_auth", MODE_PRIVATE);
        name = prefs.getString("name", null);
        email = prefs.getString("email", null);
        System.out.println("token = " + prefs.getString("token", null));
        id = prefs.getString("id", null);
        mobile = prefs.getString("mobile", null);
        token = prefs.getString("token", null);
        isEmailVerified = prefs.getBoolean("isEmailVerified", false);
        isNumVerified = prefs.getBoolean("isNumVerified", false);
        isSite = prefs.getBoolean("isSite", false);
        mUsername = myView.findViewById(R.id.username_acc);
        mEmail = myView.findViewById(R.id.email_acc);
        mMobileNumber = myView.findViewById(R.id.mobileNo_acc);
        mPassword = myView.findViewById(R.id.acc_password);
        verifyEmailButton = myView.findViewById(R.id.verifyEmailButton);
        if(isSite){
            mPassword.setVisibility(View.GONE);
        }
        else{
            mPassword.setVisibility(View.VISIBLE);
        }
        mUsername.setText(name);
        mEmail.setText(email);
        mMobileNumber.setText(mobile);

//        editAccount = myView.findViewById(R.id.editAccount);
//        System.out.println("username and email = " + name + " " + email);
//        editAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AccountActivityEdit.class);
//                intent.putExtra("username", mUsername.getText().toString());
//                intent.putExtra("email", mEmail.getText().toString());
//                intent.putExtra("mobile", mMobileNumber.getText().toString());
//                intent.putExtra("id", id);
//                intent.putExtra("token", token);
//                startActivity(intent);
//
//            }
//        });
        if(!isEmailVerified){
            Drawable imgRight = getContext().getResources().getDrawable( R.drawable.ic_error );
            imgRight.setBounds( 0, 0, 60, 60 );
            Drawable imgLeft = getContext().getResources().getDrawable(R.drawable.ic_email_black_24dp);
            imgLeft.setBounds( 0, 0, 60, 60 );
            mEmail.setCompoundDrawables(imgLeft, null, imgRight , null);
            verifyEmailButton.setVisibility(View.VISIBLE);
        }
        else if(isEmailVerified){
            Drawable imgRight = getContext().getResources().getDrawable( R.drawable.ic_checked );
            imgRight.setBounds( 0, 0, 60, 60 );
            Drawable imgLeft = getContext().getResources().getDrawable(R.drawable.ic_email_black_24dp);
            imgLeft.setBounds( 0, 0, 60, 60 );
            mEmail.setCompoundDrawables(imgLeft, null, imgRight , null);
            verifyEmailButton.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.email_acc);
            mMobileNumber.setLayoutParams(params);
        }
        if(!isNumVerified){
            Drawable imgRight = getContext().getResources().getDrawable( R.drawable.ic_error );
            imgRight.setBounds( 0, 0, 60, 60 );
            Drawable imgLeft = getContext().getResources().getDrawable(R.drawable.ic_call_black_24dp);
            imgLeft.setBounds( 0, 0, 60, 60 );
            mMobileNumber.setCompoundDrawables(imgLeft, null, imgRight , null);
        }
        else if(isNumVerified){
            Drawable imgRight = getContext().getResources().getDrawable( R.drawable.ic_checked );
            imgRight.setBounds( 0, 0, 60, 60 );
            Drawable imgLeft = getContext().getResources().getDrawable(R.drawable.ic_call_black_24dp);
            imgLeft.setBounds( 0, 0, 60, 60 );
            mMobileNumber.setCompoundDrawables(imgLeft, null, imgRight , null);
        }

        mUsername.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (mUsername.getRight() - mUsername.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        LayoutInflater li = LayoutInflater.from(getContext());
                        View promptsView = li.inflate(R.layout.customeditname, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getContext());

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        userInput =  promptsView
                                .findViewById(R.id.editTextDialogUserInput);
                        userInput.setText(name);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Save",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                // get user input and set it to result
                                                // edit text
                                                sendRequest();
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

                        return true;
                    }
                }
                return true;
            }
        });

        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPassword.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (mUsername.getRight() - mUsername.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Intent intent = new Intent(getContext(), ChangePassword.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        return true;
                    }
                }
                return true;
            }
        });

        return myView;
    }
    private void sendRequest() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://139.59.86.66:4000/api/users/update?id=" + id;
        name = userInput.getText().toString().trim();
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("login_auth", MODE_PRIVATE).edit();
                editor.putString("name",name);

                editor.apply();
                mUsername.setText(name);
                NavigationView navigationView = getActivity().getWindow().findViewById(R.id.nav_view);
                View header = navigationView.getHeaderView(0);
                TextView navHeaderUsername = header.findViewById(R.id.login_user_name);
                navHeaderUsername.setText("Hello, " + name + "!");
                Toast.makeText(getContext(), "Account details successfully updated!!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", name);
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



}
