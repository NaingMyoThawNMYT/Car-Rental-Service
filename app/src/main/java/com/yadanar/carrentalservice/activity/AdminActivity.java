package com.yadanar.carrentalservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yadanar.carrentalservice.R;

public class AdminActivity extends AppCompatActivity {
    public static final String ADMIN_KEY = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void launchDashboardActivity(View v) {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void launchCarListActivity(View v) {
        Intent i = new Intent(this, CustomerActivity.class);
        i.putExtra(ADMIN_KEY, true);
        startActivity(i);
    }

    public void launchCarTypesActivity(View v) {
        startActivity(new Intent(this, CarTypeListActivity.class));
    }
}
