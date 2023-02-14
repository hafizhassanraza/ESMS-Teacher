package com.enfotrix.cgscteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SaveSharedPreferences.getUserName(SplashActivity.this).length() == 0)
                {
                    Intent intent = new Intent(SplashActivity.this, SelectActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {


                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }

            }
        }, 2000);
    }
}