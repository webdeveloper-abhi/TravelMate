package com.abhicoder.journeymate.PreferenceManager;


import android.content.Context;
import android.content.SharedPreferences;

import com.abhicoder.journeymate.DatabaseConstant.Constants;


public class PreferenceManager {

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context){
        sharedPreferences=context.getSharedPreferences(Constants.KEY_PreferenceName,Context.MODE_PRIVATE);
    }

    public void putboolean(String key,Boolean value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public Boolean getboolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public void putString(String key,String value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,null);
    }

    public  void clear(){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}


