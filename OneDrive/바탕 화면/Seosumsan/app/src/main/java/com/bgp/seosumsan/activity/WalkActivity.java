package com.bgp.seosumsan.activity;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bgp.seosumsan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WalkActivity extends BasicAcitivty implements SensorEventListener {
    private SensorManager sensorManager;
    private  Sensor stepCountSensor;
    private TextView tvStepCount;
    private TextView tvStepInfo;
    private  TextView tvStepKcal;
    private int stepcount;

    private View procress;
    private ProgressBar progressBar;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        tvStepCount=(TextView)findViewById(R.id.wamount);
        tvStepInfo=(TextView)findViewById(R.id.wdistance);
        tvStepKcal=(TextView)findViewById(R.id.wkcal);

        tvStepCount=(TextView)findViewById(R.id.wamount);
        tvStepInfo=(TextView)findViewById(R.id.wdistance);
        tvStepKcal=(TextView)findViewById(R.id.wkcal);

        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor=sensorManager.getDefaultSensor((Sensor.TYPE_STEP_COUNTER));
        if(stepCountSensor==null)
            Toast.makeText(this,"찾을 수 없음",Toast.LENGTH_SHORT).show();
        progressBar=(ProgressBar)findViewById(R.id.progress_c);
        progressBar.setProgress(0);
        tvStepInfo.setText(0+"km");
        tvStepKcal.setText(0+"kcal");

        backButton = (ImageView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView_main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuitem_bottombar_1:
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                return true;
                            case R.id.menuitem_bottombar_2:
                                intent = new Intent(getBaseContext(), WalkActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.menuitem_bottombar_3:
                                intent = new Intent(getBaseContext(), MyPageActivity.class);
                                startActivity(intent);
                                return true;
                        }
                        return false;
                    }
                });
    }

    protected  void onResume(){
        super.onResume();
        sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected  void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_STEP_COUNTER){
            tvStepCount.setText(String.valueOf(sensorEvent.values[0])+" 걸음");
        }
        progressBar.setProgress((int)sensorEvent.values[0]);

        tvStepInfo.setText(Math.round(6*sensorEvent.values[0]/10000*1000)/1000.0 +"km");
        tvStepKcal.setText(Math.round(360*sensorEvent.values[0]/10000*100)/100.0 +"kcal");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
