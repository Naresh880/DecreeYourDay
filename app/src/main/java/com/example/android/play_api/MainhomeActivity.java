package com.example.android.play_api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainhomeActivity extends AppCompatActivity {

    private Button home;
    private CoordinatorLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainhome);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        layout = (CoordinatorLayout) findViewById(R.id.draw);

        if(getThem().equals("Dark"))
        {
            layout.setBackgroundColor(Color.parseColor("#000000"));
        }
        home = (Button) findViewById(R.id.home_btn);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainhomeActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getThem() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PLAY_API",MODE_PRIVATE);
        return prefs.getString("Theme","Light");
    }

}
