package com.example.zorawar.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private static Button but_mot;
    private static Button but_sav;
    private static Button but_test;
    private static Button but_alt;
    private static Button but_inc;
    private static Button but_pro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onButtonClickListener();

    }

    public void onButtonClickListener()
    {

            but_mot=(Button)findViewById(R.id.but_mot);
            but_sav=(Button)findViewById(R.id.but_sav);
           but_alt=(Button)findViewById(R.id.but_alt);
            but_inc=(Button)findViewById(R.id.but_alarm);
            but_pro=(Button)findViewById(R.id.but_profile);

          but_mot.setOnClickListener(
                  new View.OnClickListener() {
                      public void onClick(View v) {
                          Intent i = new Intent("com.example.zorawar.myapplication.mot11");
                          startActivity(i);
                      }
                  }
          );

        but_sav.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent("com.example.zorawar.myapplication.sav11");
                        startActivity(i);
                    }
                }
        );



        but_alt.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent("com.example.zorawar.myapplication.alt11");
                        startActivity(i);
                    }
                }
        );

        but_inc.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent("com.example.zorawar.myapplication.but_alarm11");
                        startActivity(i);
                    }
                }
        );
        but_pro.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent("com.example.zorawar.myapplication.pro51");
                        startActivity(i);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick(View view) {
    }
}
