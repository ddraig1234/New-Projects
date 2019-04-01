package com.example.zorawar.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class sav11 extends Activity implements View.OnClickListener
{

    private static Button button1,button2,button3;
    TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sav11);
        button1 = (Button) findViewById(R.id.but_s1);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.but_s2);
        button2.setOnClickListener(this);
        button3=(Button)findViewById(R.id.angle);
        button3.setOnClickListener(this);
        //onButtonClickListener();
   //     display_time();
    }

    public void onClick(View view) {
        Intent start = new Intent(this, Service_sav.class);
        switch (view.getId()) {
            case R.id.but_s1:
                Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
                this.startService(start);
                break;
            case R.id.but_s2:
                Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
                this.stopService(start);
                break;
            case R.id.angle:
                Intent i = new Intent("com.example.zorawar.myapplication.sav12");
                startActivity(i);
         }

    }
}
