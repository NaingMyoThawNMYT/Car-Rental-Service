package com.yadanar.carrentalservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchAdminMode(View v) {
        // TODO: 9/8/19 go to admin activity
    }

    public void launchCustomerMode(View v) {
        startActivity(new Intent(MainActivity.this, CustomerActivity.class));
    }
}
