package com.flux.owa;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityMain extends XActivity {

    ImageView home, favourite, chat, account;
    TextView home_text, favourite_text, chat_text, account_text;
    LinearLayout navigation, chat_lay;
    FrameLayout notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        home = findViewById(R.id.home);
        favourite= findViewById(R.id.favourite);
        chat = findViewById(R.id.chat);
        account = findViewById(R.id.account);

        home_text = findViewById(R.id.home_text);
        favourite_text= findViewById(R.id.favourite_text);
        chat_text = findViewById(R.id.chat_text);
        chat_lay = findViewById(R.id.chat_lay);
        account_text = findViewById(R.id.account_text);

        navigation = findViewById(R.id.navigation);
        notification = findViewById(R.id.notification);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentHome()).commit();


        boolean isNotification = data.getBoolean(XClass.notification, false);
        if (isNotification){
            notification.setVisibility(View.VISIBLE);
        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentHome()).commit();

                home.setImageResource(R.drawable.home_selected);
                favourite.setImageResource(R.drawable.love);
                chat.setImageResource(R.drawable.chat);
                account.setImageResource(R.drawable.account);

                home.setBackgroundColor(getResources().getColor(R.color.colorBar));
                favourite.setBackgroundColor(Color.parseColor("#00000000"));
                chat.setBackgroundColor(Color.parseColor("#00000000"));
                account.setBackgroundColor(Color.parseColor("#00000000"));

                home_text.setTextColor(getResources().getColor(R.color.colorAccent));
                favourite_text.setTextColor(getResources().getColor(R.color.colorText));
                chat_text.setTextColor(getResources().getColor(R.color.colorText));
                account_text.setTextColor(getResources().getColor(R.color.colorText));

                home_text.setBackgroundColor(getResources().getColor(R.color.colorBar));
                favourite_text.setBackgroundColor(Color.parseColor("#00000000"));
                chat_lay.setBackgroundColor(Color.parseColor("#00000000"));
                account_text.setBackgroundColor(Color.parseColor("#00000000"));

            }
        });
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentSaved()).commit();

                home.setImageResource(R.drawable.home);
                favourite.setImageResource(R.drawable.love_selected);
                chat.setImageResource(R.drawable.chat);
                account.setImageResource(R.drawable.account);

                home.setBackgroundColor(Color.parseColor("#00000000"));
                favourite.setBackgroundColor(getResources().getColor(R.color.colorBar));
                chat.setBackgroundColor(Color.parseColor("#00000000"));
                account.setBackgroundColor(Color.parseColor("#00000000"));

                home_text.setTextColor(getResources().getColor(R.color.colorText));
                favourite_text.setTextColor(getResources().getColor(R.color.colorAccent));
                chat_text.setTextColor(getResources().getColor(R.color.colorText));
                account_text.setTextColor(getResources().getColor(R.color.colorText));

                home_text.setBackgroundColor(Color.parseColor("#00000000"));
                favourite_text.setBackgroundColor(getResources().getColor(R.color.colorBar));
                chat_lay.setBackgroundColor(Color.parseColor("#00000000"));
                account_text.setBackgroundColor(Color.parseColor("#00000000"));

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentChatHeads()).commit();

                home.setImageResource(R.drawable.home);
                favourite.setImageResource(R.drawable.love);
                chat.setImageResource(R.drawable.chat_selected);
                account.setImageResource(R.drawable.account);

                home.setBackgroundColor(Color.parseColor("#00000000"));
                favourite.setBackgroundColor(Color.parseColor("#00000000"));
                chat.setBackgroundColor(getResources().getColor(R.color.colorBar));
                account.setBackgroundColor(Color.parseColor("#00000000"));

                home_text.setTextColor(getResources().getColor(R.color.colorText));
                favourite_text.setTextColor(getResources().getColor(R.color.colorText));
                chat_text.setTextColor(getResources().getColor(R.color.colorAccent));
                account_text.setTextColor(getResources().getColor(R.color.colorText));

                home_text.setBackgroundColor(Color.parseColor("#00000000"));
                favourite_text.setBackgroundColor(Color.parseColor("#00000000"));
                chat_lay.setBackgroundColor(getResources().getColor(R.color.colorBar));
                account_text.setBackgroundColor(Color.parseColor("#00000000"));

                notification.setVisibility(View.GONE);
                SharedPreferences.Editor e = data.edit();

                e.putBoolean(XClass.notification, false);
                e.apply();
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentAccount()).commit();

                home.setImageResource(R.drawable.home);
                favourite.setImageResource(R.drawable.love);
                chat.setImageResource(R.drawable.chat);
                account.setImageResource(R.drawable.account_selected);

                home.setBackgroundColor(Color.parseColor("#00000000"));
                favourite.setBackgroundColor(Color.parseColor("#00000000"));
                chat.setBackgroundColor(Color.parseColor("#00000000"));
                account.setBackgroundColor(getResources().getColor(R.color.colorBar));

                home_text.setTextColor(getResources().getColor(R.color.colorText));
                favourite_text.setTextColor(getResources().getColor(R.color.colorText));
                chat_text.setTextColor(getResources().getColor(R.color.colorText));
                account_text.setTextColor(getResources().getColor(R.color.colorAccent));

                home_text.setBackgroundColor(Color.parseColor("#00000000"));
                favourite_text.setBackgroundColor(Color.parseColor("#00000000"));
                chat_lay.setBackgroundColor(Color.parseColor("#00000000"));
                account_text.setBackgroundColor(getResources().getColor(R.color.colorBar));
            }
        });
    }
}
