package com.flux.owa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.text.NumberFormat;

public class FragmentFilter extends XFragment implements AdapterView.OnItemSelectedListener {

    private Spinner apartment_type;
    private String x_property_type, x_min = "", x_max = "";
    private RangeSeekBar range;
    private TextView price_display;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, parent, false);

        Button filter_results = view.findViewById(R.id.filter_results);
        ImageView close = view.findViewById(R.id.close);

        apartment_type = view.findViewById(R.id.filter_type_of_house);
        range = view.findViewById(R.id.filter_price);
        price_display = view.findViewById(R.id.filter_price_display);

        initializeApartmentFilter();
        initializePriceFilter();


        filter_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = "";
                if (getArguments() != null) city = getArguments().getString("city", "");


                FragmentHome home = new FragmentHome();
                Bundle filter = new Bundle();
                filter.putString("city", city);
                filter.putString("hint", x_property_type);
                filter.putString("min", x_min);
                filter.putString("max", x_max);
                filter.putString("header", "Based on your filter");
                home.setArguments(filter);
                fm.popBackStack();
                fm.beginTransaction().replace(R.id.fragment, home).commit();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
            }
        });
        return view;
    }


    private void initializeApartmentFilter(){
        ArrayAdapter<CharSequence> apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.property_type, R.layout.xml_spinner);
        apartment_adapter.setDropDownViewResource(R.layout.xml_spinner);
        apartment_type.setAdapter(apartment_adapter);
        apartment_type.setOnItemSelectedListener(this);
    }

    private void initializePriceFilter(){
        range.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                x_min = String.valueOf(Math.round(leftValue));
                x_max = String.valueOf(Math.round(rightValue));
                String min = cx.getResources().getString(R.string.naira) + NumberFormat.getNumberInstance().format(Math.round(leftValue));
                String max = cx.getResources().getString(R.string.naira) + NumberFormat.getNumberInstance().format(Math.round(rightValue));
                price_display.setText(String.format("Between %s and %s", min, max));
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] property_filter = {"apartment", "room", "bed"};
        x_property_type = property_filter[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}