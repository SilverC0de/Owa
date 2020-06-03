package com.flux.owa;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;

public class XLocalePager extends PagerAdapter {

    private Activity fx;
    private Context cx;
    private ArrayList<HashMap<String, String>> images;

    XLocalePager(Activity fx, Context cx, ArrayList<HashMap<String, String>> images) {
        this.fx = fx;
        this.cx = cx;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(cx, R.layout.xml_locale_image, null);
        SimpleDraweeView img = view.findViewById(R.id.img);
        TextView txt = view.findViewById(R.id.text);

        img.setImageURI(images.get(position).get("image"));
        txt.setText(images.get(position).get("tag"));


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }
}