package com.flux.owa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragmentReserve extends XFragment implements AdapterView.OnItemSelectedListener {

    private int param_month = 0;
    private int param_cost = 0;
    private int param_total = 0;
    private View view;
    private Bundle xnd;
    private Calendar now, instance;
    private TextView availability;
    private String x_check_in, x_check_out;
    private boolean tvAdded = false,  sofaAdded = false, bedAdded = false;




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT > 22) face.getWindow().getDecorView().setSystemUiVisibility(face.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        face.getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tvAdded = false;
        sofaAdded = false;
        bedAdded = false;
    }

    @Override
    public View baseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reserve, parent, false);
        face.getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));

        availability = view.findViewById(R.id.calculate_availability);
        String guest = "1";
        now = Calendar.getInstance();
        xnd = getArguments();
        if (xnd != null) {
            guest = xnd.getString("guest");
            param_cost = xnd.getInt("cost");
        }

        Spinner month_spinner = view.findViewById(R.id.calculate_months);
        Spinner guest_spinner = view.findViewById(R.id.calculate_guests);
        Button reserve = view.findViewById(R.id.reserve);




        ArrayAdapter<CharSequence> months_adapter = ArrayAdapter.createFromResource(cx, R.array.months, R.layout.xml_spinner);

        months_adapter.setDropDownViewResource(R.layout.xml_spinner);
        month_spinner.setAdapter(months_adapter);
        month_spinner.setOnItemSelectedListener(this);

        guest = (guest == null) ? "1" : guest;

        initializeMonthsCalculator(0);
        initializeGuestView(guest_spinner, guest);
        initializeView(view);



        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (param_month == 0){
                    Toast.makeText(face, "Please specify month duration", Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog dl = new Dialog(face);
                    dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dl.setContentView(R.layout.xml_agreement);
                    CheckBox check = dl.findViewById(R.id.agree_checker);
                    check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Button agree = dl.findViewById(R.id.agree);
                            if (isChecked){
                                agree.setAlpha(1f);
                                agree.setEnabled(true);
                                agree.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FragmentXOne xOne = new FragmentXOne();

                                        xnd.putString("check_in", x_check_in);
                                        xnd.putString("check_out", x_check_out);
                                        xnd.putString("period", String.valueOf(param_month));
                                        xnd.putString("total", String.valueOf(param_total));
                                        xOne.setArguments(xnd);
                                        fm.beginTransaction().add(R.id.fragment, xOne).addToBackStack(null).commit();

                                        String msg = "TV: " + tvAdded + ", sofa added: " + sofaAdded + " Bed added: " + bedAdded;
                                        XClass.updateUser("message", msg, mail);

                                        dl.cancel();
                                    }
                                });
                            } else {
                                agree.setAlpha(.4f);
                                agree.setEnabled(false);
                            }
                        }
                    });
                    dl.show();
                }
            }
        });
        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(cx,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yearOfCentury, int monthOfYear, int dayOfMonth) {
                                now.set(view.getYear(), view.getMonth(), view.getDayOfMonth());
                                String date = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(now.getTime());
                                availability.setText(date);
                                //instance = now;
                                initializeMonthsCalculator(param_month);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        return view;
    }


    private void initializeGuestView(Spinner spinner, String guest){
        ArrayAdapter<CharSequence> apartment_adapter;
        switch (guest){
            case "2":
                apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.two_guests, R.layout.xml_spinner);
                break;
            case "3":
                apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.three_guests, R.layout.xml_spinner);
                break;
            case "4":
                apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.four_guests, R.layout.xml_spinner);
                break;
            case "5":
                apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.five_guests, R.layout.xml_spinner);
                break;
            default:
                apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.one_guest, R.layout.xml_spinner);
        }
        apartment_adapter.setDropDownViewResource(R.layout.xml_spinner);
        spinner.setAdapter(apartment_adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void initializeView(View v){
        Spinner bedSpinner = v.findViewById(R.id.calculate_bedframe);
        ArrayAdapter<CharSequence> apartment_adapter;
        apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.option, R.layout.xml_spinner);
        apartment_adapter.setDropDownViewResource(R.layout.xml_spinner);
        bedSpinner.setAdapter(apartment_adapter);
        bedSpinner.setOnItemSelectedListener(this);

        Spinner tvSpinner = v.findViewById(R.id.calculate_tv);
        ArrayAdapter<CharSequence> tv_adapter;
        tv_adapter = ArrayAdapter.createFromResource(cx, R.array.option, R.layout.xml_spinner);
        tv_adapter.setDropDownViewResource(R.layout.xml_spinner);
        tvSpinner.setAdapter(tv_adapter);
        tvSpinner.setOnItemSelectedListener(this);

        Spinner sofaSpinner = v.findViewById(R.id.calculate_sofa);
        ArrayAdapter<CharSequence> sofa_adapter;
        sofa_adapter = ArrayAdapter.createFromResource(cx, R.array.option, R.layout.xml_spinner);
        sofa_adapter.setDropDownViewResource(R.layout.xml_spinner);
        sofaSpinner.setAdapter(sofa_adapter);
        sofaSpinner.setOnItemSelectedListener(this);
    }

    private void initializeMonthsCalculator(int count){
        int months = count * 30;
        TextView total = view.findViewById(R.id.total);

        TextView period_preview = view.findViewById(R.id.calculate_months_preview);
        TextView period_cost = view.findViewById(R.id.calculate_months_solution);

        TextView check_in = view.findViewById(R.id.check_in);
        TextView check_out = view.findViewById(R.id.check_out);

        Calendar future = Calendar.getInstance();

        String in = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH).format(now.getTime()); //25 Jan 2019
        check_in.setText(in);
        x_check_in = in;

        future.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        future.add(Calendar.DAY_OF_YEAR, months);

        String out = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH).format(future.getTime()); //25 Jan 2019
        check_out.setText(out);
        x_check_out = out;




        String preview = count + " Months x " + cx.getResources().getString(R.string.naira) + NumberFormat.getNumberInstance().format(param_cost);
        period_preview.setText(preview);

        int cost_month = count * param_cost;
        period_cost.setText(String.format("%s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(cost_month)));


        param_month = count;
        param_total = count * param_cost;
        total.setText(String.format("Total: %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(param_total)));
    }


    private void calculateGuests(int count){
        TextView guest_preview = view.findViewById(R.id.calculate_guests_preview);
        TextView guest_cost = view.findViewById(R.id.calculate_guests_solution);
        TextView total = view.findViewById(R.id.total);

        String preview = count + " Guests x " + cx.getResources().getString(R.string.naira) + NumberFormat.getNumberInstance().format(param_cost);
        guest_preview.setText(preview);

        int cost_guest = count * param_cost;
        guest_cost.setText(MessageFormat.format("{0}{1}", cx.getResources().getString(R.string.naira), cost_guest));

        param_total = param_month * param_cost;

        total.setText(String.format("Total: %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(param_total)));
    }

    private void calculateBed(boolean addBed){
        if (addBed && !bedAdded){
            bedAdded = true;
            TextView total = view.findViewById(R.id.total);

            String bed = "50000";

            param_total = param_total + 50000;
            total.setText(String.format("Total: %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(param_total)));
        } else if (!addBed && bedAdded){
            bedAdded = false;
            TextView total = view.findViewById(R.id.total);

            String bed = "50000";

            param_total = param_total - 50000;
            total.setText(String.format("Total: %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(param_total)));
        }
    }

    private void calculateTV(boolean addTV){
        if (addTV && !tvAdded){
            tvAdded = true;
            TextView total = view.findViewById(R.id.total);

            String goTV = "12000";
            String screenTV = "35000";
            String tv = "47000";

            param_total = param_total + 47000;
            total.setText(String.format("Total: %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(param_total)));
        } else if (!addTV && tvAdded){
            tvAdded = false;
            TextView total = view.findViewById(R.id.total);

            String goTV = "12000";
            String screenTV = "35000";
            String tv = "47000";

            param_total = param_total - 47000;
            total.setText(String.format("Total: %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(param_total)));
        }
    }

    private void calculateSofa(boolean addSofa){
        if (addSofa && !sofaAdded){
            sofaAdded = true;
            TextView total = view.findViewById(R.id.total);

            String sofa = "120000";

            param_total = param_total + 120000;
            total.setText(String.format("Total: %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(param_total)));
        } else if (!addSofa && sofaAdded){
            sofaAdded = false;
            TextView total = view.findViewById(R.id.total);

            String sofa = "120000";

            param_total = param_total - 120000;
            total.setText(String.format("Total: %s%s", cx.getResources().getString(R.string.naira), NumberFormat.getNumberInstance().format(param_total)));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.calculate_guests:
                calculateGuests(position);
                break;
            case R.id.calculate_months:
                initializeMonthsCalculator(position);
                break;
            case R.id.calculate_bedframe:
                calculateBed(parent.getItemAtPosition(position).toString().equals("Yes"));
                break;
            case R.id.calculate_tv:
                calculateTV(parent.getItemAtPosition(position).toString().equals("Yes"));
                break;
            case R.id.calculate_sofa:
                calculateSofa(parent.getItemAtPosition(position).toString().equals("Yes"));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}