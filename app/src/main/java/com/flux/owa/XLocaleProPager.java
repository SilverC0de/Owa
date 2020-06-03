package com.flux.owa;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class XLocaleProPager extends PagerAdapter {

    private Activity fx;
    private Context cx;
    private ArrayList<HashMap<String, String>> images;

    XLocaleProPager(Activity fx, Context cx, ArrayList<HashMap<String, String>> images) {
        this.fx = fx;
        this.cx = cx;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(cx, R.layout.xml_locale_pro_image, null);

        SharedPreferences data = cx.getSharedPreferences(XClass.sharedPreferences, Context.MODE_PRIVATE);

        LottieAnimationView loading = view.findViewById(R.id.loading);
        SimpleDraweeView img = view.findViewById(R.id.lx);
        TextView txt = view.findViewById(R.id.txt);

        TextView start = view.findViewById(R.id.start);
        TextView end = view.findViewById(R.id.end);
        TextView middle = view.findViewById(R.id.middle);


        Button fund = view.findViewById(R.id.fund);

        XListView receipts = view.findViewById(R.id.list);

        img.setImageURI(images.get(position).get("image"));
        txt.setText(images.get(position).get("tag"));

        start.setText(String.format("%s%s", cx.getResources().getString(R.string.naira), NumberFormat.getInstance().format(Integer.parseInt(images.get(position).get("start")))));
        end.setText(String.format("%s%s", cx.getResources().getString(R.string.naira), NumberFormat.getInstance().format(Integer.parseInt(images.get(position).get("end")))));
        middle.setText(String.format("%s months left", images.get(position).get("due")));


        fund.setText(String.format("Pay %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getInstance().format(Integer.parseInt(images.get(position).get("cost")))));
        fund.setOnClickListener(v->{
            fund.setAlpha(.4f);
            fund.setEnabled(false);

            loading.setVisibility(View.VISIBLE);

            String mail = data.getString(XClass.mail, null);
            String auth = data.getString(XClass.auth, null);
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

                        String mail = data.getString(XClass.mail, null);
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
                            initializePayment(loading, fund, start, mail, name, number, cvv, Integer.parseInt(mm), Integer.parseInt(yy), images.get(position).get("cost"), images.get(position).get("locale"));
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
                initializePayment(loading, fund, start, mail, images.get(position).get("cost"), auth, images.get(position).get("locale"));
            }
        });

        String locale = images.get(position).get("locale");
        List<HashMap<String, String>> list = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, XClass.apiReceipt + "?locale=" + locale, response -> {
            //loading.setVisibility(View.GONE);
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
                //loading.setVisibility(View.GONE);
            }
        }, error -> {
            //loading.setVisibility(View.GONE);
        });
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(request);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }


    private void initializePayment(LottieAnimationView loading, Button fund, TextView paid, String mail, String name, String number, String cvv, int mm, int yy, String cash, String locale){
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
                initializePayment(transaction, loading, fund,  paid, mail, cash, locale);
            }

            @Override
            public void beforeValidate(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                fund.setAlpha(1f);
                fund.setEnabled(true);
                loading.setVisibility(View.GONE);

                Toast.makeText(fx, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializePayment(Transaction trx, LottieAnimationView loading, Button fund, TextView paid, String mail, String amount, String locale){
        //for confirming payment without auth codes
        StringRequest request = new StringRequest(Request.Method.POST, XClass.apiPaidRent, response -> {

            fund.setAlpha(1f);
            fund.setEnabled(true);
            loading.setVisibility(View.GONE);
            if (response.startsWith("AUTH")){

                double cash = Double.parseDouble(amount) + Integer.parseInt(paid.getText().toString().replaceAll("[^\\d.]", ""));
                paid.setText(String.format("%s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(cash)));

                SharedPreferences data = cx.getSharedPreferences(XClass.sharedPreferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor e = data.edit();
                e.putString(XClass.auth, response);
                e.apply();

                Dialog dl = new Dialog(fx);
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.setContentView(R.layout.xml_done);
                TextView txt = dl.findViewById(R.id.info);
                txt.setText(String.format("Payment of %s%s for this apartment was successful", cx.getResources().getString(R.string.naira), amount));
                dl.show();
            } else {
                Toast.makeText(fx, response, Toast.LENGTH_SHORT).show();
            }

            //done
        }, err ->{
            //done
            Toast.makeText(fx, "paidrent", Toast.LENGTH_SHORT).show();

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
                post.put("locale", locale);
                return post;
            }
        };
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(request);
    }




    private void initializePayment(LottieAnimationView loading, Button fund, TextView paid, String mail, String amount, String auth, String locale){
        loading.setVisibility(View.VISIBLE);

        //for payments with auth codes
        StringRequest request = new StringRequest(Request.Method.POST, XClass.apiPayRent, response -> {
            Log.e("silvr", "with auth" + response);

            fund.setAlpha(1f);
            fund.setEnabled(true);
            loading.setVisibility(View.GONE);

            if (response.equals("00")){


                double cash = Double.parseDouble(amount) + Integer.parseInt(paid.getText().toString().replaceAll("[^\\d.]", ""));
                paid.setText(String.format("%s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(cash)));


                Dialog dl = new Dialog(fx);
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.setContentView(R.layout.xml_done);
                TextView txt = dl.findViewById(R.id.info);
                txt.setText(String.format("Payment of %s%s for this apartment was successful", cx.getResources().getString(R.string.naira), amount));

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
                post.put("locale", locale);
                return post;
            }
        };
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(request);
    }

}