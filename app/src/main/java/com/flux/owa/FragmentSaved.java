package com.flux.owa;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.varunest.sparkbutton.SparkButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentSaved extends XFragment {


    private int page = 0;
    private List<XLocale> locale;
    private XListView xList;
    private ProgressBar progress;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, parent, false);

        xList = view.findViewById(R.id.listings);
        progress = view.findViewById(R.id.progress);

        initializeSavedListings(page);

        return view;
    }

    private void initializeSavedListings(int batch){
        locale = new ArrayList<>();

        String mail = data.getString(XClass.mail, null);


        OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("mail", mail)
                .add("batch", String.valueOf(batch))
                .build();

        Request request = new Request.Builder().url(XClass.apiFavourites + "?mail=" + mail + "&batch=" + batch).post(body).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("silvr", "critical failure at fragment home listings");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response result) throws IOException {
                final String response = result.body().string();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            @SuppressWarnings("ConstantConditions")
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String x = object.getString("locale");
                                String image = object.getString("image");
                                String name = object.getString("name");
                                String city = object.getString("city");
                                String agent = object.getString("agent");
                                String agent_name = object.getString("agent_name");
                                String hint = object.getString("hint");
                                int price = object.getInt("price");
                                locale.add(new XLocale(x, image, name, hint, city, agent, agent_name, price));
                            }
                            XAdapter adapter = new XAdapter(cx, R.layout.xml_listings, locale);
                            xList.setVisibility(View.VISIBLE);
                            xList.setAdapter(adapter);
                            xList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    FragmentDetails details = new FragmentDetails();
                                    Bundle bnd = new Bundle();
                                    bnd.putString("locale", locale.get(position).getI());
                                    bnd.putInt("cost", locale.get(position).getPrice());
                                    details.setArguments(bnd);
                                    fm.beginTransaction().add(R.id.fragment, details).addToBackStack(null).commit();
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    initializeSavedListings(page);
                                }
                            }, 4000);
                            progress.setVisibility(View.GONE);
                        } catch (JSONException ignored){

                            Log.e("silvr", "critical failure at fragment home io and json failure");
                        }
                    }
                });
            }
        });

    }


    private static class XAdapter extends ArrayAdapter<XLocale> {

        int layout;
        Context cx;
        List<XLocale> list;

        XAdapter(@NonNull Context context, int resource, @NonNull List<XLocale> objects) {
            super(context, resource, objects);
            this.cx = context;
            this.layout = resource;
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Holder holder;
            if (convertView == null) {
                holder = new Holder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);


                holder.image = convertView.findViewById(R.id.list_image);
                holder.city = convertView.findViewById(R.id.list_city);
                holder.price = convertView.findViewById(R.id.list_price);
                holder.hint = convertView.findViewById(R.id.list_hint);
                holder.name = convertView.findViewById(R.id.list_name);
                holder.save = convertView.findViewById(R.id.list_save);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            String price = cx.getResources().getString(R.string.naira) + NumberFormat.getNumberInstance(Locale.ENGLISH).format(list.get(position).getPrice()) + "/Month";
            holder.image.setImageURI(list.get(position).getImage());
            holder.hint.setText(list.get(position).getHint());
            holder.city.setText(list.get(position).getCity());
            holder.name.setText(list.get(position).getName());
            holder.price.setText(price);

            holder.save.setVisibility(View.GONE);

            return convertView;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    static class Holder{
        SimpleDraweeView image;
        SparkButton save;
        TextView hint, name, city, price;
    }

}
