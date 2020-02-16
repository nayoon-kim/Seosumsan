package com.bgp.seosumsan.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bgp.seosumsan.SaveSharedPreference;

public class SplashActivity extends BasicAcitivty {
    private Intent intent;

    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        if(SaveSharedPreference.getUserId(SplashActivity.this).length() == 0) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
