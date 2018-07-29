package com.flipshope.app1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.ArrayList;

public class ProductVarients extends AppCompatActivity {

    private String selectedWebsite;
    private String selectedProduct;
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
                finish();
            }
        });

        final Button actionBarRefresh = findViewById(R.id.action_bar_refresh);
        actionBarRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        selectedProduct = getIntent().getStringExtra("SelectedProduct");
        final TextView actionBarTitle = findViewById(R.id.app_bar_title);
        actionBarTitle.setText(selectedProduct);

        selectedWebsite = getIntent().getStringExtra("SelectedWebsite");

        progressBar = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.products_on_website);
        mLayoutManager = new GridLayoutManager(mContext, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        dbHandler = new DBHandler(mContext, null, null, DBHandler.DATABASE_VERSION);
        progressBar.setVisibility(View.VISIBLE);
//        productsList = dbHandler.returnProducts(selectedProduct);
//        if (productsList.size()==0){
//            sendRequest();
//        }
//        else{
//
//        }
        System.out.println("selectedWebsite + selectedProduct" + selectedProduct + " " + selectedWebsite);
        sendRequest();
        mAdapter = new ProductsRecyclerAdapter(mContext, productsList, "varients", selectedWebsite, null);
        recyclerView.setAdapter(mAdapter);

    }


    private void sendRequest() {
        if (mRequestQueue== null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        String query = "";
        try {
            query = URLEncoder.encode(selectedProduct, "utf-8");
        }catch (Exception e){
            query = selectedProduct;
        }
        url = "http://139.59.86.66:65123/fetchvariants?website=" + selectedWebsite + "&pname=" + query;
        progressBar.setVisibility(View.VISIBLE);
System.out.println("url = " + url);
        mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONArray parentObject = new JSONArray(response);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    productsList.clear();
                    mAdapter.notifyDataSetChanged();
                    for(int i=0;i<parentObject.length();i++){
                        System.out.println("products loading = " + i);
                        JSONObject object = parentObject.getJSONObject(i);
                        Products2 products2 = new Products2();
                        products2.setProductName(object.getString("Name"));
                        products2.setProductImageURL(object.getString("image_url"));
                        products2.setProductPrice(object.getString("price"));
                        products2.setProductURL(object.getString("url"));
                        products2.setEmid(object.getString("emid"));
                        products2.setPid(object.getString("pid"));
                        products2.setSaledate(object.getString("sale_date"));
                        products2.setTime(object.getString("time"));
                        productsList.add(products2);
//                        dbHandler.insertTableProducts(products2);
                        count = 0;
                    }
System.out.println("product = " + productsList.get(0).getProductName());
                    ArrayList<Products2> productsList2 = dbHandler.returnProducts(selectedProduct);
                    for (int i=0;i<productsList2.size();i++){
                        Products2 product222 = productsList2.get(i);
                        if(product222.getIsAdded() == 1){
                            for (int j=0;j<productsList.size();j++){
                                Products2 product2222 = productsList.get(j);
                                if(product222.getPid().equals(product2222.getPid())){
                                    productsList.get(j).setIsAdded(1);
                                    break;
                                }
                            }
                        }
                        dbHandler.delete(product222.getPid());
                    }
                    dbHandler.insertTableProducts(productsList);
                    /* Here 'response' is a String containing the response you received from the website... */
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    count++;
                    sendRequest();
                    System.out.println("count1 = " + count);
                    progressBar.setVisibility(View.GONE);
                    }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println(error);
                progressBar.setVisibility(View.GONE);
                count++;
                System.out.println("count = " + count);
                sendRequest();
            }
        });

        if(count>1000){
            Toast.makeText(mContext, "No internet connection!!",
                    Toast.LENGTH_SHORT).show();
            count = 0;
        }
        mRequestQueue.add(mStringRequest);
    }

}
