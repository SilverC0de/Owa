package com.flux.owa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import okhttp3.OkHttpClient;

public abstract class XActivity extends AppCompatActivity {

    SharedPreferences data;
    OkHttpClient httpClient;


    // two months, 2, lockdown,


    @Override
    protected void onResume() {
        super.onResume();

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBar));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getSharedPreferences(XClass.sharedPreferences, MODE_PRIVATE);

        httpClient = new OkHttpClient();
        init();
    }

    //what you need, hald way done, he got stuck somewhere, paxful api, send you bitcoin, from their wn paxful acounnt, linking to the website, come to website, edirect to paxful for payment,
    // one time auth, enter amount and leave his balance, one time linking, blockchain wallet on the website, (2 selling channel, blockchain and paxful), reduce the manual process,
    // paystack pays you instantly, webapp,


    void init(){
        String buildAPI = data.getString(XClass.buildAPI, XClass.buildAPI);
        if (!Objects.equals(XClass.buildAPI, buildAPI)){
            startActivity(new Intent(getApplicationContext(), ActivityBuild.class));
        }
    }
}
