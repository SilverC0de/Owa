package com.flux.owa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.tmall.ultraviewpager.UltraViewPager;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentDetails extends XFragment {


    private UltraViewPager pager;
    private LottieAnimationView loading;
    private String locale = null;


    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, parent, false);

        loading = view.findViewById(R.id.loading);
        ImageView close = view.findViewById(R.id.close);
        SparkButton fav = view.findViewById(R.id.list_save);
        Button reserve = view.findViewById(R.id.reserve);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
            }
        });

        fav.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {

                //i
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                //set checked
            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {
                OkHttpClient httpClient = new OkHttpClient();
                SharedPreferences data = cx.getSharedPreferences(XClass.sharedPreferences, Context.MODE_PRIVATE);
                String mail = data.getString(XClass.mail, null);

                Request request = new Request.Builder().url(XClass.apiAddToFavourites + "?mail=" + mail + "&locale=" + locale).build();
                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                //saved
                            }
                        });
                    }
                });
            }
        });

        Bundle bnd = getArguments();
        if (bnd != null) locale = bnd.getString("locale");

        boolean tutured = data.getBoolean(XClass.tutured, false);
        if (!tutured){
            ShowcaseView e = new ShowcaseView.Builder(fx)
                    .setContentTitle("Apartment")
                    .setContentText("Scroll down to view apartment amenities or request for a tour")
                    .hideOnTouchOutside()
                    .setStyle(R.style.HighlightTheme)
                    .build();
            e.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                @Override
                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                    SharedPreferences.Editor e = data.edit();
                    e.putBoolean(XClass.tutured, true);
                    e.apply();
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
        initializeDetails(view, locale);
        return view;
    }

    private void initializeDetails(final View view, final String locale){
        OkHttpClient httpClient = new OkHttpClient();

        final RequestBody body = new FormBody.Builder().add("", "").build();

        Request request = new Request.Builder().url(XClass.apiDetails + "?locale=" + locale.trim()).post(body).build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(fx, "Unable to fetch details for listing", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response stat) throws IOException {
                @SuppressWarnings("ConstantConditions")
                final String response = stat.body().string();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loading.setVisibility(View.GONE);
                            int cost = 0;
                            String check = "1";
                            String xhint = XClass.outcast;
                            String ximage = XClass.outcast;
                            String xName = XClass.outcast;
                            String xAgent = XClass.outcast;
                            String xAgentName = XClass.outcast;



                            final JSONObject object = new JSONObject(response);

                            final String individuals = object.getString("guest");

                            pager = view.findViewById(R.id.details_pager);
                            if (getArguments() != null) ximage = getArguments().getString("image");
                            initializeImages(locale, ximage);



                            TextView name = view.findViewById(R.id.details_name);
                            TextView price = view.findViewById(R.id.details_price);

                            TextView guest = view.findViewById(R.id.details_guest);
                            TextView bed = view.findViewById(R.id.details_bed);
                            TextView room = view.findViewById(R.id.details_room);
                            TextView bath = view.findViewById(R.id.details_bath);
                            TextView hint = view.findViewById(R.id.details_hint);

                            TextView details = view.findViewById(R.id.details_info);

                            Button chat = view.findViewById(R.id.chat);
                            Button vr = view.findViewById(R.id.vr);
                            Button reserve = view.findViewById(R.id.reserve);

                            ImageView cabletv = view.findViewById(R.id.check_cabletv);
                            ImageView water = view.findViewById(R.id.check_water);
                            ImageView iron = view.findViewById(R.id.check_iron);
                            ImageView work = view.findViewById(R.id.check_work);
                            ImageView tv = view.findViewById(R.id.check_tv);
                            ImageView wifi = view.findViewById(R.id.check_wifi);
                            ImageView park = view.findViewById(R.id.check_park);
                            ImageView kitchen = view.findViewById(R.id.check_kitchen);
                            ImageView silverware = view.findViewById(R.id.check_silverware);
                            ImageView microwave = view.findViewById(R.id.check_microwave);
                            ImageView fridge = view.findViewById(R.id.check_fridge);
                            ImageView gas = view.findViewById(R.id.check_gas);


                            boolean bool_cabletv = Objects.equals(object.getString("cabletv"), check);
                            boolean bool_water = Objects.equals(object.getString("water"), check);
                            boolean bool_iron = Objects.equals(object.getString("iron"), check);
                            boolean bool_work = Objects.equals(object.getString("work"), check);
                            boolean bool_tv = Objects.equals(object.getString("tv"), check);
                            boolean bool_wifi = Objects.equals(object.getString("wifi"), check);
                            boolean bool_park = Objects.equals(object.getString("park"), check);
                            boolean bool_kitchen = Objects.equals(object.getString("kitchen"), check);
                            boolean bool_silverware = Objects.equals(object.getString("silverware"), check);
                            boolean bool_microwave = Objects.equals(object.getString("microwave"), check);
                            boolean bool_fridge = Objects.equals(object.getString("fridge"), check);
                            boolean bool_gas = Objects.equals(object.getString("gas"), check);
                            boolean isTaken = Objects.equals(object.getString("taken"), check);

                            String xguest = object.getString("guest").equals("1") ? " Guest" : " Guests";
                            String xbed = object.getString("bed").equals("1") ? " Bed space" : " Bed spaces";
                            String xroom = object.getString("room").equals("1") ? " Room" : " Rooms";
                            String xbath = object.getString("bath").equals("1") ? " Bath" : " Baths";

                            if (getArguments() != null) cost = getArguments().getInt("cost");
                            if (getArguments() != null) xhint = getArguments().getString("hint");
                            if (getArguments() != null) xName = getArguments().getString("name");
                            if (getArguments() != null) xAgent = getArguments().getString("agent");
                            if (getArguments() != null) xAgentName = getArguments().getString("agent_name");


                            String xprice = cx.getResources().getString(R.string.naira) + NumberFormat.getNumberInstance(Locale.ENGLISH).format(cost) + "/Month";


                            name.setText(xName);
                            price.setText(xprice);
                            hint.setText(xhint);

                            guest.setText(String.format("%s%s", object.getString("guest"), xguest));
                            bed.setText(String.format("%s%s", object.getString("bed"), xbed));
                            room.setText(String.format("%s%s", object.getString("room"), xroom));
                            bath.setText(String.format("%s%s", object.getString("bath"), xbath));

                            details.setText(object.getString("details"));

                            if (bool_cabletv) cabletv.setImageResource(R.drawable.check);
                            if (bool_water) water.setImageResource(R.drawable.check);
                            if (bool_iron) iron.setImageResource(R.drawable.check);
                            if (bool_work) work.setImageResource(R.drawable.check);
                            if (bool_tv) tv.setImageResource(R.drawable.check);
                            if (bool_wifi) wifi.setImageResource(R.drawable.check);
                            if (bool_park) park.setImageResource(R.drawable.check);
                            if (bool_kitchen) kitchen.setImageResource(R.drawable.check);
                            if (bool_silverware) silverware.setImageResource(R.drawable.check);
                            if (bool_microwave) microwave.setImageResource(R.drawable.check);
                            if (bool_fridge) fridge.setImageResource(R.drawable.check);
                            if (bool_gas) gas.setImageResource(R.drawable.check);

                            ////////////////////////////////////////////////////////////////////////////




                            final String finalXName = xName;
                            final String finalXAgent = xAgent; //agent mail
                            final String finalXAgentName = xAgentName; //agent name
                            final String finalXhint = xhint; //city

                            chat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Realm localeRealm = Realm.getDefaultInstance();
                                    final String chat_head = finalXAgentName + " is in charge of this apartment";

                                    String mail = data.getString(XClass.mail, null);

                                    final List<String> chatters = new ArrayList<>();
                                    chatters.add(mail);
                                    chatters.add(finalXAgent);

                                    final XHeads heads = new XHeads();
                                    heads.setName(finalXName);
                                    heads.setDetails(chat_head);
                                    heads.setLink(locale);

                                    localeRealm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(@NotNull Realm realm) {
                                            realm.copyToRealmOrUpdate(heads);
                                        }
                                    });
                                    localeRealm.close();
                                    FragmentChat inquire = new FragmentChat();
                                    Bundle xnd = new Bundle();
                                    xnd.putString("title", finalXName);
                                    xnd.putString("header", chat_head);
                                    xnd.putString("locale", locale);
                                    xnd.putString("agent", finalXAgent);

                                    inquire.setArguments(xnd);
                                    fm.beginTransaction().add(R.id.fragment, inquire).addToBackStack(null).commit();
                                }
                            });

                            if (isTaken) {
                                reserve.setText("Taken");
                                reserve.setEnabled(false);
                                reserve.setTextColor(cx.getResources().getColor(R.color.colorText));
                                reserve.setBackgroundResource(R.drawable.button_off);
                            }
                            vr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FragmentVR fragmentVR = new FragmentVR();
                                    Bundle bnd = getArguments();
                                    bnd.putString("guest", individuals);
                                    fragmentVR.setArguments(bnd);
                                    fm.beginTransaction().add(R.id.fragment, fragmentVR).addToBackStack(null).commit();
                                }
                            });
                            reserve.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FragmentReserve reserve = new FragmentReserve();
                                    Bundle bnd = getArguments();
                                    bnd.putString("guest", individuals);
                                    reserve.setArguments(bnd);
                                    fm.beginTransaction().add(R.id.fragment, reserve).addToBackStack(null).commit();
                                }
                            });
                        } catch (JSONException i) {
                            loading.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }


    private void initializeImages(String ville, String index){
        ArrayList<String> img = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(XClass.apiImage + "?locale=" + ville).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                fx.runOnUiThread(() -> {
                    try {
                        JSONArray arr = new JSONArray(json);
                        if (arr.length() != 0){
                            for (int i = 0; i < arr.length(); i++){
                                JSONObject obj = arr.getJSONObject(i);
                                String path = obj.getString("path");
                                img.add(path);
                            }
                        }
                    } catch (JSONException ignored){
                        img.add(index); //add the default image if there are no images
                    } finally {
                        try {
                            XPager adapter = new XPager(fx, cx, img);
                            pager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                            pager.setBackgroundResource(0);
                            pager.setAdapter(adapter);
                            pager.initIndicator();
                            pager.getIndicator()
                                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                                    .setFocusColor(getResources().getColor(R.color.colorPrimary))
                                    .setNormalColor(getResources().getColor(R.color.colorLite))
                                    .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
                            pager.getIndicator().setMargin(0,0,0,20);
                            pager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                            pager.getIndicator().build();
                        } catch (IllegalStateException ignored){}
                    }
                });
            }
        });
    }
}