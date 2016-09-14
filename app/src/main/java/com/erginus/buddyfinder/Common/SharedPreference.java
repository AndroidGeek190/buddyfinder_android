package com.erginus.buddyfinder.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Date;

/**
 * Created by nazer on 11/4/2015.
 */
public class SharedPreference {
    private Context context;
    public static SharedPreferences preferences;

    public SharedPreference(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context getContext() {
        return context;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public int get_curr_dist() {

        return getPreferences().getInt("current_distance", 0);
    }

    public void set_curr_dist(int code) {
        Editor edit = getPreferences().edit();
        edit.putInt("current_distance", code);
        edit.commit();
    }




    public String get_loc_text_share() {

        return getPreferences().getString("text_code_share", "null");
    }

    public void set_loc_text_share(String code) {
        Editor edit = getPreferences().edit();
        edit.putString("text_code_share", code);
        edit.commit();
    }



    public String get_loc_code() {

        return getPreferences().getString("Location_code", "null");
    }

    public void set_loc_code(String code) {
        Editor edit = getPreferences().edit();
        edit.putString("Location_code", code);
        edit.commit();

    }
    public String get_version_code() {

        return getPreferences().getString("ver_code", "start");
    }

    public void set_version_code(String code) {
        Editor edit = getPreferences().edit();
        edit.putString("ver_code", code);
        edit.commit();

    }

    public String get_share_url() {

        return getPreferences().getString("url_share", "null");
    }

    public void set_share_url(String code) {
        Editor edit = getPreferences().edit();
        edit.putString("url_share", code);
        edit.commit();

    }



    public String get_location_refresh() {

        return getPreferences().getString("loc_refresh", "null");
    }

    public void set_location_refresh(String code) {
        Editor edit = getPreferences().edit();
        edit.putString("loc_refresh", code);
        edit.commit();

    }

    public int get_distance_meters() {

        return getPreferences().getInt("distance_meters", 0);
    }

    public void set_distance_meters(int code) {
        Editor edit = getPreferences().edit();
        edit.putInt("distance_meters", code);
        edit.commit();
    }

    public String get_gps_string() {

        return getPreferences().getString("gps_error", "null");
    }

    public void set_gps_string(String code) {
        Editor edit = getPreferences().edit();
        edit.putString("gps_error", code);
        edit.commit();
    }


    public String get_view_refresh() {

        return getPreferences().getString("view_refresh", "start");
    }

    public void set_view_refresh(String code) {
        Editor edit = getPreferences().edit();
        edit.putString("view_refresh", code);
        edit.commit();
    }

    public String get_loc_lat() {

        return getPreferences().getString("Location_lat", "null");
    }

    public void set_loc_lat(double code) {
        Editor edit = getPreferences().edit();
        edit.putString("Location_lat", String.valueOf(code));
        edit.commit();

    }
    public String get_loc_long() {

        return getPreferences().getString("Location_long", "null");
    }

    public void set_loc_long(double code) {
        Editor edit = getPreferences().edit();
        edit.putString("Location_long", String.valueOf(code));
        edit.commit();

    }

    public String get_url() {

        return getPreferences().getString("url", "null");
    }

    public void set_url(String code) {
        Editor edit = getPreferences().edit();
        edit.putString("url",code);
        edit.commit();

    }
    public Long get_date() {

        return getPreferences().getLong("time", 0);
    }

    public void set_date(Long code) {
        Editor edit = getPreferences().edit();
        edit.putLong("time", code);
        edit.commit();

    }


    public void cleardata() {
        Editor editor = preferences.edit();
        // To delete all.
        editor.clear();
        // Delete based on key
        //editor.remove("username");
        editor.commit();
    }

}


