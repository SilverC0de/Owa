package com.flux.owa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.realm.Realm;

public class FragmentXTwo extends XFragment {

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xtwo, parent, false);

        final EditText message = view.findViewById(R.id.message);
        final EditText name = view.findViewById(R.id.name);
        final EditText number = view.findViewById(R.id.number);
        ImageView back = view.findViewById(R.id.back);
        Button proceed = view.findViewById(R.id.step_two_agree);

        String n = data.getString(XClass.name, XClass.outcast);
        String contact = data.getString(XClass.number, XClass.outcast);
        name.setText(n);
        number.setText(contact);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nme = name.getText().toString();
                String demands = message.getText().toString();
                String contact = number.getText().toString();
                if (contact.length() > 10 && !demands.isEmpty()){
                    InputMethodManager im = (InputMethodManager)cx.getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(im).hideSoftInputFromWindow(v.getWindowToken(), 0);

                    sendMessage(nme, contact, demands);
                } else {
                    Toast.makeText(face, "Tell us what brings you here", Toast.LENGTH_SHORT).show();
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

    private void sendMessage(final String nme, final String contact, final String demands) {

        String xname = XClass.outcast;
        String xlocale = XClass.outcast;
        String xAgent = XClass.outcast;

        Bundle xnd = getArguments();
        if (xnd != null){
            xname = xnd.getString("name");
            xlocale = xnd.getString("locale");
            xAgent = xnd.getString("agent");
        }

        XClass.pushMessage(mail, demands, xlocale, xAgent);

        final String chat_head = xAgent + " is in charge of this apartment";


        final Realm realmWriter = Realm.getDefaultInstance();

        final XHeads heads = new XHeads();
        heads.setName(xname);
        heads.setDetails(chat_head);
        heads.setLink(xlocale);


        final XChats chats = new XChats();
        chats.setType("user");
        chats.setMessage(demands);
        chats.setLocale(xlocale);
        chats.setStamp(System.currentTimeMillis());


        final String finalXname = xname;
        final String finalXlocale = xlocale;
        final String finalXAgent = xAgent;

        realmWriter.executeTransaction(new Realm.Transaction() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void execute(@NotNull Realm realm) {
                realm.copyToRealmOrUpdate(heads);
                realm.copyToRealmOrUpdate(chats);

                String mail = data.getString(XClass.mail, null);
                XClass.updateUser("name", nme, mail);
                XClass.updateUser("number", contact, mail);

                SharedPreferences.Editor e = data.edit();
                e.putString(XClass.number, contact);
                e.putString(XClass.name, nme);
                e.apply();

                //send message to admin
                FragmentXThree xThree = new FragmentXThree();
                xThree.setArguments(getArguments());
                fm.beginTransaction().add(R.id.fragment, xThree).addToBackStack(null).commit();
            }
        });
        realmWriter.close();
    }
}