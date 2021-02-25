package com.rt.qpay99.activity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;

public class SplashScreen extends FragmentActivity {

    TextView tvVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
//        Intent i = new Intent(SplashScreen.this, LoginUI.class);
//        startActivity(i);
//        finish();

        tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText("Version " + SRSApp.versionName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginUI.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }

}
