package com.erginus.buddyfinder;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.erginus.buddyfinder.Common.ExitActivity;
import com.erginus.buddyfinder.Common.SharedPreference;
import com.erginus.buddyfinder.Common.VolleySingleton;
import com.erginus.buddyfinder.Fragments.Contact_us;
import com.erginus.buddyfinder.Fragments.Get_Location;
import com.erginus.buddyfinder.Fragments.My_Location;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
//    ImageView my_loc, get_loc, contact;
    LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    Location location,lastKnownLocation_byGps,lastKnownLocation_byNetwork;
    ProgressDialog pDialog;
    WebView web;
    String url2, my_code,share_code;
    int a = 0,dist;
    EditText editcode;
    Button share_code1;
    SharedPreference preference;
    LinearLayout l1, l2,linearLayout;
    FrameLayout frameLayout,frameweb;
    LocationProvider high;
    FloatingActionButton address;
    CoordinatorLayout coordinatorLayout;
    LinearLayout my_loc, get_loc, contact;
    TextView my_loc1,get_loc1,contact1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preference = new SharedPreference(MainActivity.this);
//        dialog = new ProgressDialog(MainActivity.this);
        pDialog = new ProgressDialog(MainActivity.this);
        editcode = (EditText)findViewById(R.id.edit);
        linearLayout=(LinearLayout) findViewById(R.id.llframe);
        frameLayout=(FrameLayout)findViewById(R.id.contain1);
        frameweb=(FrameLayout)findViewById(R.id.webcode);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.cord);

        l1=(LinearLayout)findViewById(R.id.ll11);
        l2=(LinearLayout)findViewById(R.id.ll12);
        address=(FloatingActionButton)findViewById(R.id.find_add);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Find_Address.class);
                startActivity(intent);
            }
        });
        Animation mLoadAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_up);
        mLoadAnimation.setDuration(2000);
        l1.startAnimation(mLoadAnimation);
        l2.startAnimation(mLoadAnimation);


        editcode.setFocusable(false);
        editcode.setClickable(false);
        web = (WebView)findViewById(R.id.web11);
        share_code1 = (Button)findViewById(R.id.share);

        share_code1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_data();
            }
        });
        locationManager = (LocationManager)getApplication().getSystemService(Context.LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        high=locationManager.getProvider(locationManager.getBestProvider(createFineCriteria(),true));
        if (!isGPSEnabled) {
            buildAlertMessageNoGps();
        }
        else{

            network();
        }
        my_loc=(LinearLayout) findViewById(R.id.my);
        get_loc=(LinearLayout)findViewById(R.id.get);
        contact=(LinearLayout)findViewById(R.id.contact_us);

        my_loc1=(TextView) findViewById(R.id.text_sl);
        get_loc1=(TextView)findViewById(R.id.text_ff);
        contact1=(TextView)findViewById(R.id.text_cu);

        share_loc_button();

        frameweb.setVisibility(View.GONE);
        my_loc.setOnClickListener(this);
        get_loc.setOnClickListener(this);
        contact.setOnClickListener(this);
    }
    public static Criteria createFineCriteria() {

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setSpeedRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);
        return c;

    }
    public void contact_button()
    {
        my_loc1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.seo_32w,0,0);
        get_loc1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.place_32w,0,0);
        contact1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.call_32,0,0);
        my_loc1.setTextColor(Color.parseColor("#FFFFFF"));
        get_loc1.setTextColor(Color.parseColor("#FFFFFF"));
        contact1.setTextColor(Color.parseColor("#F68734"));
        contact1.setTypeface(Typeface.MONOSPACE);
        contact1.setTypeface(Typeface.DEFAULT_BOLD);
        contact1.setTextSize(14);

        my_loc1.setTypeface(Typeface.SERIF);
        my_loc1.setTypeface(Typeface.DEFAULT);
        my_loc1.setTextSize(13);

        get_loc1.setTypeface(Typeface.SERIF);
        get_loc1.setTypeface(Typeface.DEFAULT);
        get_loc1.setTextSize(13);
    }
    public void find_friend_button()
    {
        my_loc1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.seo_32w,0,0);
        get_loc1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.place_32,0,0);
        contact1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.call_32w,0,0);
        my_loc1.setTextColor(Color.parseColor("#FFFFFF"));
        get_loc1.setTextColor(Color.parseColor("#F68734"));
        contact1.setTextColor(Color.parseColor("#FFFFFF"));

        my_loc1.setTypeface(Typeface.SERIF);
        my_loc1.setTypeface(Typeface.DEFAULT);
        my_loc1.setTextSize(13);

        contact1.setTypeface(Typeface.SERIF);
        contact1.setTypeface(Typeface.DEFAULT);
        contact1.setTextSize(13);

        get_loc1.setTypeface(Typeface.MONOSPACE);
        get_loc1.setTypeface(Typeface.DEFAULT_BOLD);
        get_loc1.setTextSize(14);
    }
