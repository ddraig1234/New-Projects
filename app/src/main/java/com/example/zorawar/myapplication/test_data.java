package com.example.zorawar.myapplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;



public class test_data extends Activity {

    Method dataConnSwitchmethod_ON;
    Method dataConnSwitchmethod_OFF;
    Class telephonyManagerClass;
    Object ITelephonyStub;
    Class ITelephonyClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_test);

        GetDataConnectionAPI();

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getBaseContext(),"data is OFF",Toast.LENGTH_SHORT).show();
                    turn3GOn();
                } else {
                    Toast.makeText(getBaseContext(),"data is ON",Toast.LENGTH_SHORT).show();
                    turn3GOff();
                }
            }
        });

    }

    private void GetDataConnectionAPI() {
        this.getApplicationContext();
        TelephonyManager telephonyManager =
                (TelephonyManager) this.getApplicationContext().
                        getSystemService(Context.TELEPHONY_SERVICE);

        try {
            telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
            Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
            getITelephonyMethod.setAccessible(true);
            ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
            ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());

            dataConnSwitchmethod_OFF =
                    ITelephonyClass.getDeclaredMethod("disableDataConnectivity");
            dataConnSwitchmethod_ON = ITelephonyClass.getDeclaredMethod("enableDataConnectivity");
        } catch (Exception e) { // ugly but works for me
            e.printStackTrace();
        }
    }
    private void turn3GOn() {
        dataConnSwitchmethod_ON.setAccessible(true);
        try {
            dataConnSwitchmethod_ON.invoke(ITelephonyStub);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void turn3GOff() {
        dataConnSwitchmethod_OFF.setAccessible(true);
        try {
            dataConnSwitchmethod_OFF.invoke(ITelephonyStub);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
