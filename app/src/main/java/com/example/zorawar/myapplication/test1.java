/*package com.example.zorawar.myapplication;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.abs;


    public class test1 extends Activity {
     /*
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.testing);

            SensorManager sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

            final float[] mValuesMagnet      = new float[3];
            final float[] mValuesAccel       = new float[3];
            final float[] mValuesOrientation = new float[3];
            final float[] mRotationMatrix    = new float[9];

            final Button btn_valider = (Button) findViewById(R.id.button);
            final TextView txt1 = (TextView) findViewById(R.id.textView3);
            final SensorEventListener mEventListener = new SensorEventListener() {
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }

                public void onSensorChanged(SensorEvent event) {
                    // Handle the events for which we registered
                    switch (event.sensor.getType()) {
                        case Sensor.TYPE_ACCELEROMETER:
                            System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                            break;

                        case Sensor.TYPE_MAGNETIC_FIELD:
                            System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                            break;
                    }
                };
            };

            // You have set the event lisetner up, now just need to register this with the
            // sensor manager along with the sensor wanted.
            setListners(sensorManager, mEventListener);

            btn_valider.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
                    SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
                    final CharSequence test;
                    test = "results: " + mValuesOrientation[0] +" "+mValuesOrientation[1]+ " "+ mValuesOrientation[2];
                    txt1.setText(test);
                }
            });

        }

        // Register the event listener and sensor type.
        public void setListners(SensorManager sensorManager, SensorEventListener mEventListener)
        {
            sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
}







        TextView textviewAzimuth, textviewPitch, textviewRoll;
        private static SensorManager mySensorManager;
        private boolean sersorrunning;
        SharedPreferences sensorvalues;
        Vibrator v;
        public static int count = 0;
        public boolean flag = false;
        public boolean first = true;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.test);
            textviewAzimuth = (TextView) findViewById(R.id.textazimuth);
            textviewPitch = (TextView) findViewById(R.id.textpitch);
            textviewRoll = (TextView) findViewById(R.id.textroll);
            sensorvalues = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sensorvalues.edit();

            mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

            if (mySensors.size() > 0) {
                mySensorManager.registerListener(mySensorEventListener, mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
                sersorrunning = true;
                Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG).show();
                sersorrunning = false;
                //  finish();
            }

        }

        public boolean phonemoved(SensorEvent event) {
            count++;
            sensorvalues = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sensorvalues.edit();
            Float lastX = sensorvalues.getFloat("Azimuth", 0.0f);
            Float lastY = sensorvalues.getFloat("Pitch", 0.0f);
            Float lastZ = sensorvalues.getFloat("Roll", 0.0f);
            if ((abs(event.values[0] - lastX) > 5) || ((abs(event.values[1] - lastY) > 5)) || ((abs(event.values[2] - lastZ) > 5))) {

                editor.putLong("lastupdatingtime", System.currentTimeMillis());
                editor.putFloat("Azimuth", event.values[0]);
                editor.putFloat("Pitch", event.values[1]);
                editor.putFloat("Roll", event.values[2]);
                editor.commit();
                Toast.makeText(getBaseContext(), "MOVED", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        }


        public boolean limit_exceeded(SensorEvent event) {
            int value = sensorvalues.getInt("angle_limit", 0);
            if ((Math.abs(event.values[1]) > value) || (Math.abs(event.values[2]) > value)) {
                return true;
            }
            return false;
        }


        private SensorEventListener mySensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                sensorvalues = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sensorvalues.edit();


                // Long time=System.currentTimeMillis();
                //Long lastupdate=sensorvalues.getLong("lastupdatingtime", 0l);
                //Long convertedtime=sensorvalues.getLong("convertedtime",0l);
                textviewAzimuth.setText("Azimuth: " + String.valueOf(event.values[0]));
                textviewPitch.setText("Pitch: " + String.valueOf(event.values[1]));
                textviewRoll.setText("Roll: " + String.valueOf(event.values[2]));
                if (!(phonemoved(event)) && count > 10) {

                    Long l = sensorvalues.getLong("laststoppedalarm1", 0l);
                    Long cur = System.currentTimeMillis();
                    if (limit_exceeded(event) && ((cur - l) > 4000) && (flag || first))// && !phonemoved(event))
                    {
                        first = false;
                        Toast.makeText(getBaseContext(), "angle limit exceeded", Toast.LENGTH_SHORT).show();
                        try {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            count = 0;
                            v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(50);
                            r.play();
                            show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //
                    // Vibrate for 500 milliseconds
                    //  v.vibrate(50);
                }
                // TODO Auto-generated method stub


                //  textviewAzimuth.setText("Azimuth: " + String.valueOf(event.values[0]));
                // textviewPitch.setText("Pitch: " + String.valueOf(event.values[1]));
                //textviewRoll.setText("Roll: " + String.valueOf(event.values[2]));

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO Auto-generated method stub

            }
        };

        public void show() {

            sensorvalues = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sensorvalues.edit();
            AlertDialog.Builder box = new AlertDialog.Builder(test1.this);
            box.setTitle("Confirmation!!!");
            box.setMessage("Phone set at an UNSAFE inclination");
            box.setPositiveButton("STOP MONITORING", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "Alert set off ", Toast.LENGTH_LONG).show();
                    flag = true;
                    onDestroy();
                }
            });

            box.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "Alerting set off for an hour", Toast.LENGTH_SHORT).show();
                    flag = true;
                    editor.putLong("laststoppedalarm1", System.currentTimeMillis());
                    editor.commit();
                }
            });
            box.show();
        }


        @Override
        protected void onDestroy()
        {
            // TODO Auto-generated method stub
            super.onDestroy();

            if (sersorrunning)
            {
                mySensorManager.unregisterListener(mySensorEventListener);
                Toast.makeText(test1.this, "unregisterListener", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

   /* @Override
    public void onSensorChanged(SensorEvent event) {
// TODO Auto-generated method stub
     /*   if (populatenewvalues(event))
        {
            textviewAzimuth.setText("Azimuth: " + String.valueOf(event.values[0]));
            textviewPitch.setText("Pitch: " + String.valueOf(event.values[1]));
            textviewRoll.setText("Roll: " + String.valueOf(event.values[2]));
        }

        Long time=System.currentTimeMillis();
        Long lastupdate=sensorvalues.getLong("lastupdatingtime",0);
        Long convertedtime=sensorvalues.getLong("convertedtime",0);
        if((time - lastupdate) > convertedtime)
        {
            Toast.makeText(getBaseContext(),"DATA OFF",Toast.LENGTH_LONG).show();
        }


    }*/










































