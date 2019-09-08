package com.yadanar.carrentalservice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.CarListRvAdapter;
import com.yadanar.carrentalservice.model.CarModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    private AppCompatSpinner spnTypeList;
    private RecyclerView rvCarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        spnTypeList = findViewById(R.id.spn_type_list);
        rvCarList = findViewById(R.id.rv_car_list);

        final String[] types = new String[]{"A", "B", "C"};
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this,
                R.layout.simple_spinner_item,
                types);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnTypeList.setAdapter(spnAdapter);

        spnTypeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CustomerActivity.this, types[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        rvCarList.setHasFixedSize(true);

        List<CarModel> carList = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            CarModel car = new CarModel();
            car.setType("Car " + i);
            carList.add(car);
        }
        CarListRvAdapter carListRvAdapter = new CarListRvAdapter(carList,
                new CarListRvAdapter.CarListItemOnClickListener() {
                    @Override
                    public void onClick(CarModel car, int position) {
                        Toast.makeText(CustomerActivity.this, car.getType(), Toast.LENGTH_SHORT).show();
                    }
                });
        rvCarList.setAdapter(carListRvAdapter);
    }
}
