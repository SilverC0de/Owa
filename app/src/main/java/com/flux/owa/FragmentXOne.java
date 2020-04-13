package com.flux.owa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentXOne extends XFragment {

    private Bundle xnd;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xone, parent, false);


        ImageView back = view.findViewById(R.id.back);
        TextView check_in = view.findViewById(R.id.book_check_in);
        TextView check_out = view.findViewById(R.id.book_check_out);

        TextView review = view.findViewById(R.id.book_review);

        Button proceed = view.findViewById(R.id.step_one_agree);


        xnd = getArguments();
        if (xnd != null) {
            String month = xnd.getString("period");
            String city = xnd.getString("city");
            String xcheck_in = xnd.getString("check_in");
            String xcheck_out = xnd.getString("check_out");

            review.setText(String.format("%s months in %s", month, city));
            check_in.setText(xcheck_in);
            check_out.setText(xcheck_out);
        } else {
            fm.popBackStack();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentXTwo xTwo = new FragmentXTwo();
                xTwo.setArguments(xnd);
                fm.beginTransaction().add(R.id.fragment, xTwo).addToBackStack(null).commit();
            }
        });
        return view;
    }
}