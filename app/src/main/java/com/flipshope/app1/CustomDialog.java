package com.flipshope.app1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {

    private Button yes, no;
    private CarouselView customCarouselView;
    private String[] arrayWebsitesnew;
    int NUMBER_OF_PAGES = 5;
    public Activity c;
    public CustomDialog(Activity a, String[] arrayWebsitesnew) {
        super(a);
        this.arrayWebsitesnew = arrayWebsitesnew;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = findViewById(R.id.check);
        no = findViewById(R.id.close);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        NUMBER_OF_PAGES = arrayWebsitesnew.length;
        customCarouselView = findViewById(R.id.carouselView1);
        customCarouselView.setPageCount(NUMBER_OF_PAGES);
        customCarouselView.setViewListener(viewListener);
    }
    private ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.site_view, null);
            //set view attributes here
            ImageView logo = customView.findViewById(R.id.site_logo);
            TextView site = customView.findViewById(R.id.site_name);
            site.setText(arrayWebsitesnew[position]);
            return customView;
        }


    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check:
                Intent intent = new Intent(c, WebsiteProducts.class);
                intent.putExtra("SelectedWebsite", arrayWebsitesnew[customCarouselView.getCurrentItem()]);
                c.startActivity(intent);
                dismiss();
                break;
            case R.id.close:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
        dismiss();
    }
}