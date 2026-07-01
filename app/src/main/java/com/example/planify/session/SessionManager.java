package com.example.planify.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context){

        prefs = context.getSharedPreferences(
                "PlanifySession",
                Context.MODE_PRIVATE);

        editor = prefs.edit();
    }

    public void saveUser(
            String id,
            String name,
            String email){

        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putBoolean("isLogin", true);

        editor.apply();
    }

    public boolean isLoggedIn(){
        return prefs.getBoolean("isLogin", false);
    }

    public String getUserId(){
        return prefs.getString("id","");
    }

    public String getUserName(){
        return prefs.getString("name","");
    }

    public String getEmail(){
        return prefs.getString("email","");
    }

    public void logout(){

        editor.clear();
        editor.apply();
    }
}