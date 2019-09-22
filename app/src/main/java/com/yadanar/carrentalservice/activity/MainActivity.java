package com.yadanar.carrentalservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yadanar.carrentalservice.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchAdminMode(View v) {
        startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
    }

    public void launchCustomerMode(View v) {
        startActivity(new Intent(MainActivity.this, CustomerActivity.class));
    }
}
