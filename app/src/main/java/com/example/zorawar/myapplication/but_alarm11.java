package com.example.zorawar.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class but_alarm11 extends AppCompatActivity {

    private static Button but_ala;
    private static ToggleButton tog_but;
    private static SensorManager mySensorManager;
    private boolean sersorrunning;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm11);
        tog_but = (ToggleButton) findViewById(R.id.tbtn_alarm1);
        but_ala = (Button) findViewById(R.id.but_alarm1);
        SharedPreferences sharedPrefs = getSharedPreferences("com.example.xyle", MODE_PRIVATE);
        tog_but.setChecked(sharedPrefs.getBoolean("NameOfThingToSave", true));
        onButtonClickListener();
    }

    public void onButtonClickListener() {

       // but_ala = (Button) findViewById(R.id.but_alarm1);
        but_ala.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent("com.example.zorawar.myapplication.but_alarm12");
                        startActivity(i);
                    }
                }
        );

       // tog_but = (ToggleButton) findViewById(R.id.tbtn_alarm1);
        tog_but.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //SharedPreferences.Editor editor = getSharedPreferences("Mydata", Context.MODE_PRIVATE).edit();
                    //editor.putBoolean("NameOfThingToSave", true);
                    //editor.commit();
                    //  Toast.makeText(getApplicationContext(), "Service is Off", Toast.LENGTH_SHORT).show();
                    start(buttonView);


                } else {
                    // SharedPreferences.Editor editor = getSharedPreferences("Mydata", Context.MODE_PRIVATE).edit();
                    // editor.putBoolean("NameOfThingToSave", false);
                    // editor.commit();
                    //   Toast.makeText(getApplicationContext(), "Service is On", Toast.LENGTH_SHORT).show();
                    stop(buttonView);

                }
            }
        });
    }
    private void stop(CompoundButton buttonView)
    {
        Intent i=new Intent(getBaseContext(),Service_incalarm.class);
        Toast.makeText(getApplicationContext(), "Service is Off and stopped", Toast.LENGTH_LONG).show();
        stopService(i);
    }

    private void start(CompoundButton buttonView)
    {
        Intent i=new Intent(getBaseContext(),Service_incalarm.class);
        Toast.makeText(getApplicationContext(), "Service is On and started", Toast.LENGTH_LONG).show();
        startService(i);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
