package com.flux.owa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentSuccess extends XFragment {
    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success, parent, false);

        Bundle xnd = getArguments();
        String hint = "a space";
        String city = "a city";
        if (xnd != null) {
            hint = xnd.getString("hint");
            city = xnd.getString("city");
        }

        Button end = view.findViewById(R.id.end);
        TextView content = view.findViewById(R.id.content);


        String number = data.getString(XClass.number, XClass.outcast);
        String message = "Congratulations, you just reserved a " + hint + " in " + city + ". An in-house agent will contact you on your mobile number " + number;


        content.setText(message);

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.fragment, new FragmentAccount()).commit();
            }
        });
        return view;
    }
}
