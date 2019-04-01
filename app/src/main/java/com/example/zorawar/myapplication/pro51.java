package com.example.zorawar.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;
public class pro51 extends Activity implements View.OnClickListener {

    Button button1,button2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro51);
        button1 = (Button) findViewById(R.id.but_s1);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.but_s2);
        button2.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent start = new Intent(this, Service_pro.class);
        switch (view.getId()) {
            case R.id.but_s1:
                Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
                this.startService(start);
                break;
            case R.id.but_s2:
                Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
                this.stopService(start);
                break;
        }

    }
}