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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
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

public class FragmentRegister extends XFragment {


    LottieAnimationView loading;
    Button proceed;
    TextView error;

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, parent, false);


        TextView login = view.findViewById(R.id.login);
        EditText find_mail = view.findViewById(R.id.access_mail);
        EditText find_name = view.findViewById(R.id.access_name);
        EditText find_password = view.findViewById(R.id.access_password);
        loading = view.findViewById(R.id.loading);
        error = view.findViewById(R.id.error);
        proceed = view.findViewById(R.id.access_continue);

        login.setOnClickListener(v-> fm.beginTransaction().replace(R.id.fragment, new FragmentLogin()).commit());
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = find_mail.getText().toString().trim();
                String password = find_password.getText().toString().trim();
                String name = find_name.getText().toString().trim();
                if (mail.isEmpty()){
                    error.setText("Please enter a valid mail address");
                } if (password.isEmpty()){
                    error.setText("Please enter your password");
                } else {
                    error.setText("");
                    authUser(mail, name, password);
                }
            }
        });

        return view;
    }

    void authUser(String mail, String name, String password){
        proceed.setAlpha(.4f);
        proceed.setEnabled(false);
        proceed.setText("Please Wait");

        loading.setVisibility(View.VISIBLE);


        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, XClass.apiRegister, response ->{
            try {
                JSONObject object = new JSONObject(response);
                boolean logged = object.getBoolean("status");
                if (logged) {
                    //logged
                    SharedPreferences.Editor e = data.edit();
                    e.putString(XClass.mail, mail);
                    e.putString(XClass.name, name);
                    e.apply();

                    startActivity(new Intent(cx, ActivityMain.class));
                    fx.finish();
                } else {
                    String message = object.getString("message");
                    proceed.setAlpha(1f);
                    proceed.setEnabled(true);
                    proceed.setText("Create Account");

                    loading.setVisibility(View.INVISIBLE);
                    error.setText(message);
                }
                Log.e("silvr", response);

            } catch (JSONException i) {
                proceed.setAlpha(1f);
                proceed.setEnabled(true);
                proceed.setText("Create Account");

                loading.setVisibility(View.INVISIBLE);

                error.setText(i.getMessage());
            }

            Log.e("silvr", "" + response);
        }, err -> {
            proceed.setAlpha(1f);
            proceed.setEnabled(true);
            proceed.setText("Create Account");

            loading.setVisibility(View.INVISIBLE);

            error.setText(err.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();
                post.put("mail", mail);
                post.put("name", name);
                post.put("password", password);
                return post;
            }
        };
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(request);
    }
}