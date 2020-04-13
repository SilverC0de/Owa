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

    Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);
        final EditText find_mail = findViewById(R.id.access_mail);
        ImageView find_close = findViewById(R.id.access_close);
        proceed = findViewById(R.id.access_continue);

        find_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityAccess.super.onBackPressed();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = find_mail.getText().toString().trim();
                if (mail.isEmpty()){
                    Toast.makeText(ActivityAccess.this, "Please enter a valid mail address", Toast.LENGTH_SHORT).show();
                } else {
                    authUser(mail);
                }
            }
        });
    }

    void authUser(final String mail){
        proceed.setBackgroundColor(getResources().getColor(R.color.colorShadow));
        proceed.setEnabled(false);
        proceed.setText("Please Wait");

        RequestBody body = new FormBody.Builder().add("mail", mail).build();
        Request request = new Request.Builder().url(XClass.apiAuthUser + "?mail=" + mail).post(body).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        proceed.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                        proceed.setEnabled(true);
                        proceed.setText("Continue");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string().trim();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("silvr", "" + result);
                        if (result.equals("00")){
                            SharedPreferences.Editor e = data.edit();
                            e.putString(XClass.mail, mail);
                            e.apply();
                            startActivity(new Intent(getApplicationContext(), ActivityTIPI.class));
                            finish();
                        } else {
                            proceed.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                            proceed.setEnabled(true);
                            proceed.setText("Continue");
                            Toast.makeText(ActivityAccess.this, result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}