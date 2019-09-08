package com.yadanar.carrentalservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.CarModel;

public class CarDetailActivity extends AppCompatActivity {
    public static final String KEY_CAR_PARAM = "key_car_param";

    private CarModel car = null;

    private TextView tvType,
            tvPrice,
            tvYear,
            tvSeats,
            tvColor,
            tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        tvType = findViewById(R.id.tv_car_type);
        tvPrice = findViewById(R.id.tv_price);
        tvYear = findViewById(R.id.tv_year);
        tvSeats = findViewById(R.id.tv_seats);
        tvColor = findViewById(R.id.tv_color);
        tvDescription = findViewById(R.id.tv_description);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_CAR_PARAM)) {
            car = (CarModel) b.get(KEY_CAR_PARAM);

            if (car != null) {
                tvType.setText(car.getType());
                tvPrice.setText(car.getPrice() + "Ks/hr");
                tvYear.setText(String.valueOf(car.getYear()));
                tvSeats.setText(String.valueOf(car.getSeats()));
                tvColor.setText(car.getColor());
                tvDescription.setText(car.getDescription());
            }
        }

        if (car == null || !car.isAvailable()) {
            findViewById(R.id.btn_book_now).setVisibility(View.GONE);
        }
    }

    public void bookNow(View v) {
        Intent i = new Intent(CarDetailActivity.this, BookingActivity.class);
        i.putExtra(CarDetailActivity.KEY_CAR_PARAM, car);
        startActivity(i);
    }
}
