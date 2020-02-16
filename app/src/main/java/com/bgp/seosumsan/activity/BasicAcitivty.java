package com.bgp.seosumsan.activity;

import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BasicAcitivty extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        deleteStatusBar();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        deleteStatusBar();
        return super.onTouchEvent(event);
    }

    private void deleteStatusBar(){
        View decorView;
        int uiOption;

        decorView = getWindow().getDecorView();
        uiOption = decorView.getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility( uiOption );
    }
}
