package com.flux.owa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentFAQ extends XFragment {

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_faq, child, false);

        ImageView back = view.findViewById(R.id.back);

        LinearLayout question1 = view.findViewById(R.id.question1);
        LinearLayout question2 = view.findViewById(R.id.question2);
        LinearLayout question3 = view.findViewById(R.id.question3);
        LinearLayout question4 = view.findViewById(R.id.question4);

        TextView answer1 = view.findViewById(R.id.answer1);
        TextView answer2 = view.findViewById(R.id.answer2);
        TextView answer3 = view.findViewById(R.id.answer3);
        TextView answer4 = view.findViewById(R.id.answer4);

        question1.setOnClickListener(v-> showAnswer(answer1, view));
        question2.setOnClickListener(v-> showAnswer(answer2, view));
        question3.setOnClickListener(v-> showAnswer(answer3, view));
        question4.setOnClickListener(v-> showAnswer(answer4, view));

        back.setOnClickListener(v->fm.popBackStack());

        return view;
    }

    private void showAnswer(TextView answer, View view){
        TextView answer1 = view.findViewById(R.id.answer1);
        TextView answer2 = view.findViewById(R.id.answer2);
        TextView answer3 = view.findViewById(R.id.answer3);
        TextView answer4 = view.findViewById(R.id.answer4);


        answer1.setVisibility(View.GONE);
        answer2.setVisibility(View.GONE);
        answer3.setVisibility(View.GONE);
        answer4.setVisibility(View.GONE);

        answer.setVisibility(View.VISIBLE);
    }

}
