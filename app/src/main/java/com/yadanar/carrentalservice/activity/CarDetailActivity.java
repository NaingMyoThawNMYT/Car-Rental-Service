package com.yadanar.carrentalservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.Car;

public class CarDetailActivity extends AppCompatActivity {
    public static final String KEY_CAR_PARAM = "key_car_param";
    public static final String KEY_CAR_TYPE_NAME_PARAM = "key_car_type_param";

    private Car car = null;

    private ImageView imgCar;
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

        imgCar = findViewById(R.id.img_car);
        tvType = findViewById(R.id.tv_car_type);
        tvPrice = findViewById(R.id.tv_price);
        tvYear = findViewById(R.id.tv_year);
        tvSeats = findViewById(R.id.tv_seats);
        tvColor = findViewById(R.id.tv_color);
        tvDescription = findViewById(R.id.tv_description);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_CAR_PARAM)) {
            car = (Car) b.get(KEY_CAR_PARAM);

            if (car != null) {
                // TODO: 9/19/19 fetch and set car photo

                if (b.containsKey(KEY_CAR_TYPE_NAME_PARAM)) {
                    car.setTypeName(b.getString(KEY_CAR_TYPE_NAME_PARAM));
                }

                tvType.setText(car.getTypeName());
                tvPrice.setText(car.getPrice() + "ks/hr");
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
