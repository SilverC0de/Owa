package com.flux.owa;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import co.paystack.android.PaystackSdk;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class XCore extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        Fresco.initialize(getApplicationContext());
        PaystackSdk.initialize(getApplicationContext());

        /////////////////////////////////////////////////////////////////////////


        OkHttpClient httpClient = new OkHttpClient();
        Request init = new Request.Builder().url("https://api.owanow.co/init.txt").build();
        httpClient.newCall(init).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String buildAPI = response.body().string().trim(); //buildAPI from server should be the same with app
                SharedPreferences data = getSharedPreferences(XClass.sharedPreferences, MODE_PRIVATE);
                SharedPreferences.Editor e = data.edit();

                e.putString(XClass.buildAPI, buildAPI);
                e.apply();
            }
        });

        ////////////////////////////////////////////////////////////////////////

        final String mail = getSharedPreferences(XClass.sharedPreferences, MODE_PRIVATE).getString(XClass.mail, XClass.outcast);
        Request request = new Request.Builder().url(XClass.apiNotification + "?mail=" + mail).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //get messages that are not yet read
                //push messages to chat

                Realm realmSend = Realm.getDefaultInstance();
                String json = response.body().string();
                Log.e("silvr", "-----" + json);

                try {
                    JSONArray array = new JSONArray(json);
                    if (array.length() != 0){
                        SharedPreferences data = getSharedPreferences(XClass.sharedPreferences, MODE_PRIVATE);
                        SharedPreferences.Editor e = data.edit();

                        e.putBoolean(XClass.notification, true);
                        e.apply();
                    }
                    for (int i = 0; i < array.length(); i++){

                        JSONObject obj = array.getJSONObject(i);
                        final String locale = obj.getString("locale");
                        String message = obj.getString("message");

                        final XChats chats = new XChats();
                        chats.setLocale(locale);
                        chats.setMessage(message);
                        chats.setType("agent");
                        chats.setStamp(System.currentTimeMillis());
                        realmSend.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(@NotNull Realm realm) {
                                realm.copyToRealm(chats);
                            }
                        });
                    }
                    realmSend.close();
                } catch (JSONException | IllegalArgumentException ignored) {}
            }
        });
    }
}