package com.flipshope.app1;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class AppJavaScriptProxy {

    private Activity activity = null;

    public AppJavaScriptProxy(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void showMessage(String message) {

        Toast toast = Toast.makeText(this.activity.getApplicationContext(),
                message,
                Toast.LENGTH_SHORT);

        toast.show();
    }

}