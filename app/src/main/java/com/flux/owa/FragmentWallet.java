package com.flux.owa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class FragmentWallet extends XFragment {

    TextView wallet, fund;
    ListView receipts;
    LottieAnimationView loading;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_wallet, child, false);

        wallet = view.findViewById(R.id.wallet);

        EditText amount = view.findViewById(R.id.amount);
        fund = view.findViewById(R.id.fund);

        ImageView back = view.findViewById(R.id.back);
        TextView start = view.findViewById(R.id.start);
        TextView end = view.findViewById(R.id.end);

        receipts = view.findViewById(R.id.list);
        loading = view.findViewById(R.id.loading);


        back.setOnClickListener(v-> fm.popBackStack());

        String end_date = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(new Date()); //25 Jan 2019

        end.setText(end_date);
        start.setOnClickListener(v->{
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(cx,
                    (x, yearOfCentury, monthOfYear, dayOfMonth) -> {
                        Calendar cc = Calendar.getInstance();
                        int day1 = x.getDayOfMonth();
                        int month1 = x.getMonth();
                        int year1 = x.getYear();
                        cc.set(year1, month1, day1);
                        String xx = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(cc.getTime()); //25 Jan 2019
                        start.setText(xx);
                    },
                    year,
                    month,
                    day);
            //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        end.setOnClickListener(v->{
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(cx,
                    (x, yearOfCentury, monthOfYear, dayOfMonth) -> {
                        Calendar cc = Calendar.getInstance();
                        int day1 = x.getDayOfMonth();
                        int month1 = x.getMonth();
                        int year1 = x.getYear();
                        cc.set(year1, month1, day1);
                        String yy = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(cc.getTime()); //25 Jan 2019
                        end.setText(yy);
                    },
                    year,
                    month,
                    day);
            //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });


        String auth = data.getString(XClass.auth, null);

        fund.setOnClickListener(v->{
            String cash = amount.getText().toString();
            if (cash.isEmpty()){
                Toast.makeText(fx, "Enter an amount", Toast.LENGTH_SHORT).show();
            } else {
                amount.getText().clear();
                fund.setAlpha(.4f);
                fund.setEnabled(false);

                if (auth == null || auth.isEmpty() || auth.equals("null")) {
                    //request for card
                    Dialog card_dialog = new Dialog(fx);
                    card_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    card_dialog.setContentView(R.layout.xml_cards);
                    card_dialog.setCancelable(false);
                    card_dialog.show();

                    EditText edit_name = card_dialog.findViewById(R.id.full_name);

                    EditText edit_number = card_dialog.findViewById(R.id.card_number);

                    edit_name.setText(data.getString(XClass.name, XClass.outcast));
                    Button add = card_dialog.findViewById(R.id.card_save);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText edit_cvv = card_dialog.findViewById(R.id.card_cvv);
                            EditText edit_mm = card_dialog.findViewById(R.id.card_mm);
                            EditText edit_yy = card_dialog.findViewById(R.id.card_yy);

                            String name = edit_name.getText().toString();
                            String number = edit_number.getText().toString().replaceAll("\\s+","");
                            String cvv = edit_cvv.getText().toString();
                            String mm = edit_mm.getText().toString();
                            String yy = edit_yy.getText().toString();


                            if (name.isEmpty()) {
                                Toast.makeText(fx, "Enter your full name", Toast.LENGTH_SHORT).show();
                            } else if (number.isEmpty()) {
                                Toast.makeText(fx, "Enter your card number", Toast.LENGTH_SHORT).show();
                            } else if (cvv.isEmpty() || mm.isEmpty() || yy.isEmpty()) {
                                Toast.makeText(fx, "Enter a valid card details", Toast.LENGTH_SHORT).show();
                            } else {
                                card_dialog.cancel();
                                initializePayment(name, number, cvv, Integer.parseInt(mm), Integer.parseInt(yy), cash);
                            }
                        }
                    });

//                    edit_number.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                            edit_number.removeTextChangedListener(this);
//                            edit_number.setText(NanolliteSDK.cardLint(String.valueOf(s).replaceAll("\\s+","")));
//                            edit_number.setSelection(s.length());
//                            edit_number.addTextChangedListener(this);
//                        }
//                    });

                    ImageView close = card_dialog.findViewById(R.id.close);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            card_dialog.cancel();
                            fund.setAlpha(1f);
                            fund.setEnabled(true);
                        }
                    });
                    card_dialog.setOnCancelListener(x->{
                        card_dialog.cancel();
                        fund.setAlpha(1f);
                        fund.setEnabled(true);
                    });
                } else {
                    initializePayment(cash, auth);
                }
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////
        String balance = data.getString(XClass.balance, "0.00");
        wallet.setText(String.format("%s%s", cx.getResources().getString(R.string.naira), DecimalFormat.getInstance().format(Double.parseDouble(balance))));



        loadReceipts();
        return view;
    }


    private void initializePayment(String name, String number, String cvv, int mm, int yy, String cash){
        PaystackSdk.setPublicKey(XClass.paystackKey);

        loading.setVisibility(View.VISIBLE);
        //do paystack payment


        Charge payments = new Charge();
        Card card = new Card(number, mm, yy, cvv);

        payments.setEmail(mail);
        payments.setCard(card);
        payments.setAmount(Integer.parseInt(cash) * 100);

        PaystackSdk.chargeCard(fx, payments, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                initializePayment(transaction, cash);
            }

            @Override
            public void beforeValidate(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                fund.setAlpha(1f);
                fund.setEnabled(true);
                loading.setVisibility(View.GONE);
            }
        });
    }

    private void initializePayment(Transaction trx, String amount){
        //for confirming payment without auth codes
        StringRequest request = new StringRequest(Request.Method.POST, XClass.apiBilled, response -> {

            if (response.startsWith("AUTH")){
                double cash = Double.parseDouble(amount) + Double.parseDouble(wallet.getText().toString().replaceAll("[^\\d.]", ""));
                wallet.setText(String.format("%s%s", cx.getResources().getString(R.string.naira), cash));

                SharedPreferences.Editor e = data.edit();
                e.putString(XClass.auth, response);
                e.putString(XClass.balance, String.valueOf(cash));
                e.apply();

                Dialog dl = new Dialog(fx);
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.setContentView(R.layout.xml_done);
                TextView txt = dl.findViewById(R.id.info);
                txt.setText(String.format("You funded your wallet with %s%s", cx.getResources().getString(R.string.naira), amount));
                dl.show();
            } else {
                Toast.makeText(fx, response, Toast.LENGTH_SHORT).show();
            }

            fund.setAlpha(1f);
            fund.setEnabled(true);
            loading.setVisibility(View.GONE);
        }, err ->{
            fund.setAlpha(1f);
            fund.setEnabled(true);
            loading.setVisibility(View.GONE);
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();
                post.put("mail", mail);
                post.put("amount", amount);
                post.put("reference", trx.getReference());
                return post;
            }
        };
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(request);
    }




    private void initializePayment(String amount, String auth){
        loading.setVisibility(View.VISIBLE);

        //for payments with auth codes
        StringRequest request = new StringRequest(Request.Method.POST, XClass.apiBill, response -> {
            Log.e("silvr", "with auth" + response);

            fund.setAlpha(1f);
            fund.setEnabled(true);
            loading.setVisibility(View.GONE);

            if (response.equals("00")){
                double cash = Double.parseDouble(amount) + Double.parseDouble(wallet.getText().toString().replaceAll("[^\\d.]", ""));
                wallet.setText(String.format("%s%s", cx.getResources().getString(R.string.naira), cash));

                SharedPreferences.Editor e = data.edit();
                e.putString(XClass.balance, String.valueOf(cash));
                e.apply();

                Dialog dl = new Dialog(fx);
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.setContentView(R.layout.xml_done);
                TextView txt = dl.findViewById(R.id.info);
                txt.setText(String.format("You funded your wallet with %s%s", cx.getResources().getString(R.string.naira), DecimalFormat.getInstance().format(Double.parseDouble(amount))));

                dl.show();
            } else {
                Toast.makeText(fx, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        }, err ->{
            fund.setAlpha(1f);
            fund.setEnabled(true);
            loading.setVisibility(View.GONE);
            Log.e("silvr",  " " + err.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();
                post.put("mail", mail);
                post.put("amount", amount);
                post.put("auth", auth);
                return post;
            }
        };
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(request);
    }


    private void loadReceipts(){

        List<HashMap<String, String>> list = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, XClass.apiReceipt + "?mail=" + mail, response -> {
            Log.e("silvr", "receipt----" + response);
            loading.setVisibility(View.GONE);
            try {
                JSONArray array = new JSONArray(response);
                if (array.length() == 0){
                    Toast.makeText(fx, "empty", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < array.length(); i++){
                        HashMap<String, String> map = new HashMap<>();

                        JSONObject obj = array.getJSONObject(i);
                        String message = obj.getString("message");
                        String stamp = obj.getString("stamp");
                        String amount = obj.getString("amount");

                        map.put("message", message);
                        map.put("stamp", stamp);
                        map.put("amount", cx.getResources().getString(R.string.naira) + DecimalFormat.getInstance().format(Double.parseDouble(amount)));
                        list.add(map);
                    }

                    String[] from = {"message", "stamp", "amount"};
                    int[] to = {R.id.message, R.id.stamp, R.id.amount};
                    SimpleAdapter adapter = new SimpleAdapter(cx, list, R.layout.xml_receipt, from, to);
                    receipts.setAdapter(adapter);
                }
            } catch (JSONException e) {
                loading.setVisibility(View.VISIBLE);
                loading.setAnimation(R.raw.empty_box);
                loading.setRepeatCount(0);
                loading.playAnimation();
                Toast.makeText(fx, "No transaction receipt yet", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            loading.setVisibility(View.GONE);
            Toast.makeText(fx, "An error occurred", Toast.LENGTH_SHORT).show();
        });
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(request);
    }
}