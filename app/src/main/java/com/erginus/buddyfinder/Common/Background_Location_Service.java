package com.erginus.buddyfinder.Common;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nazer on 5/17/2016.
 */
public class Background_Location_Service extends Service implements LocationListener {
    LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    double latitude;
    double longitude;
    Location location;

    String url2, my_code, temp_code;
    int a = 0;
    SharedPreference preference;

    public Background_Location_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
        Log.e("create service","service");
        preference=new SharedPreference(Background_Location_Service.this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // Perform your long running operations here.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        locationManager = (LocationManager) Background_Location_Service.this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 10, this);

//        Log.e("GPS Enabled", "GPS Enabled");

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 10, this);
//                Log.e("Network", "Network");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        Log.e("service net location", "" + location.toString());
//                        dialog.dismiss();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

//                        onLocationChanged(location);


                    }
                }
            }

        } else {
//            dialog.dismiss();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
//            onLocationChanged(location);
            Log.e("service Gps location", "" + location.toString());
        }

//        if (location != null) {
//            String code = preference.get_loc_code();
//            if (code.equalsIgnoreCase("null")) {
//                temp_code = "1";
//                share_location();
//            } else {
//                temp_code = code;
//                share_location();
//            }
//
//        }

    }

    @Override
    public void onLocationChanged(Location location) {
//        Log.e("changed location", "" + location.getLatitude() + "," + location.getLongitude());
        Toast.makeText(Background_Location_Service.this, "service "+location.toString(), Toast.LENGTH_SHORT).show();
        this.location=location;

//        if(location!=null) {
//
//            dialog.dismiss();
//        }


       // temp_code=preference.get_loc_code();
        share_location();

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

    public void share_location() {
        try {
//            dialog.dismiss();
//            final ProgressDialog pDialog = new ProgressDialog(Background_Location_Service.this);
//            pDialog.setMessage("Loading...");
//            pDialog.show();
            StringRequest sr = new StringRequest(Request.Method.POST, "http://erginus.net/buddyfinder_web/api/location", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    pDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject obj = object.getJSONObject("data");
                                    my_code = obj.getString("location_code");
                                    url2 = obj.getString("location_url");
                                    preference.set_loc_code(my_code);

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
//                    pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        //Toast.makeText(Background_Location_Service.this, "Timeout Error",Toast.LENGTH_LONG).show();
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


}