public void share_loc_button()
{
    my_loc1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.seo_32,0,0);
    get_loc1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.place_32w,0,0);
    contact1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.call_32w,0,0);
    my_loc1.setTextColor(Color.parseColor("#F68734"));
    get_loc1.setTextColor(Color.parseColor("#FFFFFF"));
    contact1.setTextColor(Color.parseColor("#FFFFFF"));

    get_loc1.setTypeface(Typeface.SERIF);
    get_loc1.setTypeface(Typeface.DEFAULT);
    get_loc1.setTextSize(13);

    contact1.setTypeface(Typeface.SERIF);
    contact1.setTypeface(Typeface.DEFAULT);
    contact1.setTextSize(13);

    my_loc1.setTypeface(Typeface.MONOSPACE);
    my_loc1.setTypeface(Typeface.DEFAULT_BOLD);
    my_loc1.setTextSize(14);
}
    @Override
    protected void onRestart() {
        super.onRestart();
       if(a==0)
       {
           share_loc_button();
       }
        else if(a==1)
       {
           find_friend_button();
       }
        else if(a==2)
       {
           contact_button();
       }
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            buildAlertMessageNoGps();
        }
        else {
                network();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    //==========================================================Finding Location==========================
    public void network() {

        pDialog.setCancelable(false);
        pDialog.setMessage("Finding Location...");
        pDialog.show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String time =preference.get_location_refresh();
        Long time2= Long.valueOf(time)*1000;

        locationManager.requestLocationUpdates(high.getName(), time2, 20, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time2, 20, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time2, 20, this);

        lastKnownLocation_byNetwork =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        lastKnownLocation_byGps =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastKnownLocation_byGps == null) {
//            Log.e("gps last location no", "");
            if (lastKnownLocation_byNetwork == null)
            {
//                Log.e("network last loca no", "");
            }
            else
            {
                onLocationChanged(lastKnownLocation_byNetwork);
//                Log.e("Network Location:","" + lastKnownLocation_byNetwork.toString());
            }
        } else {
            if (lastKnownLocation_byNetwork == null)
            {
//                Log.e("network not found","network");
                pDialog.dismiss();
                network();
            }
            else
            {
                    if (lastKnownLocation_byGps.getTime() > lastKnownLocation_byNetwork.getTime()) {
//                        Log.e("gps time",""+lastKnownLocation_byGps.getTime());
//                        Log.e("gps time",""+lastKnownLocation_byNetwork.getTime());
//                        Log.e("gps accuracy",""+lastKnownLocation_byGps.getAccuracy());
//                        Log.e("network accuracy",""+lastKnownLocation_byNetwork.getAccuracy());
//                        Log.e("My Location from GPS","" + lastKnownLocation_byGps.getLatitude() +" : " + lastKnownLocation_byGps.getLongitude());
                        if(lastKnownLocation_byGps.getAccuracy()<lastKnownLocation_byNetwork.getAccuracy())
                        {
                            onLocationChanged(lastKnownLocation_byGps);
                        }
                        else
                        {
                            onLocationChanged(lastKnownLocation_byNetwork);
                        }
                    }
                else {
//                    Log.e("gps time",""+lastKnownLocation_byGps.getTime());
//                    Log.e("gps time",""+lastKnownLocation_byNetwork.getTime());
//                    Log.e("My Location Network","" + lastKnownLocation_byNetwork.getLatitude() +" : " + lastKnownLocation_byNetwork.getLongitude());
//                    Log.e("gps accuracy",""+lastKnownLocation_byGps.getAccuracy());
//                    Log.e("network accuracy",""+lastKnownLocation_byNetwork.getAccuracy());
                        if(lastKnownLocation_byGps.getAccuracy()<lastKnownLocation_byNetwork.getAccuracy())
                        {
                            onLocationChanged(lastKnownLocation_byGps);
                        }
                        else
                        {
                            onLocationChanged(lastKnownLocation_byNetwork);
                        }
                }

            }
        }

    }

    //==========================================================Share Location with other==========================
    public void share_data() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, preference.get_loc_text_share());
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Via"));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("changed location", "" + location.toString());

        this.location=location;
        pDialog.dismiss();
        if((preference.get_loc_lat().equalsIgnoreCase("null"))&&(preference.get_loc_lat().equalsIgnoreCase("null"))) {
            share_location();
        }
        else
        {
            double last_lati= Double.parseDouble(preference.get_loc_lat());
            double last_longi= Double.parseDouble(preference.get_loc_long());

            distFrom(last_lati, last_longi, location.getLatitude(), location.getLongitude());
            if(dist>preference.get_distance_meters())
            {
                share_location();
            }
            else {
                Date curDate = new Date(System.currentTimeMillis());
                Date myDate = new Date(preference.get_date());
                final int MILLI_TO_HOUR = 1000 * 60 * 60;
                long i=curDate.getTime()-myDate.getTime();
                long j=i/MILLI_TO_HOUR;
                int seconds = (int) ((i / 1000) % 60);
                int minutes = (int) ((i / 1000) / 60);
                Log.e("total hour",""+j+" "+minutes+" "+seconds);
                if(minutes>50)
                {
                    share_location();
                }
                else
                {
                    editcode.setText(preference.get_loc_code());
                    loaddata(preference.get_url());
                }
            }
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    //==========================================================Click Listener on Button==========================

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.my:
                a=0;
                share_loc_button();
                frameweb.setVisibility(View.GONE);
                if(frameLayout.getVisibility()==View.GONE)
                {
                    frameLayout.setVisibility(View.VISIBLE);
                    web.setVisibility(View.VISIBLE);
                }
                share_location();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.contain1, new My_Location());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case R.id.get:
                a=1;
                find_friend_button();
                frameweb.setVisibility(View.GONE);
                if(frameLayout.getVisibility()==View.GONE)
                {
                    frameLayout.setVisibility(View.VISIBLE);
                    web.setVisibility(View.VISIBLE);
                }

                share_location();
                android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_right);
                fragmentTransaction1.replace(R.id.contain1, new Get_Location());
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

                break;
            case R.id.contact_us:
                a=2;
                contact_button();
                frameweb.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                web.setVisibility(View.GONE);

                android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
                fragmentTransaction2.replace(R.id.webcode, new Contact_us());
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();

                break;
        }
    }

    //==========================================================GPS Not Enable Message==========================
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Disabled");
        builder.setMessage(preference.get_gps_string())
                .setCancelable(false)
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


