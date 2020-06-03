package com.flux.owa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityAccess extends XActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        new Handler().post(()->{
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorThemeLite));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBar));
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentLogin()).commit();
    }
}