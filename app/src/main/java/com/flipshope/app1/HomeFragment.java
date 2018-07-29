package com.flipshope.app1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.flipshope.app1.DBHandler.DATABASE_VERSION;

public class HomeFragment extends Fragment {
    public View myView;
    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private List<Products2> selectedProducts = new ArrayList<Products2>();
//    private String[] urls = {"https://www.flipkart.com/redmi-note-5-pro-black-64-gb/p/itmf2fc3qjzftfch?pid=MOBF28FTKDWY5EHE&srno=s_1_1&otracker=search&lid=LSTMOBF28FTKDWY5EHEVZCTQW&fm=SEARCH&iid=4f0e6004-f32f-4f16-9ef8-d1f53ed13d11.MOBF28FTKDWY5EHE.SEARCH&ppt=Homepage&ppn=Homepage&ssid=2putu3aog8x064g01528092258104&qH=286b43aac83aafdc", "https://www.flipkart.com/redmi-note-5-pro-gold-64-gb/p/itmf2fc3txmqwdkb?pid=MOBF28FTHZYYGXFY&srno=s_1_2&otracker=search&lid=LSTMOBF28FTHZYYGXFYRE2WTC&fm=SEARCH&iid=5e6566c2-2736-4b14-823c-3c7cf4590ae3.MOBF28FTHZYYGXFY.SEARCH&ppt=Homepage&ppn=Homepage&ssid=2putu3aog8x064g01528092258104&qH=286b43aac83aafdc", "https://www.flipkart.com/redmi-note-5-pro-black-64-gb/p/itmf2fc3xgmxnhpx?pid=MOBF28FTQPHUPX83&srno=s_1_3&otracker=search&lid=LSTMOBF28FTQPHUPX83H7IIOZ&fm=SEARCH&iid=1bb38e38-f5b7-46de-9abc-350236399321.MOBF28FTQPHUPX83.SEARCH&ppt=Homepage&ppn=Homepage&ssid=2putu3aog8x064g01528092258104&qH=286b43aac83aafdc", "https://www.flipkart.com/redmi-note-5-pro-rose-gold-64-gb/p/itmf2fc3jyathzcq?pid=MOBF2BSG75JYZ5YW&srno=s_1_4&otracker=search&lid=LSTMOBF2BSG75JYZ5YWE5Q4O0&fm=SEARCH&iid=6d7a8d92-4adf-41bd-a3e3-8c70793a1f6c.MOBF2BSG75JYZ5YW.SEARCH&ppt=Homepage&ppn=Homepage&ssid=2putu3aog8x064g01528092258104&qH=286b43aac83aafdc", "https://www.flipkart.com/redmi-note-5-pro-blue-64-gb/p/itmf3qstwujazqsp?pid=MOBF2BSGFKFKVQMC&srno=s_1_5&otracker=search&lid=LSTMOBF2BSGFKFKVQMCNN4QPW&fm=SEARCH&iid=1a1622b5-505c-43cb-ac3f-1052754df3b5.MOBF2BSGFKFKVQMC.SEARCH&ppt=Homepage&ppn=Homepage&ssid=2putu3aog8x064g01528092258104&qH=286b43aac83aafdc", "https://www.flipkart.com/redmi-5a-grey-32-gb/p/itmf2fwcumw7ghwy?pid=MOBEZWXEYHCFFPHD&lid=LSTMOBEZWXEYHCFFPHDM5PWPL&marketplace=FLIPKART&srno=s_1_1&otracker=search&fm=SEARCH&iid=0cb8f7a2-d5fe-4fe1-a963-8c13f462b195.MOBEZWXEYHCFFPHD.SEARCH&ppt=Search Page&ppn=Search&ssid=b82hau38wvyr4mww1528092435295&qH=f8a10d5a7adb2998", "https://www.flipkart.com/redmi-5a-gold-32-gb/p/itmf2fwcumw7ghwy?pid=MOBEZWXEGZQPBFXH&lid=LSTMOBEZWXEGZQPBFXHMVZ0OA&marketplace=FLIPKART&srno=s_1_2&otracker=search&fm=SEARCH&iid=92bb390d-6081-4127-8918-29be69f02667.MOBEZWXEGZQPBFXH.SEARCH&ppt=Search Page&ppn=Search&ssid=b82hau38wvyr4mww1528092435295&qH=f8a10d5a7adb2998", "https://www.flipkart.com/redmi-5a-blue-32-gb/p/itmf2fwcumw7ghwy?pid=MOBF34BKSDBMFKWG&lid=LSTMOBF34BKSDBMFKWGO81MQX&marketplace=FLIPKART&srno=s_1_4&otracker=search&fm=SEARCH&iid=78fe7c0e-e8f2-4129-94c1-f7292d8798b0.MOBF34BKSDBMFKWG.SEARCH&ppt=Search Page&ppn=Search&qH=f8a10d5a7adb2998", "https://www.flipkart.com/redmi-5a-blue-16-gb/p/itmf2fwcumw7ghwy?pid=MOBF34BKHPZEPGSG&lid=LSTMOBF34BKHPZEPGSGVNKPTH&marketplace=FLIPKART&srno=s_1_5&otracker=search&fm=SEARCH&iid=9df14ee7-8bbe-424e-8bc0-337d6d5849a8.MOBF34BKHPZEPGSG.SEARCH&ppt=Search Page&ppn=Search&qH=f8a10d5a7adb2998", "https://www.flipkart.com/redmi-5a-rose-gold-32-gb/p/itmf2fwcumw7ghwy?pid=MOBEZWXE8NSAZ73T&lid=LSTMOBEZWXE8NSAZ73TZT8DKM&marketplace=FLIPKART&srno=s_1_6&otracker=search&fm=SEARCH&iid=7548e5e7-f106-482c-bb87-129d5aa3887e.MOBEZWXE8NSAZ73T.SEARCH&ppt=Search Page&ppn=Search&qH=f8a10d5a7adb2998", "https://www.flipkart.com/redmi-5a-gold-16-gb/p/itmf2fwcumw7ghwy?pid=MOBEZWXENJA6PKFM&lid=LSTMOBEZWXENJA6PKFMHVLIX8&marketplace=FLIPKART&srno=s_1_7&otracker=search&fm=SEARCH&iid=da38a8bb-fe8b-4e52-82d6-b5298939c3e8.MOBEZWXENJA6PKFM.SEARCH&ppt=Search Page&ppn=Search&qH=f8a10d5a7adb2998", "https://www.flipkart.com/redmi-5a-grey-16-gb/p/itmf2fwcumw7ghwy?pid=MOBEZWXESCPGF3GZ&lid=LSTMOBEZWXESCPGF3GZ7OIFQS&marketplace=FLIPKART&srno=s_1_8&otracker=search&fm=SEARCH&iid=8bf390f7-817e-4d74-b583-79e13a0a3743.MOBEZWXESCPGF3GZ.SEARCH&ppt=Search Page&ppn=Search&qH=f8a10d5a7adb2998", "https://www.flipkart.com/mi-led-smart-tv-4-138-8-cm-55/p/itmf3pzyhtc3sy29?pid=TVSF2BVARHHNNGZG&srno=s_1_1&otracker=search&lid=LSTTVSF2BVARHHNNGZGF45N0U&fm=SEARCH&iid=8d9f8b49-208b-4aba-be2d-8ab9034815d8.TVSF2BVARHHNNGZG.SEARCH&ppt=Product Page&ppn=Product Page&ssid=l7ckbbmmg2vhpqm81528092556024&qH=e32e3fc6720f5a4a", "https://www.flipkart.com/mi-led-smart-tv-4a-108-cm-43/p/itmf2wyxznqtdkct?pid=TVSF2WYXTKAR7RAF&srno=s_1_2&otracker=search&lid=LSTTVSF2WYXTKAR7RAFSYVTEY&fm=SEARCH&iid=caaedef7-7969-4e7b-a4c3-9037304b63d8.TVSF2WYXTKAR7RAF.SEARCH&ppt=Product Page&ppn=Product Page&ssid=l7ckbbmmg2vhpqm81528092556024&qH=e32e3fc6720f5a4a","https://www.flipkart.com/mi-led-smart-tv-4a-80-cm-32/p/itmf2wyujh3gkdhg?pid=TVSF2WYUE4PWNJKM&srno=s_1_3&otracker=search&lid=LSTTVSF2WYUE4PWNJKM3ZOYBD&fm=SEARCH&iid=83f4af6d-d094-4c5f-bf6a-eac74d647133.TVSF2WYUE4PWNJKM.SEARCH&ppt=Product Page&ppn=Product Page&ssid=l7ckbbmmg2vhpqm81528092556024&qH=e32e3fc6720f5a4a"};
    private Boolean isFirstLaunch;
    DBHandler dba;
    private ArrayList<String> arrayWebsites = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url;
    public RelativeLayout noProductBox;
    public Button selectMoreProducts;
    public int count = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = myView.findViewById(R.id.selected_products);
        mContext = getActivity();
        noProductBox = myView.findViewById(R.id.noProductBox);
        selectMoreProducts = myView.findViewById(R.id.select_more_products);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(mContext, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        SharedPreferences prefs = getActivity().getSharedPreferences("login_auth", MODE_PRIVATE);
        isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);



