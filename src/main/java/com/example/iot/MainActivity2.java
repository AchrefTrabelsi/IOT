package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity2 extends AppCompatActivity {
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ProgressBar luminosityBar = findViewById(R.id.luminosityBar);
        TextView luminosityValue = findViewById(R.id.luminosityValue);
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        final float[] max = {0};
        final float[] old = {0};
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.luminosityBar),"",Snackbar.LENGTH_SHORT);
        SensorEventListener sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                int change=50;
                float luminosity=sensorEvent.values[0];
                if(luminosity> max[0])
                    max[0] =luminosity;
                if(luminosity-old[0]>change){
                    mySnackbar.setText("Luminosity Increased");
                    mySnackbar.show();
                }else if(luminosity-old[0]<-change){
                    mySnackbar.setText("Luminosity Decreased");
                    mySnackbar.show();
                }
                old[0]=luminosity;

                int progress = Math.round((luminosity/ max[0])*100);
                luminosityValue.setText(""+luminosity);

                luminosityBar.setProgress(progress);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_FASTEST );

        SensorEventListener accelerometerSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                System.arraycopy(sensorEvent.values, 0, accelerometerReading,
                        0, accelerometerReading.length);
                getOrientation();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        SensorEventListener magnetometerSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                System.arraycopy(sensorEvent.values, 0, magnetometerReading,
                        0, magnetometerReading.length);
                getOrientation();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };


        sensorManager.registerListener(accelerometerSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI );
        sensorManager.registerListener(magnetometerSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI );




    }
    private void getOrientation(){

        TextView test=findViewById(R.id.test);

        final float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrix(rotationMatrix, null,accelerometerReading, magnetometerReading);
        final float[] orientationAngles = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
        double angle = (360+Math.toDegrees(orientationAngles[0]))%360;
        ImageView img=findViewById(R.id.imageView);
        img.setRotation(-(float) angle);
        test.setText(angle+"");




    }
}