package com.avoupavou.qtcorrection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String LOG_TAG = "MainActivity";

    private String firstSelection;
    private String secondSelection;

    private double rr,hr, qt,bzt,frd,fmr;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        initSpinners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isFirstTime()){
            showDisclaimer();
        }
    }


    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean disclaimerSeen = preferences.getBoolean("DisclaimerSeen", false);

        if (!disclaimerSeen ) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("DisclaimerSeen", true);
            editor.commit();
        }
        return !disclaimerSeen;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent toSettings = new Intent(this,SettingsActivity.class);
                startActivity(toSettings);
                return true;

            case R.id.action_disclaimer:
                showDisclaimer();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void showDisclaimer() {
        DialogFragment newFragment = new MessageDialogFragment();
        newFragment.show(getSupportFragmentManager(),"Disclaimer");
    }



    private void initSpinners(){
        Spinner rr_spinner = (Spinner) findViewById(R.id.rr_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> rr_adapter = ArrayAdapter.createFromResource(this,
                R.array.rr_hr_units, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        rr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        rr_spinner.setAdapter(rr_adapter);
        rr_spinner.setOnItemSelectedListener(this);

        firstSelection = rr_spinner.getSelectedItem().toString();

        Spinner qr_spinner = (Spinner) findViewById(R.id.qr_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> qr_adapter = ArrayAdapter.createFromResource(this,
                R.array.qr_units, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        qr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        qr_spinner.setAdapter(qr_adapter);
        qr_spinner.setOnItemSelectedListener(this);

        secondSelection = qr_spinner.getSelectedItem().toString();

    }

    private void changeECGVisibility(int visible){
        findViewById(R.id.ecg_textview).setVisibility(visible);
        findViewById(R.id.ecg_edittext).setVisibility(visible);
        findViewById(R.id.ecgunit_textview).setVisibility(visible);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String option = parent.getItemAtPosition(position).toString();


        if(parent.getId() == R.id.rr_spinner ){
            firstSelection = parent.getSelectedItem().toString();
            if(option.equals("bits/sec")){
                TextView rrtext = (TextView) findViewById(R.id.rr_textview);
                rrtext.setText(R.string.hr_string);
            }else{
                TextView rrtext = (TextView) findViewById(R.id.rr_textview);
                rrtext.setText(R.string.rr_string);
            }
            ((EditText) findViewById(R.id.rr_edittext)).requestFocus();
        }else{
            ((EditText) findViewById(R.id.qr_edittext)).requestFocus();
            secondSelection = parent.getSelectedItem().toString();
        }

        if(firstSelection.equals("mm") || secondSelection.equals("mm")){
            changeECGVisibility(View.VISIBLE);
        }else{
            changeECGVisibility(View.INVISIBLE);
        }



    }

    public void onGOClick(View view){
        calculateValues();
        drawValues();
    }

    void drawValues(){
        findViewById(R.id.bzt_textview).setVisibility(View.VISIBLE);
        findViewById(R.id.frd_textview).setVisibility(View.VISIBLE);
        findViewById(R.id.fmr_textview).setVisibility(View.VISIBLE);

        TextView bztTextview = (TextView)findViewById(R.id.bzt_result_textview);
        TextView frdTextview = (TextView)findViewById(R.id.frd_result_textview);
        TextView fmrTextview = (TextView)findViewById(R.id.fmr_result_textview);
        TextView hrTextview = (TextView)findViewById(R.id.hr_result_textview);
        TextView qtTextview = (TextView)findViewById(R.id.qt_result_textview);

        bztTextview.setVisibility(View.VISIBLE);
        frdTextview.setVisibility(View.VISIBLE);
        fmrTextview.setVisibility(View.VISIBLE);
        hrTextview.setVisibility(View.VISIBLE);
        qtTextview.setVisibility(View.VISIBLE);

        DecimalFormat formatter = new DecimalFormat("#,###");

        String bztFormatted = formatter.format((int)Math.ceil(bzt));
        String frdFormatted = formatter.format((int)Math.ceil(frd));
        String fmrFormatted = formatter.format((int)Math.ceil(fmr));
        String hrFormatted = formatter.format((int)Math.ceil(hr));
        String qtFormatted = formatter.format((int)Math.ceil(qt));

        bztTextview.setText(bztFormatted+" msec");
        frdTextview.setText(frdFormatted+" msec");
        fmrTextview.setText(fmrFormatted+" msec");
        hrTextview.setText (hrFormatted+" bits/sec");
        qtTextview.setText (qtFormatted +" msec");

        if(hr > 60 && hr < 100) bztTextview.setTypeface(Typeface.DEFAULT_BOLD);
        else bztTextview.setTypeface(Typeface.DEFAULT);
        if(hr > 100)  frdTextview.setTypeface(Typeface.DEFAULT_BOLD);
        else frdTextview.setTypeface(Typeface.DEFAULT);

    }

    void calculateValues(){

        double firstValue;
        double secondValue;
        double ecgSpeed;

        String firstText = ((EditText) findViewById(R.id.rr_edittext)).getText().toString();
        String secondText = ((EditText) findViewById(R.id.qr_edittext)).getText().toString();
        String  ecgText = ((EditText) findViewById(R.id.ecg_edittext)).getText().toString();

        if(firstText.equals("")){
            ((EditText) findViewById(R.id.rr_edittext)).setError(getString(R.string.input_error_string));
        }

        if(secondText.equals("")){
            ((EditText) findViewById(R.id.qr_edittext)).setError(getString(R.string.input_error_string));
        }

        if(ecgText.equals("")){
            ((EditText) findViewById(R.id.ecg_edittext)).setError(getString(R.string.ecg_error_string));
        }

        try {
            firstValue = Double.parseDouble(firstText);
            secondValue = Double.parseDouble(secondText);
            ecgSpeed = Double.parseDouble(ecgText);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        if(firstSelection.equals("bits/sec")){
            hr = firstValue;
            rr = 60 / hr;
        }else if(firstSelection.equals("mm")){
            rr = firstValue / ecgSpeed;
            hr = 60/rr;

        }else{
            rr = firstValue;
            hr = 60/rr;
        }

        if(secondSelection.equals("mm")){
            qt = secondValue / ecgSpeed;
        }else{
            qt = secondValue;
        }

        bzt = 1000.0f * (qt /sqrt(rr));
        frd = 1000.0f * (qt /Math.cbrt(rr));
        fmr = 1000.0f * (qt + 0.154 *(1-rr));

        qt *= 1000;

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