        String timestamp = "";
        long millis=System.currentTimeMillis();
        timestamp = timestamp + new java.sql.Date(millis) + "T" +  new java.sql.Time(millis) + "Z";
        System.out.println("timestamp = " + timestamp);




        dba =new DBHandler(getActivity(),null,null,DATABASE_VERSION);
        selectedProducts = dba.returnAddedProducts();
        if(selectedProducts.size()!=0){
            mRecyclerView.setVisibility(View.VISIBLE);
            noProductBox.setVisibility(View.GONE);
            selectMoreProducts.setVisibility(View.VISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.GONE);
            noProductBox.setVisibility(View.VISIBLE);
            selectMoreProducts.setVisibility(View.GONE);

        }

        url = "http://139.59.86.66:65123/fetchWebsites";

        noProductBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();

//                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                builder.setTitle("Select a website")
//                        .setItems(arrayWebsitesnew, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(getActivity(), WebsiteProducts.class);
//                                intent.putExtra("SelectedWebsite", arrayWebsitesnew[which]);
//                                startActivity(intent);
////                                getActivity().finish();
//                            }
//
//
//                        });
//                AlertDialog dialog = builder.create();
//                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
//                String titleText = "Select a website";
//                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
//
//                ssBuilder.setSpan(
//                        foregroundColorSpan,
//                        0,
//                        titleText.length(),
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                );
//                dialog.setTitle(ssBuilder);
//                dialog.show();
            }
        });

        selectMoreProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
