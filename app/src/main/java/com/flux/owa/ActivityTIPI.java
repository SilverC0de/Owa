package com.flux.owa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;

public class ActivityTIPI extends XActivity {

    LottieAnimationView lottie;
    static int round = 1;
    int test_1, test_2, test_3, test_4, answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_i_p_i);

        lottie = findViewById(R.id.lottie);
        answerListeners();
    }
    /////////////////////////////////////////////////////////////////////////////////////////


    private void answerListeners(){
        LinearLayout one = findViewById(R.id.tipi_disagree_strongly);
        LinearLayout two = findViewById(R.id.tipi_disagree_moderately);
        LinearLayout three = findViewById(R.id.tipi_disagree_a_little);
        LinearLayout four = findViewById(R.id.tipi_neutral);
        LinearLayout five = findViewById(R.id.tipi_agree_a_little);
        LinearLayout six = findViewById(R.id.tipi_agree_moderately);
        LinearLayout seven = findViewById(R.id.tipi_agree_strongly);

        final ImageView one_image = findViewById(R.id.tipi_disagree_strongly_image);
        final ImageView two_image = findViewById(R.id.tipi_disagree_moderately_image);
        final ImageView three_image = findViewById(R.id.tipi_disagree_a_little_image);
        final ImageView four_image = findViewById(R.id.tipi_neutral_image);
        final ImageView five_image = findViewById(R.id.tipi_agree_a_little_image);
        final ImageView six_image = findViewById(R.id.tipi_agree_moderately_image);
        final ImageView seven_image = findViewById(R.id.tipi_agree_strongly_image);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_image.setImageResource(R.drawable.check);
                two_image.setImageResource(R.drawable.nocheck);
                three_image.setImageResource(R.drawable.nocheck);
                four_image.setImageResource(R.drawable.nocheck);
                five_image.setImageResource(R.drawable.nocheck);
                six_image.setImageResource(R.drawable.nocheck);
                seven_image.setImageResource(R.drawable.nocheck);

                answer = 1;

                autoNext();
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_image.setImageResource(R.drawable.nocheck);
                two_image.setImageResource(R.drawable.check);
                three_image.setImageResource(R.drawable.nocheck);
                four_image.setImageResource(R.drawable.nocheck);
                five_image.setImageResource(R.drawable.nocheck);
                six_image.setImageResource(R.drawable.nocheck);
                seven_image.setImageResource(R.drawable.nocheck);

                answer = 2;

                autoNext();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_image.setImageResource(R.drawable.nocheck);
                two_image.setImageResource(R.drawable.nocheck);
                three_image.setImageResource(R.drawable.check);
                four_image.setImageResource(R.drawable.nocheck);
                five_image.setImageResource(R.drawable.nocheck);
                six_image.setImageResource(R.drawable.nocheck);
                seven_image.setImageResource(R.drawable.nocheck);

                answer = 3;

                autoNext();
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_image.setImageResource(R.drawable.nocheck);
                two_image.setImageResource(R.drawable.nocheck);
                three_image.setImageResource(R.drawable.nocheck);
                four_image.setImageResource(R.drawable.check);
                five_image.setImageResource(R.drawable.nocheck);
                six_image.setImageResource(R.drawable.nocheck);
                seven_image.setImageResource(R.drawable.nocheck);

                answer = 4;

                autoNext();
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_image.setImageResource(R.drawable.nocheck);
                two_image.setImageResource(R.drawable.nocheck);
                three_image.setImageResource(R.drawable.nocheck);
                four_image.setImageResource(R.drawable.nocheck);
                five_image.setImageResource(R.drawable.check);
                six_image.setImageResource(R.drawable.nocheck);
                seven_image.setImageResource(R.drawable.nocheck);

                answer = 5;

                autoNext();
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_image.setImageResource(R.drawable.nocheck);
                two_image.setImageResource(R.drawable.nocheck);
                three_image.setImageResource(R.drawable.nocheck);
                four_image.setImageResource(R.drawable.nocheck);
                five_image.setImageResource(R.drawable.nocheck);
                six_image.setImageResource(R.drawable.check);
                seven_image.setImageResource(R.drawable.nocheck);

                answer = 6;

                autoNext();
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_image.setImageResource(R.drawable.nocheck);
                two_image.setImageResource(R.drawable.nocheck);
                three_image.setImageResource(R.drawable.nocheck);
                four_image.setImageResource(R.drawable.nocheck);
                five_image.setImageResource(R.drawable.nocheck);
                six_image.setImageResource(R.drawable.nocheck);
                seven_image.setImageResource(R.drawable.check);

                answer = 7;

                autoNext();
            }
        });
    }

    void autoNext(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final TextView question = findViewById(R.id.tipi_question);
                final TextView question_count = findViewById(R.id.tipi_count);


                final ImageView one_image = findViewById(R.id.tipi_disagree_strongly_image);
                final ImageView two_image = findViewById(R.id.tipi_disagree_moderately_image);
                final ImageView three_image = findViewById(R.id.tipi_disagree_a_little_image);
                final ImageView four_image = findViewById(R.id.tipi_neutral_image);
                final ImageView five_image = findViewById(R.id.tipi_agree_a_little_image);
                final ImageView six_image = findViewById(R.id.tipi_agree_moderately_image);
                final ImageView seven_image = findViewById(R.id.tipi_agree_strongly_image);

                int[] raw = {R.raw.teamwork, R.raw.work, R.raw.quiet, R.raw.raw_1_friend};


                final String[] questions = {
                        "I am very enthusiastic and love to be around people",
                        "I criticize people and I am quarrelsome",
                        "I am reserved and quiet",
                        "I am sympathetic and warm to people",
                };

                int x = (answer == 1) ? 7 : (answer == 2) ? 6 : (answer == 3) ? 5 : (answer == 4) ? 4 : (answer == 5) ? 3 : (answer == 6) ? 2 : 1;

                switch (round){
                    case 1:
                        test_1 = answer;
                        break;
                    case 2:
                        test_2 = x;
                        break;
                    case 3:
                        test_3 = answer;
                        break;
                    case 4:
                        test_4 = x;
                        break;
                } round++;



                if (round > 4) {

                    String mail = data.getString(XClass.mail, null);

                    int extroversion = (int )Math.round ( ( ( (test_1 + test_3) / 2.0 ) / 7.0) * 100 );
                    int considerate = (int) Math.round ( ( ( (test_2 + test_4) / 2.0 ) / 7.0) * 100 );


                    XClass.updateUser("extroverted", String.valueOf(extroversion), mail);
                    XClass.updateUser("considerate", String.valueOf(considerate), mail);


                    SharedPreferences.Editor e = data.edit();
                    e.putBoolean(XClass.okTested, true);
                    e.apply();

                    startActivity(new Intent(getApplicationContext(), ActivityMain.class));
                    finish();
                } else {

                    lottie.setAnimation(raw[round - 1]);
//                    lottie.setRepeatCount(LottCou);
                    lottie.playAnimation();
                    question.setText(questions[round - 1]);
                    question_count.setText(String.format(Locale.ENGLISH, "Question %d of 4", round));

                    one_image.setImageResource(R.drawable.nocheck);
                    two_image.setImageResource(R.drawable.nocheck);
                    three_image.setImageResource(R.drawable.nocheck);
                    four_image.setImageResource(R.drawable.nocheck);
                    five_image.setImageResource(R.drawable.nocheck);
                    six_image.setImageResource(R.drawable.nocheck);
                    seven_image.setImageResource(R.drawable.nocheck);
                }
            }
        }, 400);
    }
}