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
import android.widget.ImageView;

import com.erginus.buddyfinder.Common.SharedPreference;
import com.erginus.buddyfinder.Direction;
import com.erginus.buddyfinder.R;
import com.squareup.picasso.Picasso;

/**
 * Created by nazer on 5/7/2016.
 */
public class Get_Location extends Fragment {
Button find;

    double latitude;
    double longitude;
    Location location;
    String url2;
    WebView web;
    ProgressDialog prDialog;
    SharedPreference sharedPreference;
    EditText editcode1;
    public Get_Location() {
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
        View rootview = inflater.inflate(R.layout.get_location, container, false);
        sharedPreference=new SharedPreference(getActivity());
//        web = (WebView)rootview.findViewById(R.id.web1);
        find=(Button)rootview.findViewById(R.id.find1);
        editcode1 = (EditText)rootview.findViewById(R.id.edit1);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enter_code = editcode1.getText().toString();
                if (enter_code.equals("")) {
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    editcode1.startAnimation(shake);
                } else {

                    Intent intent2 = new Intent(getActivity(), Direction.class);
                    intent2.putExtra("loccode", "" + enter_code);
                    startActivity(intent2);

                }
            }
        });
//        loaddata(sharedPreference.get_url());
//
        return  rootview;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.get_location);
//
//        web = (WebView) findViewById(R.id.web1);
//        find=(Button)findViewById(R.id.find1);
//        find.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent(Get_Location.this, Direction.class);
//                startActivity(intent2);
//            }
//        });
//        Intent intent=getIntent();
//        String location2=intent.getStringExtra("loc");
//        Log.e("loc", "" + location2);
//
//        url2="https://www.google.com/maps/place/"+location2;
//        loaddata(url2);
//
//        }


    public void loaddata(final String url1)
    {
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setBuiltInZoomControls(true);
        Log.e("url", "" + url1);

//        prDialog = new ProgressDialog(getActivity());
//        prDialog.setMessage("Please wait ...");
//        prDialog.show();

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
//                if(prDialog.isShowing()){
//                    prDialog.dismiss();
//                }

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