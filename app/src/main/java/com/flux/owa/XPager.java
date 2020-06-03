package com.flux.owa;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class XPager extends PagerAdapter {

    private Activity fx;
    private Context cx;
    private ArrayList<String> images;

    XPager(Activity fx, Context cx, ArrayList<String> images) {
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
        View view = View.inflate(cx, R.layout.xml_image, null);
        SimpleDraweeView img = view.findViewById(R.id.img);

        img.setImageURI(images.get(position));

//        img.setOnClickListener(v->{
//            Dialog dl = new Dialog(fx);
//            dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dl.setContentView(R.layout.xml_image);
//
//            SimpleDraweeView i = dl.findViewById(R.id.img);
//            i.setImageURI(images.get(position));
//            i.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
//            dl.show();
//        });

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