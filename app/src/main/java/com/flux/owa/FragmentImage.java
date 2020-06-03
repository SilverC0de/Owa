package com.flux.owa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FragmentImage extends XFragment {

    private List<XImage> images;
    private ListView list;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, parent, false);
        ImageView back = view.findViewById(R.id.back);
        list = view.findViewById(R.id.list);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
            }
        });
        String locale = getArguments().getString("locale");

        StringRequest request = new StringRequest(Request.Method.GET, XClass.apiImage + "?locale=" + locale, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                images = new ArrayList<>();

                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++){
                        String image = arr.getJSONObject(i).getString("path");
                        images.add(new XImage(image));
                    }
                    XAdapter adapter = new XAdapter(cx, fx, R.layout.xml_image, images);
                    list.setAdapter(adapter);
                } catch (JSONException ignored){ }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(cx).add(request);


        return view;
    }



    //////////////////////////////////////////////////////////////////////////////////////////////

    private static class XAdapter extends ArrayAdapter<XImage> {

        int layout;
        Activity fx;
        Context cx;
        List<XImage> list;

        XAdapter(@NonNull Context context, Activity activity, int resource, @NonNull List<XImage> objects) {
            super(context, resource, objects);
            this.cx = context;
            this.fx = activity;
            this.layout = resource;
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final FragmentHome.Holder holder;
            if (convertView == null) {
                holder = new FragmentHome.Holder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);


                holder.image = convertView.findViewById(R.id.img);

                convertView.setTag(holder);
            } else {
                holder = (FragmentHome.Holder) convertView.getTag();
            }

            //holder.image.setImageURI(list.get(position).getImage());
            NanolliteSDK.initializeFrescoImage(fx, cx, holder.image, list.get(position).getImage());

            return convertView;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    static class Holder{
        SimpleDraweeView image;
    }



}
