package com.yadanar.carrentalservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.CarListManageRvAdapter;
import com.yadanar.carrentalservice.listener.CarListItemOnClickListener;
import com.yadanar.carrentalservice.model.Car;

import java.util.ArrayList;
import java.util.List;

public class CarListManageActivity extends AppCompatActivity {
    private RecyclerView rvCarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list_manage);

        rvCarList = findViewById(R.id.rv_car_list);

        ((TextView) findViewById(R.id.tv_title)).setText(R.string.car_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCarList.setLayoutManager(linearLayoutManager);
        rvCarList.setHasFixedSize(true);

        List<Car> carList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Car car = new Car();
            car.setType("Car " + i);
            car.setPrice(1000 * i);
            car.setYear(1000 * i);
            car.setSeats(i);
            car.setColor("Color " + i);
            car.setAvailable(i % 2 == 0);
            carList.add(car);
        }
        CarListManageRvAdapter adapter = new CarListManageRvAdapter(carList,
                new CarListItemOnClickListener() {
                    @Override
                    public void onClick(Car car, int position) {
                        goToCarEditorActivity(car);
                    }
                });
        rvCarList.setAdapter(adapter);

        (findViewById(R.id.btn_add_new)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCarEditorActivity(null);
            }
        });
    }

    private void goToCarEditorActivity(Car car) {
        Intent i = new Intent(this, CarEditorActivity.class);
        i.putExtra(CarDetailActivity.KEY_CAR_PARAM, car);
        startActivity(i);
    }
}
