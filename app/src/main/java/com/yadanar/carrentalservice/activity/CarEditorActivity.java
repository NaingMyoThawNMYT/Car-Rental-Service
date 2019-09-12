
package com.yadanar.carrentalservice.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.Car;

public class CarEditorActivity extends AppCompatActivity {
    private Car car = null;

    private AppCompatTextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_editor);

        tvTitle = findViewById(R.id.tv_title);

        String title = "";
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(CarDetailActivity.KEY_CAR_PARAM)) {
                car = (Car) b.get(CarDetailActivity.KEY_CAR_PARAM);

                if (car != null) {
                    // TODO: 9/12/19 set data to ui

                    title = car.getType();
                }
            }
        }

        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.add_new);
        }

        tvTitle.setText(title);
    }

    public void cancel(View v) {
        finish();
    }

    public void save(View v) {
        // TODO: 9/12/19 save to firebase

        finish();
    }
}