//                 final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                 System.out.println(arrayWebsites);
//                 final String[] arrayWebsitesnew = new String[arrayWebsites.size()];
//                 arrayWebsites.toArray(arrayWebsitesnew);
// //                System.out.println(arrayWebsites + "     " + arrayWebsitesnew[0]);
//                 builder.setTitle("Select a website")
//                         .setItems(arrayWebsitesnew, new DialogInterface.OnClickListener() {
//                             public void onClick(DialogInterface dialog, int which) {
//                                 // The 'which' argument contains the index position
//                                 // of the selected item
//                                 // call api for products

//                                 Intent intent = new Intent(getActivity(), WebsiteProducts.class);
//                                 intent.putExtra("SelectedWebsite", arrayWebsitesnew[which]);
//                                 startActivity(intent);
// //                                getActivity().finish();
//                             }


//                         });
//                 AlertDialog dialog = builder.create();
//                 ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
//                 String titleText = "Select a website";

//                 // Initialize a new spannable string builder instance
//                 SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

//                 // Apply the text color span
//                 ssBuilder.setSpan(
//                         foregroundColorSpan,
//                         0,
//                         titleText.length(),
//                         Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                 );

//                 // Set the alert dialog title using spannable string builder
//                 dialog.setTitle(ssBuilder);
//                 dialog.show();
            }
        });
        mAdapter = new ProductsRecyclerAdapter(mContext, selectedProducts, "home", "", myView);
        mRecyclerView.setAdapter(mAdapter);

        return myView;
    }


    private void sendRequest() {



        System.out.println("send req called");
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        mStringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    arrayWebsites.clear();
                    JSONArray parentObject = new JSONArray(response);
                    for(int i=0;i<parentObject.length();i++){
                        JSONObject sitename = parentObject.getJSONObject(i);
                        count = 0;
                        arrayWebsites.add(sitename.getString("site_name"));
                        final String[] arrayWebsitesnew = new String[arrayWebsites.size()];
                        arrayWebsites.toArray(arrayWebsitesnew);
                        CustomDialog cdd=new CustomDialog(getActivity(), arrayWebsitesnew);
                        cdd.show();
                    }
                    System.out.println("arraywebsites = " + arrayWebsites);
                    /* Here 'response' is a String containing the response you received from the website... */

                } catch (JSONException e) {
//                    e.printStackTrace();
                    count++;
                    sendRequest();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

                count++;

                sendRequest();

            }
        });

        if(count>1000){
            System.out.println(count + " = count");
            Toast.makeText(mContext, "No internet connection!!",
                    Toast.LENGTH_SHORT).show();
            count = 0;
        }
        mRequestQueue.add(mStringRequest);
    }


