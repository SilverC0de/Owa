package com.flux.owa;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragmentAccount extends XFragment implements AdapterView.OnItemSelectedListener {

    private String dob, mail;
    private Spinner edit_gender;
    private EditText edit_name, edit_number, edit_address;
    private ImageView pencil_name, pencil_number, pencil_address;
    private TextView account_name;
    private TextView edit_dob;


    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,  parent, false);


        TextView account_mail = view.findViewById(R.id.account_mail);

        face.getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        if (Build.VERSION.SDK_INT > 22) face.getWindow().getDecorView().setSystemUiVisibility(face.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mail = data.getString(XClass.mail, null);
        account_name = view.findViewById(R.id.account_name);

        edit_name = view.findViewById(R.id.edit_name);
        edit_number = view.findViewById(R.id.edit_number);
        edit_address = view.findViewById(R.id.edit_address);
        edit_gender = view.findViewById(R.id.edit_gender);
        edit_dob = view.findViewById(R.id.edit_dob);

        pencil_name = view.findViewById(R.id.pencil_name);
        pencil_number = view.findViewById(R.id.pencil_number);
        pencil_address = view.findViewById(R.id.pencil_address);

        String xname = data.getString(XClass.name, XClass.outcast);
        String xmail = data.getString(XClass.mail, XClass.outcast);
        String xnumber = data.getString(XClass.number, XClass.outcast);
        String xdob = data.getString(XClass.dob, XClass.outcast);

        account_name.setText(xname);
        account_mail.setText(xmail);

        edit_name.setText(xname);
        edit_number.setText(xnumber);
        edit_dob.setText(xdob);

        initializeEditor();
        return view;
    }


    private void initializeEditor(){
        ArrayAdapter<CharSequence> bed_adapter = ArrayAdapter.createFromResource(cx, R.array.gender, R.layout.xml_spinner);
        bed_adapter.setDropDownViewResource(R.layout.xml_spinner);
        edit_gender.setAdapter(bed_adapter);


        pencil_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_name.setEnabled(true);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        edit_name.requestFocus();
                        InputMethodManager im = (InputMethodManager) cx.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (im != null) im.showSoftInput(edit_name, InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                pencil_name.setImageResource(R.drawable.save);
                pencil_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pencil_name.setImageResource(R.drawable.pencil);

                        String name = edit_name.getText().toString().trim();
                        edit_name.setEnabled(false);

                        account_name.setText(name);

                        saveData(XClass.name, name);
                        XClass.updateUser("name", name, mail);
                    }
                });
            }
        });

        pencil_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_number.setEnabled(true);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        edit_number.requestFocus();
                        InputMethodManager im = (InputMethodManager) cx.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (im != null) im.showSoftInput(pencil_number, InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                pencil_number.setImageResource(R.drawable.save);
                pencil_number.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pencil_number.setImageResource(R.drawable.pencil);

                        String number = edit_number.getText().toString().trim();
                        edit_number.setEnabled(false);

                        saveData(XClass.number, number);
                        XClass.updateUser("number", number, mail);
                    }
                });
            }
        });

        pencil_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_address.setEnabled(true);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        edit_address.requestFocus();
                        InputMethodManager im = (InputMethodManager) cx.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (im != null) im.showSoftInput(edit_address, InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                pencil_address.setImageResource(R.drawable.save);
                pencil_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pencil_address.setImageResource(R.drawable.pencil);

                        String addr = edit_address.getText().toString().trim();
                        edit_address.setEnabled(false);

                        saveData(XClass.address, addr);
                        XClass.updateUser(XClass.address, addr, mail);
                    }
                });
            }
        });



        edit_gender.setOnItemSelectedListener(this);

        edit_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(cx,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                                Calendar cc = Calendar.getInstance();
                                int day = view.getDayOfMonth();
                                int month = view.getMonth();
                                int year =  view.getYear();
                                cc.set(year, month, day);
                                dob = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(cc.getTime()); //25 Jan 2019
                                edit_dob.setText(dob);
                                saveData(XClass.dob, dob);
                                XClass.updateUser("dob", dob, mail);

                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }


    private void saveData(String name, String value){

        SharedPreferences.Editor e = data.edit();
        e.putString(name, value);
        e.apply();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String gender = parent.getItemAtPosition(position).toString();
        saveData(XClass.gender, gender);
        XClass.updateUser("gender", gender, mail);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}