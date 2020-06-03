package com.flux.owa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentLogin extends XFragment {


    LottieAnimationView loading;
    Button proceed;
    TextView error;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, parent, false);

        TextView register = view.findViewById(R.id.register);
        EditText find_mail = view.findViewById(R.id.access_mail);
        EditText find_password = view.findViewById(R.id.access_password);
        proceed = view.findViewById(R.id.access_continue);
        loading = view.findViewById(R.id.loading);
        error = view.findViewById(R.id.error);

        register.setOnClickListener(v-> fm.beginTransaction().replace(R.id.fragment, new FragmentRegister()).commit());

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = find_mail.getText().toString().trim();
                String password = find_password.getText().toString().trim();
                if (mail.isEmpty()){
                    error.setText("Please enter a valid mail address");
                } if (password.isEmpty()){
                    error.setText("Please enter a valid password");
                } else {
                    authUser(mail, password);
                }
            }
        });

        return view;
    }

    void authUser(String mail, String password){
        proceed.setAlpha(.4f);
        proceed.setEnabled(false);
        proceed.setText("Please Wait");

        loading.setVisibility(View.VISIBLE);

        error.setText("");

        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, XClass.apiSignIn, response ->{
            try {
                JSONObject object = new JSONObject(response);

                boolean logged = object.getBoolean("status");
                if (logged) {
                    //logged
                    Log.e("silvr", response);

                    String name = object.getString("name");
                    String phone_number = object.getString("number");
                    String address = object.getString("address");
                    String dob = object.getString("dob");
                    String gender = object.getString("gender");
                    String tenant = object.getString("tenant");

                    String auth = object.getString("auth");
                    String balance = object.getString("wallet");

                    SharedPreferences.Editor e = data.edit();
                    e.putString(XClass.mail, mail);
                    e.putString(XClass.name, name);
                    e.putString(XClass.line, phone_number);
                    e.putString(XClass.address, address);
                    e.putString(XClass.dob, dob);
                    e.putString(XClass.gender, gender);
                    e.putString(XClass.auth, auth);
                    e.putString(XClass.balance, balance);
                    e.apply();

                    startActivity(new Intent(cx, ActivityMain.class));
                    fx.finish();
                } else {
                    String message = object.getString("message");
                    proceed.setAlpha(1f);
                    proceed.setEnabled(true);
                    proceed.setText("Login");

                    error.setText(message);

                    loading.setVisibility(View.INVISIBLE);
                    Log.e("silvr", "err");
                    //not logged
                }
                Log.e("silvr", response);

            }catch (JSONException i){
                proceed.setAlpha(1f);
                proceed.setEnabled(true);
                proceed.setText("Login");

                loading.setVisibility(View.INVISIBLE);

                error.setText(i.getMessage());
            }
        }, err->{
            proceed.setAlpha(1f);
            proceed.setEnabled(true);
            proceed.setText("Login");

            error.setText(err.getMessage());

            loading.setVisibility(View.INVISIBLE);
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();
                post.put("mail", mail);
                post.put("password", password);
                return post;
            }
        };
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(request);
    }
}
