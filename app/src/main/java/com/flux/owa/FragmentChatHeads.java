package com.flux.owa;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FragmentChatHeads extends XFragment {

    private List<HashMap<String, Object>> simple_chat_heads;



    @SuppressWarnings("ConstantConditions")
    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_heads, parent, false);

        RelativeLayout chat_view = view.findViewById(R.id.chat_view);
        ListView list = view.findViewById(R.id.chat_head_list);


        final Realm realmInit = Realm.getDefaultInstance();
        final RealmResults<XHeads> heads;

        heads = realmInit.where(XHeads.class).findAll();
        heads.load();


        realmInit.beginTransaction();
        if (heads.size() == 1) {
            String chat_locale = heads.get(0).getLocale();
            String chat_title = heads.get(0).getName();
            String chat_header = heads.get(0).getDetails();
            String chat_agent = heads.get(0).getAgent();

            FragmentChat inquire = new FragmentChat();
            Bundle xnd = new Bundle();
            xnd.putString("title", chat_title);
            xnd.putString("header", chat_header);
            xnd.putString("locale", chat_locale);
            xnd.putString("agent", chat_agent);

            inquire.setArguments(xnd);
            fm.beginTransaction().add(R.id.fragment, inquire).commit();
        } else if (heads.size() > 1){
            chat_view.setVisibility(View.GONE);
            //show chat views

            simple_chat_heads = new ArrayList<>();

            for (int i = 0; i < heads.size(); i++){
                String name = heads.get(i).getName();
                String info = heads.get(i).getDetails();
                String locale = heads.get(i).getLocale();
                String agent = heads.get(i).getAgent();
                boolean isNotification = heads.get(i).isNotification();

                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("info", info);
                map.put("locale", locale);
                map.put("agent", agent);
                map.put("notification", isNotification);
                simple_chat_heads.add(map);
            }

            String[] from = {"name", "info"};
            int[] to = {R.id.chat_name, R.id.chat_info};

            SimpleAdapter adapter = new SimpleAdapter(cx, simple_chat_heads, R.layout.xml_chats, from, to){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    FrameLayout fl = view.findViewById(R.id.chat_notification);
                    boolean isNotification = (boolean) simple_chat_heads.get(position).get("notification");
                    if (isNotification) fl.setVisibility(View.VISIBLE);
                    return view;
                }
            };
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    /*
                    Realm db =  Realm.getDefaultInstance();

                    final XHeads ch = db.where(XHeads.class).equalTo("locale", simple_chat_heads.get(position).get("locale").toString()).findFirst();
                    db.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NotNull Realm realm) {
                            ch.setNotification(false);
                            realm.commitTransaction();
                            Log.e("silvr", "notification closd");
                        }
                    });
                    db.close();



                     */
                    String chat_title = (String) simple_chat_heads.get(position).get("name");
                    String chat_header = (String) simple_chat_heads.get(position).get("info");
                    String chat_locale = (String) simple_chat_heads.get(position).get("locale");
                    String chat_channel = (String) simple_chat_heads.get(position).get("channel");

                    FragmentChat inquire = new FragmentChat();
                    Bundle xnd = new Bundle();
                    xnd.putString("title", chat_title);
                    xnd.putString("header", chat_header);
                    xnd.putString("locale", chat_locale);
                    xnd.putString("channel", chat_channel);

                    inquire.setArguments(xnd);
                    fm.beginTransaction().add(R.id.fragment, inquire).addToBackStack(null).commit();
                }
            });
            list.setVisibility(View.VISIBLE);
        }

        String mail = data.getString(XClass.mail, null);
        realmInit.close();
        return view;
    }
}
