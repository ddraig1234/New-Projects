package com.example.zorawar.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class sav12 extends AppCompatActivity
{

    //public static int value_time=0;
    Button but;
    EditText text;
    Float temp;
    public static SharedPreferences limitshared;
    int x,y;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sav12);
        limitshared = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = limitshared.edit();
        temp = limitshared.getFloat("time_limit", 0.0f);
        text = (EditText) findViewById(R.id.disp_text);
        text.setText(String.valueOf(temp));
        NumberPicker np1=(NumberPicker)findViewById(R.id.np1);
        NumberPicker np2=(NumberPicker)findViewById(R.id.np2);
        np1.setMinValue(0);
        np1.setMaxValue(5);
        np1.setWrapSelectorWheel(false);
        np2.setMinValue(1);
        np2.setMaxValue(9);
        np2.setWrapSelectorWheel(false);
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                x = newVal;


            }
        });
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                y = newVal;


            }
        });


    }

    public void onClick(View v) {
        but = (Button) findViewById(R.id.but_sav1);
          {
            limitshared = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = limitshared.edit();
              float m=(float)(10*x+y);
            editor.putFloat("time_limit",m);
            editor.commit();
            text = (EditText) findViewById(R.id.disp_text);
            temp = limitshared.getFloat("time_limit", 0.0f);
            text.setText(String.valueOf(temp));
            calculation();

        }

    }

    public void calculation()
    {

        Float p = limitshared.getFloat("time_limit", 0.0f);
        long convertedtime= (long) (p*60000);
        SharedPreferences.Editor editor=limitshared.edit();
        editor.putLong("convertedtime", convertedtime);
        editor.commit();

        Long p1 = limitshared.getLong("convertedtime",0l);
      //  Toast.makeText(getBaseContext(),"time limit ->"+ String.valueOf(p1),Toast.LENGTH_LONG).show();
    }
}

