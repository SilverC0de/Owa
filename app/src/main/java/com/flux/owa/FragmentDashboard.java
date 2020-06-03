package com.flux.owa;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tmall.ultraviewpager.UltraViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentDashboard extends XFragment {

    UltraViewPager pager;
    TextView preview;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, parent, false);

        pager = view.findViewById(R.id.dashboard_pager);
        preview = view.findViewById(R.id.preview);
        LinearLayout receipt = view.findViewById(R.id.receipt);
        LinearLayout invite = view.findViewById(R.id.invite);
        LinearLayout faq = view.findViewById(R.id.faq);
        LinearLayout profile = view.findViewById(R.id.profile);

        LinearLayout rate = view.findViewById(R.id.rate);
        LinearLayout locale = view.findViewById(R.id.locale);
        LinearLayout logout = view.findViewById(R.id.logout);


        receipt.setOnClickListener(v-> fm.beginTransaction().add(R.id.fragment, new FragmentWallet()).addToBackStack(null).commit());

        invite.setOnClickListener(v-> fm.beginTransaction().add(R.id.fragment, new FragmentInvite()).addToBackStack(null).commit());
        faq.setOnClickListener(v-> fm.beginTransaction().add(R.id.fragment, new FragmentFAQ()).addToBackStack(null).commit());
        profile.setOnClickListener(v-> fm.beginTransaction().add(R.id.fragment, new FragmentAccount()).addToBackStack(null).commit());
        locale.setOnClickListener(v-> fm.beginTransaction().add(R.id.fragment, new FragmentLocale()).addToBackStack(null).commit());

        logout.setOnClickListener(v-> {
            startActivity(new Intent(cx, ActivityAccess.class));
            fx.finish();
        });

        rate.setOnClickListener(v->{
            Uri uri = Uri.parse("market://details?id=" + cx.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + cx.getPackageName())));
            }
        });

        initializeOwa();
        return view;
    }


    private void initializeOwa(){
        ArrayList<HashMap<String, String>> img = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, XClass.apiBooked, response ->{
            try {
                JSONArray arr = new JSONArray(response);
                if (arr.length() > 0) {
                    preview.setVisibility(View.GONE);
                    for (int i = 0; i < arr.length(); i++) {
                        //get locale id only
                        HashMap<String, String> map = new HashMap<>();
                        String image = arr.getJSONObject(i).getString("img");
                        String tag = arr.getJSONObject(i).getString("tag");

                        map.put("image", image);
                        map.put("tag", tag);
                        img.add(map);
                    }
                    XLocalePager adapter = new XLocalePager(fx, cx, img);
                    pager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                    pager.setBackgroundResource(0);
                    pager.setAdapter(adapter);
                    pager.initIndicator();
                    pager.getIndicator()
                            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                            .setFocusColor(getResources().getColor(R.color.colorPrimary))
                            .setNormalColor(getResources().getColor(R.color.colorLite))
                            .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
                    pager.getIndicator().setMargin(0, 0, 0, 20);
                    pager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                    pager.getIndicator().build();
                }
            } catch (JSONException ignored) { }

        }, err -> {
            Toast.makeText(fx, "No internet connection", Toast.LENGTH_SHORT).show();
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

//    private void initializeOwa(View v){
//        String tenant = data.getString(XClass.tenant, "0");
//        if (!Objects.equals(tenant, "0")){
//            String tenantImg = data.getString(XClass.tenantImage, "");
//            String tenantDesc = data.getString(XClass.tenantDesc, "");
//
//
//            LinearLayout layout = v.findViewById(R.id.apartment);
//            SimpleDraweeView layoutImg = v.findViewById(R.id.apartment_image);
//            TextView layoutTxt = v.findViewById(R.id.apartment_text);
//
//            layout.setVisibility(View.VISIBLE);
//            layoutImg.setImageURI(tenantImg);
//            layoutTxt.setText(tenantDesc);
//
//
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //open apartment view
//
//                }
//            });
//        }
//    }
}
