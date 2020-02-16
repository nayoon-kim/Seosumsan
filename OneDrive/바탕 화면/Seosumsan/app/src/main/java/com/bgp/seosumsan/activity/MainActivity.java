package com.bgp.seosumsan.activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bgp.seosumsan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BasicAcitivty {
    private FrameLayout background;
    private String swit = "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        background = (FrameLayout) findViewById(R.id.map);

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

    public void onTextViewClick(View view) {
        TextView textView = (TextView) view;
        String str = textView.getText().toString();

        if (swit.compareTo(str)==0) {
            Intent intent = new Intent(getBaseContext(), SelectActivity.class);
            intent.putExtra("group", str);
            startActivity(intent);
        } else {
            if (str.compareTo("N")==0) { //N
                swit = str;
                background.setBackgroundResource(R.drawable.seoul_map_n);
            } else if (str.compareTo("E")==0) { //E
                swit = str;
                background.setBackgroundResource(R.drawable.seoul_map_e);
            } else if (str.compareTo("W")==0) { //W
                swit = str;
                background.setBackgroundResource(R.drawable.seoul_map_w);
            } else if (str.compareTo("S")==0) { //S
                swit = str;
                background.setBackgroundResource(R.drawable.seoul_map_s);
            } else if (str.compareTo("M")==0) { //M
                swit = str;
                background.setBackgroundResource(R.drawable.seoul_map_m);
            }
        }
    }
}