/*package com.example.zorawar.myapplication;

        import android.app.Activity;
        import android.content.Context;

        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.os.Vibrator;
        import android.widget.TextView;
        import android.widget.Toast;




public class test extends Activity implements SensorEventListener {
        public Float lastX, lastY, lastZ;
        private SensorManager sensorManager;
        private Sensor accelerometer;
        private float deltaXMax = 0;
        private float  deltaYMax = 0;
        private float deltaZMax = 0;

        private float deltaX = 0;
        private float deltaY = 0;
        private float deltaZ = 0;

        private float vibrateThreshold = 0;
            long lastUpdate = System.currentTimeMillis();

        private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;
        public Vibrator v;

         @Override
     protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initializeViews();
       // startService(new Intent(getBaseContext(), test.class));

        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        { // success! we have an accelerometer
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        vibrateThreshold=2;
        }
        else
        {
// fai! we don't have an accelerometer!
        }
//initialize vibration

        }
            public void initializeViews()
            {

                currentX=(TextView) findViewById(R.id.currentX);
                currentY =(TextView) findViewById(R.id.currentY);
                currentZ =(TextView) findViewById(R.id.currentZ);
                maxX=(TextView) findViewById(R.id.maxX);
                maxY=(TextView) findViewById(R.id.maxY);
                maxZ=(TextView) findViewById(R.id.maxZ);
                // Create object of SharedPreferences.


//                lastX=Float.parseFloat(currentX.getText().toString());
  //              lastY=Float.parseFloat(currentY.getText().toString());
    //            lastZ=Float.parseFloat(currentZ.getText().toString());


           /*     SharedPreferences preferences= getDefaultSharedPreferences(this);
                //now get Editor
                SharedPreferences.Editor editor= preferences.edit();
                //put your value
                editor.putFloat("lastX", lastX);
                editor.putFloat("lastY", lastY);
                editor.putFloat("lastZ", lastZ);
                //commits your edits
                editor.commit();

            }





            protected void onResume()
            {

                super.onResume();

                sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
            }



          //  public void startService(View v) {
            //    startService(new Intent(getBaseContext(), test.class));
           // }

            //onPause() unregister the accelerometer for stop listening the events
         protected void onPause()
         {
         super.onPause();
         sensorManager.unregisterListener(this);
         }
  @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {

        }
        @Override
        public void onSensorChanged(SensorEvent event) {

// In onSensorChanged:
            //long curTime = System.currentTimeMillis();


            // clean current values
        displayCleanValues();
            // display the current x,y,z accelerometer values
        displayCurrentValues(event);
            // display the max x,y,z accelerometer values
        displayMaxValues();

            // get the change of the x,y,z values of the accelerometer




            deltaX = Math.abs(lastX - event.values[0]);
            deltaY = Math.abs(lastY - event.values[1]);
            deltaZ = Math.abs(lastZ - event.values[2]);

            // if the change is below 2, it is just plain noise
            if (deltaX < vibrateThreshold)
                deltaX = 0;
            if (deltaY < vibrateThreshold)
                deltaY = 0;
            if (deltaZ < vibrateThreshold)
                deltaZ = 0;


            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];
            vibrate();
        }
        public void vibrate()
        {
            long curTime=System.currentTimeMillis();
            v=(Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        if(( deltaZ > vibrateThreshold ) || ( deltaY > vibrateThreshold ) || ( deltaZ > (vibrateThreshold-1) ))
        {
            if ((curTime - lastUpdate) > 2000){ // only reads data twice per second
                lastUpdate = curTime;
                Toast.makeText(getApplicationContext(),"time: "+lastUpdate,Toast.LENGTH_SHORT).show();
            }
        v.vibrate(50);
        }
        }

        public void displayCleanValues()
        {
            currentX.setText("0.0");
            currentY.setText("0.0");
            currentZ.setText("0.0");
        }
// display the current x,y,z accelerometer values
public void displayCurrentValues(SensorEvent event)
{
        currentX.setText(Float.toString(event.values[0]));
        currentY.setText(Float.toString(event.values[1]));
        currentZ.setText(Float.toString(event.values[2]));
    SharedPreferences preferences= getSharedPreferences("mypref", 0);
    preferences.Editor editor=preferences.edit();
    //now get Editor
    Float curX=preferences.getFloat("lastX",0.0f);
    Float curZ=preferences.getFloat("lastZ", 0.0f);
    Float curY=preferences.getFloat("lastY", 0.0f);
    //put your value
    editor.putFloat("lastX", event.values[0]);
    editor.putFloat("lastY", event.values[1]);
    editor.putFloat("lastZ", event.values[2]);
    //commits your edits
    editor.commit();
}

// display the max x,y,z accelerometer values

public void displayMaxValues()
{
        if(deltaX > deltaXMax)
        {
            deltaXMax = deltaX;
            maxX.setText(Float.toString(deltaXMax));
        }

        if(deltaY > deltaYMax)
        {
            deltaYMax = deltaY;
            maxY.setText(Float.toString(deltaYMax));
        }

        if(deltaZ > deltaZMax)
        {
            deltaZMax = deltaZ;
            maxZ.setText(Float.toString(deltaZMax));
        }
}
}*/