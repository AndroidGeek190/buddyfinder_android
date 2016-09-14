package com.erginus.buddyfinder.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.erginus.buddyfinder.Common.SharedPreference;
import com.erginus.buddyfinder.Direction;
import com.erginus.buddyfinder.MainActivity;
import com.erginus.buddyfinder.R;

/**
 * Created by nazer on 5/7/2016.
 */
public class My_Location extends Fragment {



    Location location;
    String url2;
    WebView web;
    ProgressDialog prDialog;
    Button share_code;
    EditText editcode;
    SharedPreference sharedPreference;
    public My_Location() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.my_location, container, false);
        editcode = (EditText)rootview.findViewById(R.id.edit);
        sharedPreference=new SharedPreference(getActivity());
        editcode.setText(sharedPreference.get_loc_code());

        editcode.setFocusable(false);
        editcode.setClickable(false);
//        web = (WebView)rootview.findViewById(R.id.web1);
        share_code = (Button)rootview.findViewById(R.id.share);

        share_code.setOnClickListener(new View.OnClickListener() {
                        @Override
            public void onClick(View v) {
                share_data();
            }
        });
//        loaddata(sharedPreference.get_url());
//
        return  rootview;
    }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_location);
//        share_code = (Button) findViewById(R.id.share);
//        web = (WebView) findViewById(R.id.web1);
////
//        share_code.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (a == 0) {
//                    share_data();
////                } else {
////                    String enter_code = editcode.getText().toString();
////                    if (enter_code.equals("")) {
////                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
////                        editcode.startAnimation(shake);
////                    }
//
////                }
//            }
//        });
//
//
//
//        Intent intent=getIntent();
//        String location2=intent.getStringExtra("loc");
//        Log.e("loc",""+location2);
//
//        url2="https://www.google.com/maps/place/"+location2;
//                loaddata(url2);
//
//        share_code.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share_data();
//            }
//        });
//    }

    public void share_data() {
        String message = "My Current Location Code: "+"my code"+"\n"+"Use This app to Navigate the Friends. Just Exchange the code.";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, sharedPreference.get_loc_text_share());
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(share, "Share Via"));
    }

//public void loaddata(final String url1) {
//    WebSettings webSettings = web.getSettings();
//    webSettings.setJavaScriptEnabled(true);
//    webSettings.setDisplayZoomControls(true);
//    webSettings.setBuiltInZoomControls(true);
//    Log.e("url", "" + url1);
//
////    prDialog = new ProgressDialog(getActivity());
////    prDialog.setMessage("Please wait ...");
////    prDialog.show();
//
//    web.setClickable(true);
//    web.setFocusableInTouchMode(true);
//
//    web.setWebViewClient(new WebViewClient() {
//
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return false;
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//
//        }
//
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
////            if (prDialog.isShowing()) {
////                prDialog.dismiss();
////            }
//
//        }
//
//    });
//
//
//    web.loadUrl(url1);
//
//}


}