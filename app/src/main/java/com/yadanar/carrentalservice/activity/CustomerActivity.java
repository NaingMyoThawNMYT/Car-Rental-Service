package com.yadanar.carrentalservice.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.CarListRvAdapter;
import com.yadanar.carrentalservice.listener.CarListItemOnClickListener;
import com.yadanar.carrentalservice.model.Car;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    private boolean adminMode = false;

    private AppCompatEditText edtSearch;
    private RecyclerView rvCarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        edtSearch = findViewById(R.id.edt_search);
        rvCarList = findViewById(R.id.rv_car_list);

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
        final CarListRvAdapter carListRvAdapter = new CarListRvAdapter(carList,
                new CarListItemOnClickListener() {
                    @Override
                    public void onClick(Car car, int position) {
                        Intent i = new Intent(CustomerActivity.this,
                                adminMode
                                        ? CarEditorActivity.class
                                        : CarDetailActivity.class);
                        i.putExtra(CarDetailActivity.KEY_CAR_PARAM, car);
                        startActivity(i);
                    }
                });
        rvCarList.setAdapter(carListRvAdapter);

        Bundle b = getIntent().getExtras();
        if (b != null
                && b.containsKey(AdminActivity.ADMIN_KEY)
                && b.getBoolean(AdminActivity.ADMIN_KEY)) {
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
            adminMode = true;
        }

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                carListRvAdapter.getFilter().filter(editable.toString());
            }
        });

        setLayoutManager(getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setLayoutManager(newConfig);
    }

    private void setLayoutManager(Configuration configuration) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(),
                configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? 3
                        : 2);
        rvCarList.setLayoutManager(gridLayoutManager);
    }

    public void addNewCar(View v) {
        startActivity(new Intent(this, CarEditorActivity.class));
    }
}
