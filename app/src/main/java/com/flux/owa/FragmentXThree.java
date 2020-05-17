package com.flux.owa;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.NumberFormat;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentXThree extends XFragment {


    private Charge payments;
    private Button proceed;
    private String finalCost;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xthree, parent, false);
        proceed = view.findViewById(R.id.step_three_agree);

        ImageView back = view.findViewById(R.id.back);
        TextView place = view.findViewById(R.id.review_place);
        TextView period = view.findViewById(R.id.review_month);
        final TextView dates = view.findViewById(R.id.review_date);
        TextView total = view.findViewById(R.id.review_total);

        Bundle xnd = getArguments();
        String xImg = "";
        String hint = "A location";
        String city = "Lagos";
        String month = "0";
        String in = "Today";
        String out = "Check out date";
        String cost = "0";
        String xlocale = "0";
        if (xnd != null) {
            xlocale = xnd.getString("locale");
            xImg = xnd.getString("image");
            hint = xnd.getString("hint");
            city = xnd.getString("city");
            month = xnd.getString("period");
            in = xnd.getString("check_in");
            out = xnd.getString("check_out");
            cost = xnd.getString("total");
        }

        place.setText(String.format("%s in %s", hint, city));
        period.setText(String.format("%s Months", month));
        dates.setText(String.format("%s  -  %s", in, out));
        total.setText(String.format("%s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(Integer.parseInt(cost))));

        initiatePaystackEngine(view); //load payment card

        finalCost = cost;
        proceed.setText(String.format("Pay %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(Integer.parseInt(finalCost) / (Integer.parseInt(month)) * 2)));
        final String finalOut = out;
        final String finalCity = city;
        final String finalHint = hint;
        final String finalXImg = xImg;
        final String finalXlocale = xlocale;
        final String finalMonth = month;
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card = data.getString(XClass.cardCvv, XClass.outcast);
                if (card.isEmpty()){
                    Toast.makeText(face, "Add a payment method first", Toast.LENGTH_SHORT).show();
                } else {
                    proceed.setText("Please Wait");
                    proceed.setTextColor(cx.getResources().getColor(R.color.colorBar));
                    proceed.setBackgroundResource(R.drawable.button_off);


                    int amount = (Integer.parseInt(finalCost) / ( Integer.parseInt(finalMonth))) * 2 * 100;
                    //upload monthly carges
                    //upload card token
                    //upload start and end date
                    //upload house number

                    //update apartments with number of tenants
                    payments.setAmount(amount);
                    PaystackSdk.chargeCard(face, payments, new Paystack.TransactionCallback() {
                        @Override
                        public void onSuccess(Transaction transaction) {
                            XClass.updateUser(XClass.tenant, finalXlocale, mail);
                            XClass.updateUser("bill_total", finalCost, mail);
                            XClass.updateUser("bill_paid",  String.valueOf((Integer.parseInt(finalCost) / Integer.parseInt(finalMonth)) * 2), mail);
                            XClass.updateUser("bill_month", finalMonth, mail);
                            XClass.updateUser("bill_invoice", String.valueOf(Integer.parseInt(finalCost) / Integer.parseInt(finalMonth)), mail);
                            //bill invoice is how much to be deducted subsequently


                            SharedPreferences.Editor e = data.edit();
                            e.putString(XClass.tenant, finalXlocale);
                            e.putString(XClass.tenantImage, finalXImg);
                            e.putString(XClass.tenantDesc, "Your "+ finalHint + " at " + finalCity + " expires on " + finalOut);
                            e.apply();

                            paystackAuth(transaction.getReference());
                        }

                        @Override
                        public void beforeValidate(Transaction transaction) {
                            //keep calm and do nothing
                        }

                        @Override
                        public void onError(Throwable error, Transaction transaction) {
                            Toast.makeText(face, error.getMessage(), Toast.LENGTH_SHORT).show();

                            proceed.setBackgroundResource(R.drawable.button);
                            proceed.setTextColor(cx.getResources().getColor(R.color.colorPrimary));
                            proceed.setText(String.format("Pay %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format((Integer.parseInt(finalCost) / (Integer.parseInt(finalMonth))) * 2)));
                        }
                    });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
            }
        });

        return view;
    }








    private void initiatePaystackEngine(Dialog dl, String name, String number, String cvv, int mm, int yy){
        PaystackSdk.setPublicKey(XClass.paystackKey);
        String mail = data.getString(XClass.mail, null);
        Card card = new Card(number, mm, yy, cvv);
        if (!card.isValid()){
            Toast.makeText(face, "Invalid card", Toast.LENGTH_SHORT).show();
        } else {
            payments = new Charge();
            payments.setEmail(mail);
            payments.setCard(card);
            SharedPreferences.Editor e = data.edit();
            e.putString(XClass.name, name);
            e.putString(XClass.cardNumber, number);
            e.putString(XClass.cardCvv, cvv);
            e.putString(XClass.cardMM, String.valueOf(mm));
            e.putString(XClass.cardYY, String.valueOf(yy));
            e.apply();
            dl.cancel();

            //set name and details
        }
    }









    private void initiatePaystackEngine(View v){
        LinearLayout add = v.findViewById(R.id.add_card);
        final TextView card_name = v.findViewById(R.id.payment_name);
        final TextView card_number = v.findViewById(R.id.payment_number);


        String cc = data.getString(XClass.cardCvv, XClass.outcast);
        if (cc.isEmpty()){

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog card_dialog = new Dialog(face);
                    card_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    card_dialog.setContentView(R.layout.xml_cards);
                    card_dialog.setCancelable(false);
                    card_dialog.show();

                    final EditText edit_name = card_dialog.findViewById(R.id.full_name);
                    edit_name.setText(data.getString(XClass.name, XClass.outcast));

                    Button add = card_dialog.findViewById(R.id.card_save);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText edit_number = card_dialog.findViewById(R.id.card_number);
                            EditText edit_cvv = card_dialog.findViewById(R.id.card_cvv);
                            EditText edit_mm = card_dialog.findViewById(R.id.card_mm);
                            EditText edit_yy = card_dialog.findViewById(R.id.card_yy);

                            String name = edit_name.getText().toString();
                            String number = edit_number.getText().toString();
                            String cvv = edit_cvv.getText().toString();
                            String mm = edit_mm.getText().toString();
                            String yy = edit_yy.getText().toString();


                            if (name.isEmpty()){
                                Toast.makeText(face, "Enter your full name", Toast.LENGTH_SHORT).show();
                            } else if (number.isEmpty()){
                                Toast.makeText(face, "Enter your card number", Toast.LENGTH_SHORT).show();
                            } else if (cvv.isEmpty() || mm.isEmpty() || yy.isEmpty()){
                                Toast.makeText(face, "Enter a valid card details", Toast.LENGTH_SHORT).show();
                            } else {
                                card_name.setText(name);
                                card_number.setText(String.format("**** **** **** %s", number.substring(number.length() - 4)));
                                initiatePaystackEngine(card_dialog, name, number, cvv, Integer.parseInt(mm), Integer.parseInt(yy));
                            }
                        }
                    });

                    ImageView close = card_dialog.findViewById(R.id.close);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            card_dialog.cancel();
                        }
                    });
                }
            });

        } else {
            String name = data.getString(XClass.name, XClass.outcast);
            String number = data.getString(XClass.cardNumber, XClass.outcast);
            String cvv = data.getString(XClass.cardCvv, XClass.outcast);
            String mm = data.getString(XClass.cardMM, XClass.outcast);
            String yy = data.getString(XClass.cardYY, XClass.outcast);

            PaystackSdk.setPublicKey(XClass.paystackKei);
            String mail = data.getString(XClass.mail, null);
            Card card = new Card(number, Integer.parseInt(mm), Integer.parseInt(yy), cvv);

            payments = new Charge();
            payments.setEmail(mail);
            payments.setCard(card);

            card_name.setText(name);
            card_number.setText(String.format("**** **** **** %s", number.substring(number.length() - 4)));
        }
    }

    private void paystackAuth(final String auth){
        Request request = new Request.Builder().url(XClass.apiPaystack + "?mail=" + mail + "&auth=" + auth).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String message = response.body().string().trim();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (message.equals("00")){
                            FragmentSuccess success = new FragmentSuccess();
                            success.setArguments(getArguments());
                            fm.beginTransaction().replace(R.id.fragment, success).commit();
                        } else {
                            proceed.setBackgroundResource(R.drawable.button);
                            proceed.setTextColor(cx.getResources().getColor(R.color.colorPrimary));
                            proceed.setText(String.format("Pay %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(Integer.parseInt(finalCost))));
                            Toast.makeText(face, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}