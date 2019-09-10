
package com.yadanar.carrentalservice.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.Car;

public class CarEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_editor);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(CarDetailActivity.KEY_CAR_PARAM)) {
            Car car = (Car) b.get(CarDetailActivity.KEY_CAR_PARAM);

            if (car != null) {
                Toast.makeText(this, car.getType(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
