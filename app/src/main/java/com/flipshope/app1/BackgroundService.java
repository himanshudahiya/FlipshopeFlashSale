package com.flipshope.app1;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.flipshope.app1.DBHandler.DATABASE_VERSION;

public class BackgroundService extends Service {

    public static final long NOTIFY_INTERVAL = 30*60*1000; // 30 minutes
    DBHandler dba;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        SharedPreferences prefs = getSharedPreferences("login_auth", MODE_PRIVATE);
        Boolean auth = prefs.getBoolean("auth", false);
        if (auth) {
            dba = new DBHandler(getApplicationContext(), null, null, DATABASE_VERSION);
            System.out.println("background service");
            if (mTimer != null) {
                mTimer.cancel();
            } else {
                // recreate new
                mTimer = new Timer();
            }
            // schedule task
            mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
        }
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
//                    sendRequest();
                }

            });
        }



    }

    private String getDateTime() {
        // get date time in custom format
        String timestamp = "";
        long millis=System.currentTimeMillis();
        timestamp = timestamp + new java.sql.Date(millis) + "T" +  new java.sql.Time(millis) + "Z";
        System.out.println("timestamp = " + timestamp);

        return timestamp;
    }

    private void sendRequest() {
//        mRequestQueue = Volley.newRequestQueue(this);
//        String url = "http://192.168.0.20:65123/fetch?time=" + getDateTime();
//
//        mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
//            @Override
//            public void onResponse(String response){
//                try {
//                    JSONArray parentObject = new JSONArray(response);
//                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                            .permitAll().build();
//
//                    StrictMode.setThreadPolicy(policy);
//                    for(int i=0;i<parentObject.length();i++){
//                        System.out.println("products loading = " + i);
//                        JSONObject object = parentObject.getJSONObject(i);
//                        Products2 products2 = new Products2();
//                        products2.setProductName(object.getString("Name"));
//                        products2.setProductImageURL(object.getString("image_url"));
//                        products2.setProductPrice(object.getString("price"));
//                        products2.setProductURL(object.getString("url"));
//                        products2.setEmid(object.getString("emid"));
//                        products2.setPid(object.getString("pid"));
//                        products2.setSaledate(object.getString("sale_date"));
//                        products2.setTime(object.getString("time"));
//                        dba.insertTableProducts(products2);
//                    }
//                    /* Here 'response' is a String containing the response you received from the website... */
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error){
//                System.out.println("error111 = " + error);
//
//            }
//        });
//        mRequestQueue.add(mStringRequest);
    }


}