//    private class FirstTimeLogin extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            for (int i=0;i<urls.length;i++) {
//                try {
//                    System.out.println("url0 = " + urls[i]);
//                    Document document = Jsoup.connect(urls[i]).get();
//                    String name = document.select("._9E25nV").first().text();
//                    String price = document.select("._1vC4OE").first().text();
//                    Element image = document.select(".Yun65Y").first();
//                    String url = image.attr("abs:src");
//                    Products2 products21 = new Products2();
//                    products21.setProductName(name);
//                    products21.setProductImageURL(url);
//                    products21.setProductPrice(price);
//                    products21.setProductURL(urls[i]);
//                    products2.add(products21);
//                    dba.insertTableProducts(products21);
//                    System.out.println("product no = " + i + "product name = " + name);
//                    publishProgress();
//                } catch (Exception e) {
//                    System.out.println("error parsing link");
//                    e.printStackTrace();
//
//                }
//
//            }
//
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
////            TextView txt = (TextView) findViewById(R.id.output);
////            txt.setText("Executed"); // txt.setText(result);
//            // might want to change "executed" for the returned string passed
//            // into onPostExecute() but that is upto you
//            mAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            System.out.println("onprogressupdate");
////            mAdapter.notifyDataSetChanged();
//        }
//    }
//
//
//    private class OnDBUpdate extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            ArrayList<Products2> products2Copy = new ArrayList<>();
//            for (int i=0;i<urls.length;i++) {
//                try {
//                    System.out.println("onupdate");
//                    Document document = Jsoup.connect(urls[i]).get();
//                    String name = document.select("._9E25nV").first().text();
//                    String price = document.select("._1vC4OE").first().text();
//                    Element image = document.select(".Yun65Y").first();
//                    String url = image.attr("abs:src");
//                    Products2 products21 = new Products2();
//                    products21.setProductName(name);
//                    products21.setProductImageURL(url);
//                    products21.setProductPrice(price);
//                    products21.setProductURL(urls[i]);
//                    System.out.println("product21 = " + products21.getProductURL() + "  " + products21.getProductImageURL() + " " + products21.getProductName() + "   " + products21.getProductPrice());
//                    products2Copy.add(products21);
//                } catch (Exception e) {
//                    System.out.println("error parsing link" + e.toString());
//                    e.printStackTrace();
//                }
//            }
//            dba.updateProducts(products2Copy);
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            System.out.println("onupdate post execute");
//            products2.clear();
//            products2.addAll(dba.returnProducts());
//            mAdapter.notifyDataSetChanged();
//            System.out.println(products2.get(0).getProductName());
//        }
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            System.out.println("onprogressupdate");
////            mAdapter.notifyDataSetChanged();
//        }
//    }

    public void ViewInvalidate(View myView){
        mRecyclerView = myView.findViewById(R.id.selected_products);
        noProductBox = myView.findViewById(R.id.noProductBox);
        selectMoreProducts = myView.findViewById(R.id.select_more_products);
        mRecyclerView.setVisibility(View.GONE);
        noProductBox.setVisibility(View.VISIBLE);
        selectMoreProducts.setVisibility(View.GONE);
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        myView.invalidate();
        dba =new DBHandler(getActivity(),null,null,DATABASE_VERSION);
        selectedProducts = dba.returnAddedProducts();
//
        if(selectedProducts.size()!=0){
            mRecyclerView.setVisibility(View.VISIBLE);
            noProductBox.setVisibility(View.GONE);
            selectMoreProducts.setVisibility(View.VISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.GONE);
            noProductBox.setVisibility(View.VISIBLE);
            selectMoreProducts.setVisibility(View.GONE);

        }
        mAdapter = new ProductsRecyclerAdapter(mContext, selectedProducts, "home", "", myView);
        mRecyclerView.setAdapter(mAdapter);
        //Refresh your stuff here
    }
}