//==========================================================Load Data on Web View==========================
    public void loaddata(final String url1) {
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setBuiltInZoomControls(true);
if(a==2) {

}
        else {
//    pDialog = new ProgressDialog(MainActivity.this);
    pDialog.setMessage("Please wait ...");
    pDialog.show();
}
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
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            }

        });


        web.loadUrl(url1);

    }



//==========================================================Web Service hit==========================

    public void share_location() {
        try {
            Log.e("hit main","web code");
            pDialog.dismiss();
            if(a==2) {

            }
            else {
//                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.show();
            }
            StringRequest sr = new StringRequest(Request.Method.POST, "http://erginus.net/buddyfinder_web/api/location", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        if (serverCode.equalsIgnoreCase("0")) {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, serverMessage, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject obj = object.getJSONObject("data");
                                            my_code = obj.getString("location_code");
                                            url2 = obj.getString("location_url");
                                    preference.set_url(url2);
                                    Date curDate = new Date(System.currentTimeMillis());
//                                    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");
//                                    String DateToStr = format.format(curDate);

                                    long millis = curDate.getTime();
                                    Log.e("Time",""+millis);
                                    preference.set_date(millis);
                                    share_code = obj.getString("android_sharing_text");
                                    preference.set_loc_text_share(share_code);
                                    preference.set_loc_code(my_code);
                                    preference.set_loc_lat(location.getLatitude());
                                    preference.set_loc_long(location.getLongitude());

                                    if(a==0)
                                    {
                                        editcode.setText(my_code);
                                        editcode.setFocusable(false);
                                        editcode.setClickable(false);
                                    }
                                            loaddata(url2);
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

                    params.put("location_latitude",""+location.getLatitude());
                    params.put("location_longitude",""+location.getLongitude());
                    params.put("location_code",preference.get_loc_code());
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



    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Alert !")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        Intent a = new Intent(Intent.ACTION_MAIN);
//                        a.addCategory(Intent.CATEGORY_HOME);
//                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(a);

//                        stopService(new Intent(MainActivity.this, Background_Location_Service.class));
                        ExitActivity.exitApplication(MainActivity.this);

                        //finish();
                        //LoginActivity.super.onBackPressed();
                    }
                }).create().show();
    }



    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
//        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);
        Location locationB = new Location("B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);
        dist = (int) locationA.distanceTo(locationB);
        Log.e("distance",""+dist);
        preference.set_curr_dist(dist);
        return dist;

    }



}
