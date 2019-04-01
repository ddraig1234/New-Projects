package com.example.zorawar.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static java.lang.Math.abs;

public class Service_sav extends Service implements SensorEventListener
{
    private boolean sersorrunning;
    SharedPreferences sensorvalues;
    private static SensorManager mSensorManager;
    private static Sensor mSensor;
    private float[] history=new float[3];
    private  int flag=0;
    public String currentState = "no motion";
    public String dataState = "no";
    private  Toast t;
   private boolean mbd=false,wifi=false;
    private Long prev;
    private int count=0,m=1;



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public void onStart(final Intent intent, int startid) {

        Toast.makeText(this, "STARTED....", Toast.LENGTH_SHORT).show();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorvalues=getSharedPreferences("Mydata",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sensorvalues.edit();
        editor.putLong("lastupdatingtime", System.currentTimeMillis());
        //prev=System.currentTimeMillis();
        editor.commit();

    }


    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();


        mSensorManager.unregisterListener(this);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        SharedPreferences limitshared = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        phonemoved(event);
        if (currentState.equals("motion")) {
            sensorvalues = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            Long convertedtime = sensorvalues.getLong("convertedtime", 0l);

            Long lastupdate = sensorvalues.getLong("lastupdatingtime", 0l);
            Long time = System.currentTimeMillis();
            if (flag == 1 && convertedtime > lastupdate - prev) {
                 if (mbd||wifi){
                     setMobileDataState(true);
                     if(mbd)
                     Toast.makeText(getBaseContext(), " DataPack has been switched on ", Toast.LENGTH_SHORT).show();
                     if(wifi)
                         Toast.makeText(getBaseContext(), " WIFI has been switched on ", Toast.LENGTH_SHORT).show();

                 }else {
              //  Toast.makeText(getBaseContext(), " previous state was switched off ", Toast.LENGTH_SHORT).show();
                 }
                flag = 0;
                count = 0;
            } else if ((lastupdate - prev) < convertedtime) {
                t = Toast.makeText(getBaseContext(), "MOVED within limit", Toast.LENGTH_SHORT);

                //   t.setGravity(Gravity.TOP,5,5);t.show();


            }

        } else {

            sensorvalues = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            Long convertedtime = sensorvalues.getLong("convertedtime", 0l);
            Long time = System.currentTimeMillis();
            Long lastupdate = sensorvalues.getLong("lastupdatingtime", 0l);
            if (m != 1) {
                if (((time - lastupdate) < convertedtime) && count == 0) {

                    t = Toast.makeText(getBaseContext(), "no motion ,within time limit", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.RIGHT, 0, 0);
                    //    t.show();
                    count = 1;
                } else if (((time - lastupdate) > convertedtime) && flag == 0) {
                    flag = 1;
                    getMobileDataState(getBaseContext());
                    if (mbd||wifi) {
                       setMobileDataState(false);
                        if(mbd)
                            Toast.makeText(getBaseContext(), " DataPack has been switched off ", Toast.LENGTH_SHORT).show();
                        if(wifi)
                            Toast.makeText(getBaseContext(), " WIFI has been switched off ", Toast.LENGTH_SHORT).show();

                    } else {
                        //Toast.makeText(getBaseContext(), "Data is already off ", Toast.LENGTH_SHORT).show();
                    }  }
            } else if (m == 1) {
                m = 0;
                time = System.currentTimeMillis();
                if (((time - lastupdate) > convertedtime)) {
                    flag = 1;
                     getMobileDataState(getBaseContext());
                    if (mbd||wifi) {
                        setMobileDataState(false);
                        Toast.makeText(getBaseContext(), "WIFI has been switched off", Toast.LENGTH_SHORT).show();
                    } else {
                    //Toast.makeText(getBaseContext(), "Data is already off", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        }
    }
    public void setMobileDataState(boolean enabled)
    {   if(mbd){
        try {
            ConnectivityManager dataManager;
            dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            dataMtd.setAccessible(true);
            dataMtd.invoke(dataManager, enabled);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }}
        if(wifi){
      //  Toast.makeText(this,"mobile data enabled",Toast.LENGTH_SHORT).show();
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    } }
    public void getMobileDataState(Context mContext) {
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected)
        {
            if(activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE)
                mbd=true;
            if(activeNetwork.getType()==ConnectivityManager.TYPE_WIFI)
                wifi=true;
        }
    }

    public void phonemoved(SensorEvent event) {
        float xChange = history[0] - event.values[0];
        float yChange = history[1] - event.values[1];
        float zChange=  history[2] - event.values[2];

        history[0] = event.values[0];
        history[1] = event.values[1];
        history[2] = event.values[2];
        sensorvalues=getSharedPreferences("Mydata",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sensorvalues.edit();
        if ( (xChange > 3)||(xChange < -3)||(yChange > 3)||(yChange < -3)||(zChange > 3)||(zChange < -3))
        {
            prev=sensorvalues.getLong("lastupdatingtime",0l);
            editor.putLong("lastupdatingtime", System.currentTimeMillis());
            editor.commit();
            currentState="motion";
        }else{
            currentState="no motion";
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
