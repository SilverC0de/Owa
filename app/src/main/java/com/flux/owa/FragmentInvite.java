package com.flux.owa;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentInvite extends XFragment {

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_invite, child, false);

        ImageView back = view.findViewById(R.id.back);

        LinearLayout facebook = view.findViewById(R.id.facebook);
        LinearLayout twitter = view.findViewById(R.id.twitter);
        LinearLayout email = view.findViewById(R.id.email);
        LinearLayout whatsapp = view.findViewById(R.id.whatsapp);
        LinearLayout sms = view.findViewById(R.id.sms);

        facebook.setOnClickListener(v-> shareImageWithText());
        twitter.setOnClickListener(v-> shareImageWithText());
        whatsapp.setOnClickListener(v-> shareImageWithText());

        back.setOnClickListener(v-> fm.popBackStack());

        return view;
    }
    void shareImageWithText(){
        Uri contentUri = Uri.parse("android.resource://" + cx.getPackageName() + "/drawable/" + "ic_launcher");

        StringBuilder msg = new StringBuilder();
        msg.append("Hey, Download this awesome app! I totally recommend it for reserving an apartment anywhere in Nigeria");
        msg.append("\n");
        msg.append("https://play.google.com/store/apps/details?id=com.flux.owa"); //example :com.package.name

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setType("*/*");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg.toString());
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            try {
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(cx, "No App Available", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
