package com.flipshope.app1;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


public class WebViewTest extends AppCompatActivity {

    private String URL;
    private String pid;
    private String name;
    private String emid;
    private String sale_date;
    WebView myWebView;
    private String cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_test);
        Bundle extras = getIntent().getExtras();
        URL = extras.getString("url");
        pid = extras.getString("pid");
        emid = extras.getString("emid");
        name = extras.getString("name");
        sale_date = extras.getString("sale_date");
        cookie = extras.getString("cookie");
        System.out.println("url = " + URL);
        URL = "https://" + URL;
//        URL = "https://www.flipkart.com/redmi-note-5-pro-black-64-gb/p/itmf2fc3xgmxnhpx?pid=MOBF28FTQPHUPX83&srno=s_1_3&otracker=search&lid=LSTMOBF28FTQPHUPX83H7IIOZ&fm=SEARCH&iid=1bb38e38-f5b7-46de-9abc-350236399321.MOBF28FTQPHUPX83.SEARCH&ppt=Homepage&ppn=Homepage&ssid=2putu3aog8x064g01528092258104&qH=286b43aac83aafdc";
//        Pattern MY_PATTERN = Pattern.compile("^p/[a-z0-9]*$");
//        Matcher m = MY_PATTERN.matcher(URL);
//        while (m.find()) {
//            pid = m.group(1);
//        }
//        String[] data = URL.split("/");
//        String split = data[5];
//        String[] data1 = split.split("\\?");
//        String split1 = data1[1];
//        String[] data2 = split1.split("&");
//        String[] data4 = URL.split("&");
//        URL = data4[0];
//        pid = data2[0];
//        name = data[3];
//        emid = data2[3];
//        String[] data3 = emid.split("=");
//        emid = data3[1];
//        System.out.println("name = " + name + "pid = " + pid + "emid = " + emid);


        myWebView = findViewById(R.id.webview);

        // Inflate your custom layout
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.webview_action_bar,
                null);

        // Set up your ActionBar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        // You customization
        final int actionBarColor = getResources().getColor(R.color.action_bar);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));

        final Button actionBarBack = (Button) findViewById(R.id.action_bar_back);
//        actionBarTitle.setText("Index(2)");
        actionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button actionBarRefresh = (Button) findViewById(R.id.action_bar_refresh);
        actionBarRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.reload();
            }
        });
//        actionBarSent.setText("Sent");

