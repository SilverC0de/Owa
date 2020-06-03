package com.flux.owa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivitySplash extends XActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().post(()->{
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorThemeLite));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBar));
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean inducted = data.getBoolean(XClass.induction, false);
                String mail = data.getString(XClass.mail, null);
                boolean okTested = data.getBoolean(XClass.okTested, false);

                if (!inducted)
                    startActivity(new Intent(getApplicationContext(), ActivityIntro.class));
                else if (mail == null)
                    startActivity(new Intent(getApplicationContext(), ActivityAccess.class));
                else if (!okTested)
                    startActivity(new Intent(getApplicationContext(), ActivityMain.class)); //tipi
                else
                    startActivity(new Intent(getApplicationContext(), ActivityMain.class));
                finish();
            }
        }, 2200);



    }
}