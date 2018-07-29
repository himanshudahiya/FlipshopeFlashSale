package com.flipshope.app1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebsiteProducts extends AppCompatActivity {


    private String selectedWebsite;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url;
    private ArrayList<Products2> productsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private DBHandler dbHandler;
    private ProgressBar progressBar;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websites_products);
        mContext = this;
        selectedWebsite = getIntent().getStringExtra("SelectedWebsite");

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.webview_action_bar,
                null);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        // You customization
        final int actionBarColor = getResources().getColor(R.color.action_bar);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));

        final Button actionBarBack = findViewById(R.id.action_bar_back);
        actionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final Button actionBarRefresh = findViewById(R.id.action_bar_refresh);
        actionBarRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        final TextView actionBarTitle = findViewById(R.id.app_bar_title);
        actionBarTitle.setText(selectedWebsite);

        progressBar = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.products_on_website);
        mLayoutManager = new GridLayoutManager(mContext, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        dbHandler = new DBHandler(mContext, null, null, DBHandler.DATABASE_VERSION);
        mAdapter = new ProductsRecyclerAdapter(mContext, productsList, "websiteproducts", selectedWebsite, null);
        recyclerView.setAdapter(mAdapter);
        //
        progressBar.setVisibility(View.VISIBLE);
        sendRequest();

    }


    private void sendRequest() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        url = "http://139.59.86.66:65123/fetchproducts?website=" + selectedWebsite;
        progressBar.setVisibility(View.VISIBLE);
        productsList.clear();
        mAdapter.notifyDataSetChanged();
        mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    productsList.clear();
                    mAdapter.notifyDataSetChanged();
                    JSONArray parentObject = new JSONArray(response);
                    for(int i=0;i<parentObject.length();i++){
                        System.out.println("products loading = " + i);
                        JSONObject object = parentObject.getJSONObject(i);
                        Products2 products2 = new Products2();
                        products2.setProductName(object.getString("Name"));
                        products2.setProductImageURL(object.getString("image_url"));
                        products2.setProductPrice(object.getString("price"));
                        products2.setProductURL(object.getString("url"));
                        productsList.add(products2);
                        count = 0;
//                        dbHandler.insertTableProducts(products2);
                    }
                    /* Here 'response' is a String containing the response you received from the website... */

                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    count++;
                    sendRequest();

                    e.printStackTrace();
                    System.out.println("error = " + e.getStackTrace());
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                count++;
                sendRequest();
                System.out.println("error111 = " + error);
                progressBar.setVisibility(View.GONE);
            }
        });

        if(count>1000){
            Toast.makeText(mContext, "No internet connection!!",
                    Toast.LENGTH_SHORT).show();
            count = 0;
        }
        mRequestQueue.add(mStringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
