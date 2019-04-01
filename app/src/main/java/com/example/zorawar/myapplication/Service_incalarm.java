package com.example.zorawar.myapplication;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

import static java.lang.Math.abs;

public class Service_incalarm extends Service implements SensorEventListener
{
   private static SensorManager mySensorManager;
   private boolean sersorrunning;
   SharedPreferences sensorvalues;
   Vibrator v;
   public static int count = 0;
    public static int c = 0;
   public boolean flag = false;
   public static boolean first = true;
   public static boolean firsttime = true;
    private Intent intent;
    private int flags;
    private int startId;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public boolean phonemoved(SensorEvent event)
    {
        count++;
        sensorvalues = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sensorvalues.edit();
        Float lastX = sensorvalues.getFloat("Azimuth1", 0.0f);
        Float lastY = sensorvalues.getFloat("Pitch1", 0.0f);
        Float lastZ = sensorvalues.getFloat("Roll1", 0.0f);
        if ((abs(event.values[0] - lastX) > 5) || ((abs(event.values[1] - lastY) > 5)) || ((abs(event.values[2] - lastZ) > 5)))
        {
            editor.putFloat("Azimuth1", event.values[0]);
            editor.putFloat("Pitch1", event.values[1]);
            editor.putFloat("Roll1", event.values[2]);
            editor.commit();
            return true;
        }
        else
        {
            return false;
        }
    }


    public boolean limit_exceeded(SensorEvent event)
    {
        int value = sensorvalues.getInt("angle_limit", 0);
        if ((Math.abs(event.values[1]) > value) || (Math.abs(event.values[2]) > value))
        {
            return true;
        }
        return false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        this.intent = intent;
        this.flags = flags;
        this.startId = startId;


        sensorvalues=getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sensorvalues.edit();

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

        if (mySensors.size() > 0)
        {
            mySensorManager.registerListener(Service_incalarm.this, mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            sersorrunning = true;
            Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG).show();
        } else
        {
            Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG).show();
            sersorrunning = false;

        }

        return super.onStartCommand(intent, flags, startId);
    }



        @Override
        public void onSensorChanged(SensorEvent event)
        {
            sensorvalues = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sensorvalues.edit();



            if (!(phonemoved(event)) && count > 15)
            {

                Long l = sensorvalues.getLong("laststoppedalarm", 0l);
                Long cur = System.currentTimeMillis();

                if(c==0)
                {

                    if (limit_exceeded(event) && (((cur - l) > 3600000) || (firsttime)) && (flag || first))
                    {

                        first = false;
                        firsttime = false;
                        try
                        {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getBaseContext(), notification);
                            count = 0;
                            v = (Vibrator) getSystemService(getBaseContext().VIBRATOR_SERVICE);
                            v.vibrate(50);
                            r.play();
                            show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }


    public void show()
    {
            sensorvalues = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sensorvalues.edit();

            AlertDialog.Builder box = new AlertDialog.Builder(getBaseContext());
            box.setTitle("Confirmation!!!");
            box.setMessage("Phone set at an UNSAFE inclination");

            box.setPositiveButton("STOP MONITORING", new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Toast.makeText(getApplicationContext(), "Alert set off ", Toast.LENGTH_LONG).show();
                   stop();
                    flag = true;

                }
            });

        /*    box.setNegativeButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Toast.makeText(getApplicationContext(), "Alerting set off for an hour", Toast.LENGTH_LONG).show();
                    flag = true;
                    editor.putLong("laststoppedalarm", System.currentTimeMillis());
                    editor.commit();
                }
            });
*/
            final AlertDialog b = box.create();
            b.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            b.show();
        }
    private void stop()
    {
        Intent i=new Intent(getBaseContext(),Service_incalarm.class);
        Toast.makeText(getApplicationContext(), "Service is Off and stopped", Toast.LENGTH_LONG).show();
        stopService(i);
    }
}
