package com.flux.owa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentHome extends XFragment {

    private int page = 0;

    private List<XLocale> locale;
    private XListView xList;
    private ProgressBar progress;
    private TextView header_view;
    private SearchView search;
    private ImageView filter_view;

    private String query_header = "";
    private String query_city = "";
    private String query_hint = "";
    private String query_min = "10000";
    private String query_max = "1000000000";


    @Override
    public void onDestroy() {
        super.onDestroy();
        page = 0;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cx = context;
        fm = getFragmentManager();
    }

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, parent, false);

        xList = view.findViewById(R.id.listings);
        progress = view.findViewById(R.id.progress);
        header_view = view.findViewById(R.id.header);
        search = view.findViewById(R.id.search);
        LinearLayout selection = view.findViewById(R.id.selection);

        filter_view = view.findViewById(R.id.filter_view);

        locale = new ArrayList<>();


        Bundle query = getArguments();

        if (query != null) {
            locale.clear();
            xList.setAdapter(null);
            query_header = query.getString("header");
            query_city = query.getString("city");
            query_hint = query.getString("hint");
            query_min = query.getString("min");
            query_max = query.getString("max");
            header_view.setText(query_header);
        }

        if (query_city != null) search.setQuery(query_city, false);


        initializeView(view);
        initializeSearch(view);
        initializeSelection(view);
        initializeListings(query_city, query_hint, query_min, query_max, page); //display everything

        initializeMore();

        return view;
    }

    private void initializeView(View v){
        String tenant = data.getString(XClass.tenant, "0");
        if (!Objects.equals(tenant, "0")){
            String tenantImg = data.getString(XClass.tenantImage, "");
            String tenantDesc = data.getString(XClass.tenantDesc, "");


            LinearLayout layout = v.findViewById(R.id.apartment);
            SimpleDraweeView layoutImg = v.findViewById(R.id.apartment_image);
            TextView layoutTxt = v.findViewById(R.id.apartment_text);

            layout.setVisibility(View.VISIBLE);
            layoutImg.setImageURI(tenantImg);
            layoutTxt.setText(tenantDesc);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open apartment view

                }
            });
        }
    }

    private void initializeSelection(View v){
        ImageView apartment = v.findViewById(R.id.select_apartment);
        ImageView bed = v.findViewById(R.id.select_bed);
        ImageView room = v.findViewById(R.id.select_room);

        apartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                String hint = "apartment";
                locale.clear();
                xList.setAdapter(null);

                progress.setVisibility(View.VISIBLE);
                header_view.setText("Based on your selection");

                Bundle bnd = new Bundle();
                bnd.putString("city", XClass.outcast);
                bnd.putString("hint", hint);
                new FragmentHome().setArguments(bnd);
                initializeListings(XClass.outcast, hint, XClass.outcast, XClass.outcast, page);
            }
        });
        bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                String hint = "bed";
                locale.clear();
                xList.setAdapter(null);

                progress.setVisibility(View.VISIBLE);
                header_view.setText("Based on your selection");

                Bundle bnd = new Bundle();
                bnd.putString("city", XClass.outcast);
                bnd.putString("hint", hint);
                new FragmentHome().setArguments(bnd);
                initializeListings(XClass.outcast, hint, XClass.outcast, XClass.outcast, 0);
            }
        });
        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                String hint = "room";
                locale.clear();
                xList.setAdapter(null);

                progress.setVisibility(View.VISIBLE);
                header_view.setText("Based on your selection");

                Bundle bnd = new Bundle();
                bnd.putString("city", XClass.outcast);
                bnd.putString("hint", hint);
                new FragmentHome().setArguments(bnd);
                initializeListings(XClass.outcast, hint, XClass.outcast, XClass.outcast, page);
            }
        });
    }

    private void initializeSearch(final View v){
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                page = 0;
                String hint = "apartment"; // user choice of selection

                locale.clear();
                xList.setAdapter(null);

                progress.setVisibility(View.VISIBLE);

                InputMethodManager im = (InputMethodManager)cx.getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(im).hideSoftInputFromWindow(v.getWindowToken(), 0);

                Bundle bnd = new Bundle();
                bnd.putString("header", "Apartments around " + query);
                bnd.putString("city", query);
                bnd.putString("hint", hint);
                new FragmentHome().setArguments(bnd);
                initializeListings(query, hint, query_min, query_max, page);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initializeListings(final String city, final String hint, final String min, final String max, final int batch) {

        OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("city", city)
                .add("hint", hint)
                .add("min", min)
                .add("max", max)
                .add("batch", String.valueOf(batch))
                .build();

        Log.e("silvr", "--hint" + hint);
        Log.e("silvr", "--city" + city);
        Log.e("silvr", "--batch" + batch);

        Request request = new Request.Builder().url(XClass.apiListings + "?city=" + city + "&hint=" + hint + "&min=" + min + "&max=" + max + "&batch=" + batch).post(body).build();
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
                            @SuppressWarnings("ConstantConditions") final JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String x = object.getString("i");
                                String image = object.getString("image");
                                String name = object.getString("name");
                                String city = object.getString("city");
                                String hint = object.getString("hint");
                                String agent = object.getString("agent");
                                String agent_name = object.getString("agent_name");
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
                                    bnd.putInt("cost", locale.get(position).getPrice());
                                    bnd.putString("name", locale.get(position).getName());
                                    bnd.putString("city", locale.get(position).getCity());
                                    bnd.putString("hint", locale.get(position).getHint());
                                    bnd.putString("locale", locale.get(position).getI());
                                    bnd.putString("agent", locale.get(position).getAgent());
                                    bnd.putString("agent_name", locale.get(position).getAgent_name());
                                    bnd.putString("image", locale.get(position).getImage());
                                    details.setArguments(bnd);
                                    fm.beginTransaction().add(R.id.fragment, details).addToBackStack(null).commit();
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (array.length() > 18) initializeListings(city, hint, min, max, page++);
                                }
                            }, 4000);
                            progress.setVisibility(View.GONE);
                        } catch (JSONException ignored){
                            Log.e("silvr", "critical failure at fragment home io and json failure" + ignored.getMessage());
                        }
                    }
                });
            }
        });

        filter_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFilter filter = new FragmentFilter();
                Bundle bnd = new Bundle();
                bnd.putString("city", city);
                filter.setArguments(bnd);
                fm.beginTransaction().add(R.id.filter, filter).setCustomAnimations(R.anim.slide_up, R.anim.slide_down).addToBackStack(null).commit();
            }
        });
    }


    private void initializeMore(){

    }



    private static class XAdapter extends ArrayAdapter<XLocale>{

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


            holder.save.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {

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

                    Request request = new Request.Builder().url(XClass.apiAddToFavourites + "?mail=" + mail + "&locale=" + list.get(position).getI()).build();
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
