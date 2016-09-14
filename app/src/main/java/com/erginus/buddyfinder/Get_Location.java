package com.erginus.buddyfinder;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by nazer on 5/7/2016.
 */
public class Get_Location extends AppCompatActivity {
Button find;

    double latitude;
    double longitude;
    Location location;
    String url2;
    WebView web;
    ProgressDialog prDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_location);

        web = (WebView) findViewById(R.id.web1);
        find=(Button)findViewById(R.id.find1);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Get_Location.this, Direction.class);
                startActivity(intent2);
            }
        });
        Intent intent=getIntent();
        String location2=intent.getStringExtra("loc");
        Log.e("loc", "" + location2);

        url2="https://www.google.com/maps/place/"+location2;
        loaddata(url2);

        }


    public void loaddata(final String url1)
    {
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setBuiltInZoomControls(true);
        Log.e("url", "" + url1);

        prDialog = new ProgressDialog(Get_Location.this);
        prDialog.setMessage("Please wait ...");
        prDialog.show();

        web.setClickable(true);
        web.setFocusableInTouchMode(true);

        web.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(prDialog.isShowing()){
                    prDialog.dismiss();
                }

            }

        });


        web.loadUrl(url1);

//    web.setWebViewClient(new WebViewClient() {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url1);
//            return false;
//        }
//    });

    }


}