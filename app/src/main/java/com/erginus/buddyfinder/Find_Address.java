package com.erginus.buddyfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
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
import com.erginus.buddyfinder.Common.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nazer on 8/9/2016.
 */
public class Find_Address extends AppCompatActivity {
ListView listView;
    AutoCompleteTextView text_type;
    List<String> address_id_list,address_list;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_place);
        address_list=new ArrayList();
        address_id_list=new ArrayList();
        listView=(ListView)findViewById(R.id.list_address);
        text_type=(AutoCompleteTextView)findViewById(R.id.edit_search2);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.cord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true); // show or hide the default home button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
            getSupportActionBar().setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(text_type.getText().toString().equalsIgnoreCase(""))
                {

                }
                else {
                    textchanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                text_type.setThreshold(1);
            }
        });
    }
    public void textchanged()
    {
        address_id_list.clear();
        address_list.clear();
        try {
            Log.e("hit main","web code");

            StringRequest sr = new StringRequest(Request.Method.POST, "http://erginus.net/buddyfinder_web/api/search", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        if (serverCode.equalsIgnoreCase("0")) {
                            listView.setVisibility(View.GONE);
                           // Toast.makeText(Find_Address.this, serverMessage, Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, serverMessage, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                listView.setVisibility(View.VISIBLE);
                                if ("1".equals(serverCode)) {
                                    JSONArray array = object.getJSONArray("data");
//                                    address_id_list.clear();
//                                    address_list.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject index0 = array.optJSONObject(i);
                                       String add_id = index0.optString("location_code");
                                       String add_name = index0.optString("location_value");
                                        address_id_list.add(add_id);
                                        address_list.add(add_name);

                                    }
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Find_Address.this, R.layout.address_layout,R.id.text_ad, address_list);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        InputMethodManager imm = (InputMethodManager)Find_Address.this.getSystemService(
                                                        Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(text_type.getWindowToken(), 0);
                                        Log.e("index and refernce", "" + position + "  " + address_id_list.get(position));
                                        Intent intent2 = new Intent(Find_Address.this, Direction.class);
                                        intent2.putExtra("loccode", "" + address_id_list.get(position));
                                        startActivity(intent2);
                                    }
                                });
                                //text_type.setAdapter(adapter);
                               // text_type.setThreshold(1);
//                                text_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        String placeid = address_id_list.get(position);
//                                        Log.e("index and refernce", "" + position + "  " + placeid);
//                                        text_type.setText("");
//                                        InputMethodManager imm = (InputMethodManager)Find_Address.this.getSystemService(
//                                                        Context.INPUT_METHOD_SERVICE);
//                                        imm.hideSoftInputFromWindow(text_type.getWindowToken(), 0);
//
//                                    }
//                                });


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

                    params.put("query",""+text_type.getText());
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