//        final Button actionBarStaff = (Button) findViewById(R.id.action_bar_staff);
//        actionBarStaff.setText("Staff");
//
//        final Button actionBarLocations = (Button) findViewById(R.id.action_bar_locations);
//        actionBarLocations.setText("HIPPA Locations");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient());
        // myWebView.getSettings().setUserAgentString(newUA);
        myWebView.addJavascriptInterface(new AppJavaScriptProxy(this), "androidAppProxy");

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                String buffe = "function mytest(){ document.getElementsByTagName('body')[0].innerHTML = new Date(); }";
//                myWebView.loadUrl("javascript:"+buffe);
                String scriptFile = "$s = jQuery.noConflict();\n" +
                        "var xx = window.location.href;\n" +
                        "n5sd = \"" + sale_date + "\";\n" +
                        "mitvd= \"5 31 2018 12:00:00\";\n" +
                        "var ti = 0, apic = 0;\n" +
                        "var sfvhj='';\n" +
                        "fkautobuy(\"" + cookie +  "\", \"" + name + "\", n5sd, \"" + pid + "\", 5, sfvhj, '" + emid + "');\n" +
                        "\n" +
                        "function getnextdate(sd) {\n" +
                        "    var cdate = new Date()\n" +
                        "        .getTime();\n" +
                        "    while (cdate > sd) sd = sd + 7 * 24 * 60 * 60000;\n" +
                        "    return sd;\n" +
                        "}\n" +
                        "function fkautobuy(cookie, mobile, date, stri, refresh, msg1, emid) {\n" +
                        "    //console.log(date);\n" +
                        "    date = new Date(date)\n" +
                        "        .getTime();\n" +
                        "    if (xx.search(stri) > 0) {\n" +
                        "        if (msg1) msg = msg1;\n" +
                        "        else msg = \"\";\n" +
                        "        eid = emid;\n" +
                        "        FK3buy(date, mobile, refresh);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "function FK3buy(mobdate, mobname, refresh) {\n" +
                        "    var ele = document.getElementById(\"fyureka\");\n" +
                        "    if (!ele) {\n" +
                        "        var elemDiv = document.createElement('div');\n" +
                        "        elemDiv.id = \"fyureka\";\n" +
                        "        elemDiv.style.cssText = 'width: 100%; position: fixed; bottom: 40px; right: 0px; z-index: 99999; border: green; border-radius: 10px;box-shadow: 4px 4px 20px green;background: aliceblue;';\n" +
                        "        document.body.appendChild(elemDiv);\n" +
                        "        document.getElementById(\"fyureka\")\n" +
                        "            .innerHTML = '<div id=\"fyureka\" style=\"width: 100%;position: fixed;bottom: 50px;right: 0px;z-index: 99999;/* border: green; */border-radius: 10px;box-shadow: green 4px 4px 20px;background: aliceblue;\"><div><p id=\"fmsg\" style=\"display: table-cell;vertical-align: middle;padding: 10px 20px;font-family: Helvetica, Arial,sans-serif;font-size: 12px;color: #c60;margin: 0;font-weight: 600;line-height: 16px;\"></p></div></div>';\n" +
                        "        var ele = document.getElementById(\"fyureka\");\n" +
                        "    }\n" +
                        "    cdate = new Date()\n" +
                        "        .getTime();\n" +
                        "    var tymleft = getnextdate(mobdate) - cdate;\n" +
                        "    if (tymleft > 60 * 60000 && tymleft < 601200000) {\n" +
                        "        document.getElementById(\"fmsg\")\n" +
                        "            .innerHTML = \"Login to your account if not already\";\n" +
                        "        setTimeout(function() {\n" +
                        "            FK3buy(mobdate, mobname, refresh)\n" +
                        "        }, tymleft - 59 * 60000);\n" +
                        "    } else if (tymleft < 3600000 && tymleft > 240000) { // var timeleft = document.getElementsByClassName(\"timeleft-large\");\n" +
                        "        ele.style.background = \"gold\";\n" +
                        "        document.getElementById(\"fmsg\")\n" +
                        "            .innerHTML = \"Don't forget to login before sale.<br>Click refresh if I do not turn green before 3 minutes of sale\";\n" +
                        "        setTimeout(function() {\n" +
                        "            FK3buy(mobdate, mobname, refresh)\n" +
                        "        }, tymleft - 239000);\n" +
                        "    } else if (tymleft < 240000 && tymleft > 180000) {\n" +
                        "        ele.style.background = \"white\";\n" +
                        "        document.getElementById(\"fmsg\")\n" +
                        "            .innerHTML = \"Wait, we will refresh your window within next one minute\";\n" +
                        "        setTimeout(function() {\n" +
                        "            location.reload()\n" +
                        "        }, tymleft - 180000);\n" +
                        "    } else if (tymleft < 180000 || tymleft > 604620000) {\n" +
                        "        ele.style.background = \"springgreen\";\n" +
                        "        if (refresh) {\n" +
                        "            document.getElementById(\"fmsg\")\n" +
                        "                .innerHTML = \"Tried to click \" + ti + \" times\";\n" +
                        "            if (ti == refresh * 10){ location.reload(); return;}\n" +
                        "            else if((tymleft < 90000 || tymleft > 604710000) && !(ti%10)) trycallapi(eid);\n" +
                        "        } else document.getElementById(\"fmsg\")\n" +
                        "            .innerHTML = \"we have tried to click it for you \" + ti + \" times\";\n" +
                        "        if ($s( \"span:contains('BUY NOW')\" ).length) {\n" +
                        "            // if (fkoco) setCookie(\"fsocb\", fkoco, 30, \"/checkout/init\");\n" +
                        "            // setCookie(\"CONG\", 1, 180, \"/\");\n" +
                        "            history.pushState(null, null, location.href);\n" +
                        "            callapi(eid, mobdate, mobname, refresh);\n" +
                        "        }\n" +
                        "        else if (ti < 4200) {\n" +
                        "            ti++;\n" +
                        "            setTimeout(function() {\n" +
                        "                FK3buy(mobdate, mobname, refresh);\n" +
                        "            }, 100);\n" +
                        "\n" +
                        "        }\n" +
                        "    } else if (tymleft > 601200000 && tymleft < 604795000) {\n" +
                        "        ele.remove();\n" +
                        "    }\n" +
                        "\n" +
                        "\n" +
                        "}\n" +
                        "function callapi(id, mobdate, mobname, refresh) {\n" +
                        "    apic++;\n" +
                        "    ti++;\n" +
                        "    var httpq4 = new getXMLHTTPRequest();\n" +
                        "    httpq4.open(\"POST\", '/api/5/cart', true);\n" +
                        "    httpq4.onreadystatechange = function() {\n" +
                        "        if (httpq4.readyState == 4) {\n" +
                        "            if (httpq4.status == 200) {\n" +
                        "                var mytext = httpq4.responseText;\n" +
                        "\n" +
                        "                try {\n" +
                        "                    if (JSON.parse(mytext)['RESPONSE']['cartResponse'][id]['presentInCart'] == true) {\n" +
                        "                        virwcart();\n" +
                        "                    } else if (apic < 14) return setTimeout(function() {\n" +
                        "                        FK3buy(mobdate, mobname, refresh);\n" +
                        "                    }, apic * 100);\n" +
                        "                    else document.getElementById(\"fmsg\")\n" +
                        "                        .innerHTML = \"Some error occured, please try manually\";\n" +
                        "                } catch (err) {\n" +
                        "                    if (apic < 14) return setTimeout(function() {\n" +
                        "                        FK3buy(mobdate, mobname, refresh);\n" +
                        "                    }, apic * 100);\n" +
                        "                    else document.getElementById(\"fmsg\")\n" +
                        "                        .innerHTML = \"Some error occured, please try manually\";\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    };\n" +
                        "    httpq4.setRequestHeader(\"Content-type\", \"application/json\");\n" +
                        "    httpq4.setRequestHeader('X-user-agent', navigator.userAgent + ' FKUA/website/41/website/Desktop');\n" +
                        "    httpq4.send('{\"cartContext\":{\"' + id + '\":{\"quantity\":1}}}');\n" +
                        "}\n" +
                        "function getXMLHTTPRequest() {\n" +
                        "    req = new XMLHttpRequest();\n" +
                        "    return req;\n" +
                        "}\n" +
                        "\n" +
                        "\n" +
                        "function trycallapi(id) {\n" +
                        "    var httpq4 = new getXMLHTTPRequest();\n" +
                        "    httpq4.open(\"POST\", '/api/5/cart', true);\n" +
                        "    httpq4.onreadystatechange = function() {\n" +
                        "        if (httpq4.readyState == 4) {\n" +
                        "            if (httpq4.status == 200) {\n" +
                        "                var mytext = httpq4.responseText;\n" +
                        "                try {\n" +
                        "                    // virwcart();\n" +
                        "                    if (JSON.parse(mytext)['RESPONSE']['cartResponse'][id]['presentInCart'] == true) {\n" +
                        "                        if (fkoco) setCookie(\"fsocb\", fkoco, 30, \"/checkout/init\");\n" +
                        "                        setCookie(\"CONG\", 1, 180, \"/\");\n" +
                        "                        history.pushState(null, null, location.href);\n" +
                        "                        virwcart();\n" +
                        "                        return true;\n" +
                        "                    }\n" +
                        "                } catch (err) {\n" +
                        "                    return false;\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    };\n" +
                        "    httpq4.setRequestHeader(\"Content-type\", \"application/json\");\n" +
                        "    httpq4.setRequestHeader('X-user-agent', navigator.userAgent + ' FKUA/website/41/website/Desktop');\n" +
                        "    httpq4.send('{\"cartContext\":{\"' + id + '\":{\"quantity\":1}}}');\n" +
                        "}\n" +
                        "\n" +
                        "function virwcart(){\n" +
                        "    window.location = 'https://www.flipkart.com/rv/viewcart';\n" +
                        "}";
                injectScriptFile1(view, "jquery.js");
                injectScriptFile(view, scriptFile);
//                myWebView.loadUrl("javascript:setTimeout(mytest(), 500);");
            }
        });
        myWebView.loadUrl(URL);
    }
    private void injectScriptFile(WebView view, String scriptFile) {
        InputStream input;
        try {
            input = new ByteArrayInputStream(scriptFile.getBytes(Charset.forName("UTF-8")));
//         input = getAssets().open(scriptFile);
            System.out.println("input = " + input);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            System.out.println("encoded = " + encoded);
            view.loadUrl("javascript:eval(window.atob('" + encoded + "'))");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void injectScriptFile1(WebView view, String scriptFile) {
        InputStream input;
        try {
//            input = new ByteArrayInputStream(scriptFile.getBytes(Charset.forName("UTF-8")));
            input = getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            System.out.println("encoded = " + encoded);
            view.loadUrl("javascript:eval(window.atob('" + encoded + "'))");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

