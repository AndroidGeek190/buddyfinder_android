package com.erginus.buddyfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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
import com.erginus.buddyfinder.Common.Background_Location_Service;
import com.erginus.buddyfinder.Common.SharedPreference;
import com.erginus.buddyfinder.Common.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nazer on 5/7/2016.
 */
public class Direction extends AppCompatActivity {
    FloatingActionButton back,refresh;
    WebView web;
    ProgressDialog prDialog;
    String from,to,url;
    SharedPreference preference;
    Thread t;
    volatile boolean stop = false;
    Long time2;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direction);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
preference=new SharedPreference(Direction.this);
        String time=preference.get_view_refresh();
        time2= Long.valueOf(time)*1000;
        back=(FloatingActionButton)findViewById(R.id.back1);
        refresh=(FloatingActionButton)findViewById(R.id.refresh1);
        web = (WebView) findViewById(R.id.web1);
coordinatorLayout=(CoordinatorLayout)findViewById(R.id.cord);
        Intent intent=getIntent();
        to=intent.getStringExtra("loccode");

        direction();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                t.interrupt();
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("distance current :",""+preference.get_curr_dist());
                direction();
            }
        });

//        refresh();

    }

//public void refresh()
//{
//
//     t = new Thread() {
//        @Override
//        public void run() {
//            try {
//                while (!isInterrupted()) {
//                    Thread.sleep(time2);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e("distance current :",""+preference.get_curr_dist());
//                            if(preference.get_curr_dist()>20) {
//                                direction();
//                            }
//
//                        }
//                    });
//                }
//            } catch (InterruptedException e) {
//                Log.e("intrrupt",""+e);
//            }
//        }
//    };t.start();
//
//
//}
    @Override
    public void onDestroy() {
        stop = true;
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        t.interrupt();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop = false;
    }

    public void loaddata(final String url1)
    {
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setBuiltInZoomControls(true);
        Log.e("url", "" + url1);

        prDialog = new ProgressDialog(Direction.this);
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

    public void direction() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(Direction.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            StringRequest sr = new StringRequest(Request.Method.POST, "http://erginus.net/buddyfinder_web/api/route", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
//                    Log.e("response====","" + response.toString());

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        if (serverCode.equalsIgnoreCase("0")) {
                            Toast.makeText(Direction.this, ""+serverMessage, Toast.LENGTH_SHORT).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject obj = object.getJSONObject("data");
                                    url = obj.getString("route_url");
                                    loaddata(url);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Bad Internet Connection", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    }
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("from_location_code",preference.get_loc_code());
                    params.put("to_location_code", to);

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
