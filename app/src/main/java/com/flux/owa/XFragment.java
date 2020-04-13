package com.flux.owa;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import okhttp3.OkHttpClient;

public abstract class XFragment extends Fragment {

    Activity face;
    Context cx;
    SharedPreferences data;
    FragmentManager fm;
    LinearLayout navigation;
    OkHttpClient httpClient;
    String mail;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cx = context;
        face = getActivity();
        fm = getFragmentManager();
        httpClient = new OkHttpClient();
        data = cx.getSharedPreferences(XClass.sharedPreferences, Context.MODE_PRIVATE);
        mail = data.getString(XClass.mail, null);
        face.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        if (Build.VERSION.SDK_INT > 22) face.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        navigation = face.findViewById(R.id.navigation);
        return baseFragment(inflater, container, savedInstanceState);
    }


    public abstract View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    /*
    void init(){
        String buildAPI = data.getString(XClass.buildAPI, XClass.buildAPI);
        if (!Objects.equals(XClass.buildAPI, buildAPI)){
            startActivity(new Intent(cx, ActivityBuild.class));
            face.finish();
        }
    }

     */
}