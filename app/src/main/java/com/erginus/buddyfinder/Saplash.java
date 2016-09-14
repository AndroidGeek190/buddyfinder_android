package com.erginus.buddyfinder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Movie;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.erginus.buddyfinder.Common.Background_Location_Service;
import com.erginus.buddyfinder.Common.ConnectionDetector;
import com.erginus.buddyfinder.Common.SharedPreference;
import com.erginus.buddyfinder.Common.VolleySingleton;
import com.erginus.buddyfinder.Intro.Introduction;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nazer on 5/11/2016.
 */
public class Saplash extends AppCompatActivity {
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    LocationManager locationManager;
    ProgressDialog dialog;
    String version,android_url,gps_string;
    SharedPreference preference;
    AlertDialog.Builder builder,alert,builder2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
preference=new SharedPreference(Saplash.this);
        locationManager = (LocationManager) Saplash.this.getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        cd = new ConnectionDetector(getApplicationContext());
        internet();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
        internet();
    }

    public void internet()
    {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
//            if (!isGPSEnabled)
//            {
////                Log.e("gps not", ""+isGPSEnabled);
//                buildAlertMessageNoGps();
//            }
//            else {
                version();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
//                            Intent intent = new Intent(Saplash.this, Introduction.class);
//                            startActivity(intent);
//                            finish();


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
//            }

        } else {
            showAlertDialog(Saplash.this, "No internet connection",
                    "Internet is not working, please check.", false);
        }

    }
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setTitle(title);
        alert.setCancelable(false);
        alert.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        internet();
                    }

                }

        );
        AlertDialog dialog_card = alert.create();
        dialog_card.getWindow().setGravity(Gravity.BOTTOM);
        dialog_card.show();

    }

    private void version_code() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("App Version");
        builder.setMessage("Please update your app First.")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);

                    }
                });
        final AlertDialog alert = builder.create();
        alert.getWindow().setGravity(Gravity.BOTTOM);
        alert.show();
    }


    private void buildAlertMessageNoGps() {
       builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Location Services Disabled");
//        builder2.setMessage("GPS on this phone might be disabled, please check once and enable it, for the app to serve better.")
        builder2.setMessage(gps_string)
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                });
        final AlertDialog alert = builder2.create();
        alert.getWindow().setGravity(Gravity.BOTTOM);
        alert.show();
    }
public void version()
{
    try {
        final ProgressDialog pDialog = new ProgressDialog(Saplash.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e("Web service hit","web service hit");
        StringRequest sr = new StringRequest(Request.Method.POST, "http://erginus.net/buddyfinder_web/api/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
//                Log.e("response=","" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    String serverCode = object.getString("code");
                    String serverMessage = object.getString("message");
                    if (serverCode.equalsIgnoreCase("0")) {
                        Toast.makeText(Saplash.this, ""+serverMessage, Toast.LENGTH_SHORT).show();
                    }
                    if (serverCode.equalsIgnoreCase("1")) {
                        try {

                            if ("1".equals(serverCode)) {
                                JSONObject obj = object.getJSONObject("data");
                                version = obj.getString("android_version");
                                android_url = obj.getString("android_url");
                                preference.set_share_url(android_url);
                                gps_string=obj.getString("gps_error");
                                preference.set_gps_string(gps_string);

                                String loc_Refresh=obj.getString("location_refresh");
                                int distance_meters=obj.getInt("distance_meters");
                                String view_refresh=obj.getString("route_refresh");
                                preference.set_location_refresh(loc_Refresh);
                                preference.set_view_refresh(view_refresh);
                                preference.set_distance_meters(distance_meters);
                                if (!isGPSEnabled)
                                {
                                    buildAlertMessageNoGps();
                                }
                                else {
                                    String getcode = preference.get_version_code();

                                    if (getcode.equalsIgnoreCase("start")) {
                                        preference.set_version_code(version);
                                        Intent intent = new Intent(Saplash.this, Introduction.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (version.equalsIgnoreCase(getcode)) {
                                        Intent intent = new Intent(Saplash.this, Introduction.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        version_code();
                                    }

                                }

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
                    Toast.makeText(Saplash.this, "Bad Internet Connection",
                            Toast.LENGTH_LONG).show();
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

                params.put("current_timestamp","");
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

