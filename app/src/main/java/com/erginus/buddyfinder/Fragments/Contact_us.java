package com.erginus.buddyfinder.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.erginus.buddyfinder.Common.SharedPreference;
import com.erginus.buddyfinder.Common.VolleySingleton;
import com.erginus.buddyfinder.Direction;
import com.erginus.buddyfinder.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nazer on 5/7/2016.
 */
public class Contact_us extends Fragment {
    Button share,contact_direction;
    LinearLayout back;
    ImageView imageView;
    WebView web;
    String url;
    SharedPreference preference;
    public Contact_us() {
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
        View rootview = inflater.inflate(R.layout.contact, container, false);
        preference=new SharedPreference(getActivity());

        share=(Button)rootview.findViewById(R.id.share1);
        LinearLayout l1,l2;
        l1=(LinearLayout)rootview.findViewById(R.id.ll1);
        l2=(LinearLayout)rootview.findViewById(R.id.ll2);

        Animation mLoadAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
        mLoadAnimation.setDuration(2500);
        l1.startAnimation(mLoadAnimation);

        Animation mLoadAnimation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        mLoadAnimation1.setDuration(2500);
        l2.startAnimation(mLoadAnimation1);

        contact_direction=(Button)rootview.findViewById(R.id.direction_click);
        contact_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent2 = new Intent(getActivity(), Direction.class);
                intent2.putExtra("loccode", "erginus");
                startActivity(intent2);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_App();
            }
        });
        imageView=(ImageView)rootview.findViewById(R.id.img);
//        back=(LinearLayout)rootview.findViewById(R.id.er_back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        Picasso.with(getActivity()).load("http://erginus.com/assets/images/logo.png").into(imageView);
        return  rootview;
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.contact);
//        preference=new SharedPreference(Contact_us.this);
//        share=(Button)findViewById(R.id.share1);
//        contact_direction=(Button)findViewById(R.id.direction_click);
//        contact_direction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent(Contact_us.this, Direction.class);
//                intent2.putExtra("loccode", "erginus");
//                startActivity(intent2);
//            }
//        });
//
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share_App();
//            }
//        });
//        imageView=(ImageView)findViewById(R.id.img);
//        back=(LinearLayout)findViewById(R.id.er_back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        Picasso.with(Contact_us.this).load("http://erginus.com/assets/images/logo.png").into(imageView);
//
//    }
    public void share_App() {
//        String message = "Navigate Friends & Family Members: \nhttps://play.google.com/store/apps/details?id=com.erginus.buddyfinder\n"+"Use This app to Navigate the Friends. Just Exchange the code.";
        String message = "https://play.google.com/store/apps/details?id=com.erginus.buddyfinder";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(share, "Share Via"));
    }



}
