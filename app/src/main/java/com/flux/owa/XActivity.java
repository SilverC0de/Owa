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

    void init(){
        String buildAPI = data.getString(XClass.buildAPI, XClass.buildAPI);
        if (!Objects.equals(XClass.buildAPI, buildAPI)){
            startActivity(new Intent(getApplicationContext(), ActivityBuild.class));
        }
    }
}
