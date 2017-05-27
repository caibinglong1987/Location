package com.test.locationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.test.locationtest.services.LocationInformationService;

import static android.content.Intent.ACTION_MAIN;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, LocationInformationService.class);
        serviceIntent.setAction(ACTION_MAIN);
        startService(serviceIntent);
    }
}
