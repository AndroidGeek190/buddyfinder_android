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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by nazer on 5/7/2016.
 */
public class My_Location extends AppCompatActivity  {



    Location location;
    String url2;
    WebView web;
    ProgressDialog prDialog;
    Button share_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location);
        share_code = (Button) findViewById(R.id.share);
        web = (WebView) findViewById(R.id.web1);



        Intent intent=getIntent();
        String location2=intent.getStringExtra("loc");
        Log.e("loc",""+location2);

        url2="https://www.google.com/maps/place/"+location2;
                loaddata(url2);

        share_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_data();
            }
        });
    }

    public void share_data() {
        String message = "https://erginus.net";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(share, "Share Via"));
    }
public void loaddata(final String url1) {
    WebSettings webSettings = web.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDisplayZoomControls(true);
    webSettings.setBuiltInZoomControls(true);
    Log.e("url", "" + url1);

    prDialog = new ProgressDialog(My_Location.this);
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
            if (prDialog.isShowing()) {
                prDialog.dismiss();
            }

        }

    });


    web.loadUrl(url1);

}

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}