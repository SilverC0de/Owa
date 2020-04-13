package com.flux.owa;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentChat extends XFragment {

    private EditText message;
    private LinearLayout chat_view;
    private ScrollView chat_scroll;

    private String locale = XClass.outcast, xAgent;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_chat, parent, false);
        ImageView send = view.findViewById(R.id.send);
        ImageView close = view.findViewById(R.id.chat_close);
        TextView title = view.findViewById(R.id.chat_title);
        TextView header = view.findViewById(R.id.chat_header);

        Bundle bnd = getArguments();
        if (bnd != null){
            locale = bnd.getString("locale");

            String chat_title = bnd.getString("title");
            String chat_header = bnd.getString("header");
            xAgent = bnd.getString("agent");

            title.setText(chat_title);
            header.setText(chat_header);
        }

        message = view.findViewById(R.id.message);
        chat_scroll = view.findViewById(R.id.chat_scroll);
        chat_view = view.findViewById(R.id.chat_view);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat = message.getText().toString();
                message.getText().clear();

                if (!chat.isEmpty()) sendMessage(chat);
            }
        });

        initializeChats();
        initializeView(view);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
            }
        });
        return view;
    }

    ///////////////////////////////////////////////////////////////////

    private void sendMessage(String message){
        XClass.pushMessage(mail, message, locale, xAgent);

        TextView text_view = new TextView(cx);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.END;
        params.setMargins(10,4,10,4);

        text_view.setText(message);
        text_view.setMinWidth(160);
        text_view.setGravity(Gravity.CENTER);
        text_view.setTextColor(Color.WHITE);
        text_view.setGravity(Gravity.END);
        text_view.setPadding(16, 16, 16, 16);
        text_view.setBackgroundResource(R.drawable.message);
        chat_view.addView(text_view);
        text_view.setLayoutParams(params);


        Realm realmSend = Realm.getDefaultInstance();
        final XChats chats = new XChats();
        chats.setLocale(locale);
        chats.setMessage(message);
        chats.setType("user");
        chats.setStamp(System.currentTimeMillis());
        realmSend.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                realm.copyToRealm(chats);
            }
        });
        realmSend.close();
    } //display it

    private void receiveMessage(String message){
        TextView text_view = new TextView(cx);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.START;
        params.setMargins(10,4,10,4);

        text_view.setText(message);
        text_view.setMinWidth(160);
        text_view.setGravity(Gravity.CENTER);
        text_view.setTextColor(Color.BLACK);
        text_view.setGravity(Gravity.START);
        text_view.setPadding(16, 16, 16, 16);
        text_view.setBackgroundResource(R.drawable.button_off);
        text_view.setLayoutParams(params);

        chat_view.addView(text_view);

        Realm realmSend = Realm.getDefaultInstance();
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
        realmSend.close();

    } //display message from callback

    private void initializeChats(){

        Realm db = Realm.getDefaultInstance();
        final RealmResults<XChats> chats = db.where(XChats.class).equalTo("locale", locale).sort("stamp", Sort.ASCENDING).findAll();

        chats.load();

        db.beginTransaction();

        for (int i = 0; i < chats.size(); i++){
            String chat = Objects.requireNonNull(chats.get(i)).getMessage();
            String type = Objects.requireNonNull(chats.get(i)).getType();
            final String locale = Objects.requireNonNull(chats.get(i)).getLocale();

            if (type.equals("user")){
                TextView text_view = new TextView(cx);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.gravity = Gravity.END;
                params.setMargins(10,4,10,4);

                text_view.setText(chat);
                text_view.setMinWidth(160);
                text_view.setGravity(Gravity.CENTER);
                text_view.setTextColor(Color.WHITE);
                text_view.setGravity(Gravity.END);
                text_view.setPadding(16, 16, 16, 16);
                text_view.setBackgroundResource(R.drawable.message);
                chat_view.addView(text_view);
                text_view.setLayoutParams(params);

                //scroll to last
            } else {
                TextView text_view = new TextView(cx);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.gravity = Gravity.START;
                params.setMargins(10,4,10,4);

                text_view.setText(chat);
                text_view.setMinWidth(160);
                text_view.setGravity(Gravity.CENTER);
                text_view.setTextColor(Color.WHITE);
                text_view.setGravity(Gravity.START);
                text_view.setPadding(16, 16, 16, 16);
                text_view.setBackgroundResource(R.drawable.edit);
                chat_view.addView(text_view);
                text_view.setLayoutParams(params);
            }
        }
        db.close();
    } //the saved chats from realm db

    private void initializeView(View v){
        final ImageView apartment = v.findViewById(R.id.chat_locale);

        apartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get details online
                //thats the littile info icon
                final ProgressBar progress = v.findViewById(R.id.progress);
                progress.setVisibility(View.VISIBLE);
                apartment.setVisibility(View.GONE);
                Request request = new Request.Builder().url(XClass.apiApartment + "?locale=" + locale).build();

                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                                apartment.setVisibility(View.VISIBLE);

                                Toast.makeText(face, "Internal server error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                                apartment.setVisibility(View.VISIBLE);
                            }
                        });

                        try {
                            JSONObject obj = new JSONObject(response.body().string());
                            FragmentDetails details = new FragmentDetails();
                            Bundle bnd = new Bundle();

                            bnd.putInt("cost", obj.getInt("price"));
                            bnd.putString("name", obj.getString("name"));
                            bnd.putString("city", obj.getString("city"));
                            bnd.putString("hint", obj.getString("hint"));
                            bnd.putString("image", obj.getString("image"));

                            bnd.putString("locale", locale);
                            details.setArguments(bnd);
                            fm.beginTransaction().add(R.id.fragment, details).addToBackStack(null).commit();
                        } catch (JSONException | NumberFormatException ignored) { }
                    }
                });
            }
        });

    } //clicks, apartments and many many rubbish

}