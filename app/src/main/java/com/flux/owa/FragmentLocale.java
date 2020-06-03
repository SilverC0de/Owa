package com.flux.owa;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tmall.ultraviewpager.UltraViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentLocale extends XFragment {

    RelativeLayout empty;
    UltraViewPager pager;
    LottieAnimationView loading;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_locale, child, false);

        ImageView back = view.findViewById(R.id.back);

        empty = view.findViewById(R.id.empty);
        pager = view.findViewById(R.id.locale_pager);
        loading = view.findViewById(R.id.loading);


        back.setOnClickListener(v->fm.popBackStack());
        initializeOwa();
        return view;
    }


    private void initializeOwa(){
        ArrayList<HashMap<String, String>> img = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, XClass.apiBooked, response ->{
            loading.setVisibility(View.GONE);
            try {
                JSONArray arr = new JSONArray(response);
                if (arr.length() > 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        JSONObject obj = arr.getJSONObject(i);

                        String locale = obj.getString("locale");
                        String image = obj.getString("img");
                        String tag = obj.getString("tag");
                        String cost = obj.getString("cost");
                        String start = obj.getString("amount_paid");
                        String end = obj.getString("amount_total");
                        String stayed = obj.getString("month_stayed");
                        String total = obj.getString("month_total");


                        double line = (double) ((Double.parseDouble(stayed) / Double.parseDouble(total)) * 100.0);

                        Log.e("silver", "line" + line);
                        map.put("locale", locale);
                        map.put("image", image);
                        map.put("tag", tag);
                        map.put("start", start);
                        map.put("end", end);
                        map.put("cost", cost);
                        map.put("line", String.valueOf(line));
                        map.put("due", String.valueOf(Integer.parseInt(total) - Integer.parseInt(stayed)));
                        img.add(map);
                    }
                    XLocaleProPager adapter = new XLocaleProPager(fx, cx, img);
                    pager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                    pager.setBackgroundResource(0);
                    pager.setAdapter(adapter);
                    pager.initIndicator();
                    pager.getIndicator()
                            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                            .setFocusColor(getResources().getColor(R.color.colorAccent))
                            .setNormalColor(getResources().getColor(R.color.colorShadow))
                            .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
                    pager.getIndicator().setMargin(0, 0, 0, 20);
                    pager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                    pager.getIndicator().build();
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
            } catch (JSONException i) {
                Log.e("silvr", i.getMessage());
            }
        }, err -> {
            loading.setVisibility(View.GONE);
            Toast.makeText(fx, "Internal server error", Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();
                post.put("mail", mail);
                return post;
            }
        };
        Volley.newRequestQueue(cx).add(request);
    }
}
