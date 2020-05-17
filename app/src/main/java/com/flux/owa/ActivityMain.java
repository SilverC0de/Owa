package com.flux.owa;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

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



//
//        new ShowcaseView.Builder(this)
//                .setTarget(new ActionViewTarget(this, ActionViewTarget.Type.HOME))
//                .setContentTitle("Home")
//                .setContentText("Where us the home button")
//                .hideOnTouchOutside()
//                .build();


        initializeIntro();


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

    private void initializeIntro() {
        boolean tutured = data.getBoolean(XClass.tutured, false);

        if (!tutured) {
            ShowcaseView e = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(home))
                    .setContentTitle("Apartments")
                    .setContentText("Available apartments will be displayed here")
                    .hideOnTouchOutside()
                    .setStyle(R.style.HighlightTheme)
                    .build();
            e.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                @Override
                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                    ShowcaseView e = new ShowcaseView.Builder(ActivityMain.this)
                            .setTarget(new ViewTarget(favourite))
                            .setContentTitle("Favourites")
                            .setContentText("Apartments you like will be displayed here")
                            .hideOnTouchOutside()
                            .setStyle(R.style.HighlightTheme)
                            .build();
                    e.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                        @Override
                        public void onShowcaseViewHide(ShowcaseView showcaseView) {
                            ShowcaseView e = new ShowcaseView.Builder(ActivityMain.this)
                                    .setTarget(new ViewTarget(chat))
                                    .setContentTitle("Inbox")
                                    .setContentText("You can chat with our agents about houses you like")
                                    .hideOnTouchOutside()
                                    .setStyle(R.style.HighlightTheme)
                                    .build();
                            e.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                                @Override
                                public void onShowcaseViewHide(ShowcaseView showcaseView) {

                                }

                                @Override
                                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                                }

                                @Override
                                public void onShowcaseViewShow(ShowcaseView showcaseView) {

                                }

                                @Override
                                public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                                }
                            });
                        }

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                        }

                        @Override
                        public void onShowcaseViewShow(ShowcaseView showcaseView) {

                        }

                        @Override
                        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                        }
                    });
                }

                @Override
                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                }

                @Override
                public void onShowcaseViewShow(ShowcaseView showcaseView) {

                }

                @Override
                public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                }
            });
        }
    } //don't open, its a long boring boilerplate lines of codes, it's boring
}
