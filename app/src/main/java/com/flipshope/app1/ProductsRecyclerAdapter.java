package com.flipshope.app1;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.security.interfaces.DSAKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<Products2> productsList;
    private String mCurrentActivity;
    private String mSelectedWebsite;
    private View mView;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mProductName;
        public TextView mProductPrice;
        public ImageView mProductImage;
        public Button mBuyNow;
        public Button mAddToCart;
        public Button removeFromCart;
        public RelativeLayout productCard;
        public TextView mSaleDate;
        public ViewHolder(View view) {
            super(view);
            mProductName = view.findViewById(R.id.productName);
            mProductPrice = view.findViewById(R.id.productPrice);
            mProductImage = view.findViewById(R.id.productImage);
            mBuyNow = view.findViewById(R.id.buyNowButton);
            mAddToCart = view.findViewById(R.id.addToCart);
            removeFromCart = view.findViewById(R.id.removeFromCart);
            productCard = view.findViewById(R.id.product_card);
            mSaleDate = view.findViewById(R.id.sale_date);
        }
    }


    public ProductsRecyclerAdapter(Context mContext, List<Products2> productsList, String mCurrentActivity, String mSelectedWebsite, View mView) {
        this.mContext = mContext;
        this.productsList = productsList;
        this.mCurrentActivity = mCurrentActivity;
        this.mSelectedWebsite = mSelectedWebsite;
        this.mView = mView;
        System.out.println("productsList = " + this.productsList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productscard, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Products2 products = productsList.get(position);
        holder.mProductName.setText(products.getProductName());
        holder.mProductPrice.setText("\u20B9" + products.getProductPrice());


        if(mCurrentActivity.equals("home") || mCurrentActivity.equals("varients")) {
            String reformattedStr = "";
            SimpleDateFormat fromUser = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
            SimpleDateFormat fromUser2 = new SimpleDateFormat("dd-MM-yyyy");

            SimpleDateFormat toformat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            try {

                reformattedStr = toformat.format(fromUser.parse(products.getSaledate()));
            } catch (ParseException e) {
                try {
                    reformattedStr = toformat.format(fromUser2.parse(products.getSaledate()));
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

            reformattedStr = "<font color=#1a7067><b style='color:#000'>Sale Date: </b></font>" + reformattedStr;
            holder.mSaleDate.setText(Html.fromHtml(reformattedStr));
        }
//        Uri uri = Uri.parse(products.getProductImageURL());
//        holder.mProductImage.setImageURI(uri);
        Glide.with(mContext).load(products.getProductImageURL()).into(holder.mProductImage);


        holder.mBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewTest.class);
                intent.putExtra("url", products.getProductURL());
                intent.putExtra("pid", products.getPid());
                intent.putExtra("emid", products.getEmid());
                intent.putExtra("name", products.getProductName());
                intent.putExtra("sale_date", products.getSaledate());
                intent.putExtra("cookie", products.getCookie());
                mContext.startActivity(intent);
            }
        });

        if(mCurrentActivity.equals("home")){
            holder.mAddToCart.setVisibility(View.GONE);
            holder.removeFromCart.setVisibility(View.VISIBLE);
            holder.mBuyNow.setVisibility(View.VISIBLE);
            holder.mSaleDate.setVisibility(View.VISIBLE);
        }
        if (mCurrentActivity.equals("websiteproducts")){
            holder.mAddToCart.setVisibility(View.GONE);
            holder.removeFromCart.setVisibility(View.GONE);
            holder.mBuyNow.setVisibility(View.GONE);
            holder.mSaleDate.setVisibility(View.GONE);
        }
        if (mCurrentActivity.equals("varients")){
            holder.mBuyNow.setVisibility(View.VISIBLE);
            holder.mSaleDate.setVisibility(View.VISIBLE);
            DBHandler dba = new DBHandler(mContext, null, null, DBHandler.DATABASE_VERSION);
            if(dba.checkAdded(products.getProductURL())){
                holder.removeFromCart.setVisibility(View.VISIBLE);
                holder.mAddToCart.setVisibility(View.GONE);
            }
            else {
                holder.mAddToCart.setVisibility(View.VISIBLE);
                holder.removeFromCart.setVisibility(View.GONE);
            }


        }
        holder.mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler dba = new DBHandler(mContext, null, null, DBHandler.DATABASE_VERSION);
                dba.addToCart(products.getProductURL());

                Toast.makeText(mContext, "Added to cart",
                        Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler dba = new DBHandler(mContext, null, null, DBHandler.DATABASE_VERSION);
                dba.removeFromCart(products.getProductURL());
                if (mCurrentActivity.equals("varients")){
                    holder.mAddToCart.setVisibility(View.VISIBLE);
                    holder.removeFromCart.setVisibility(View.GONE);
                }
                else {
                    productsList.remove(products);
                    if (productsList.size() == 0) {
                        HomeFragment fragment = new HomeFragment();
                        fragment.ViewInvalidate(mView);
                    }

                    notifyDataSetChanged();
                }
                Toast.makeText(mContext, "Removed from cart",
                        Toast.LENGTH_SHORT).show();
            }
        });

        if (mCurrentActivity.equals("websiteproducts")){
            holder.productCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductVarients.class);
                    intent.putExtra("SelectedWebsite", mSelectedWebsite);
                    intent.putExtra("SelectedProduct", products.getProductName());
                    mContext.startActivity(intent);
                }
            });
        }
    }




    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return productsList.size();
    }
}