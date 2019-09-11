package com.yadanar.carrentalservice.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.DashboardCarListRvAdapter;
import com.yadanar.carrentalservice.model.Car;
import com.yadanar.carrentalservice.model.Customer;
import com.yadanar.carrentalservice.model.RentedCar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private DashboardCarListRvAdapter dashboardCarListRvAdapter;

    private AppCompatEditText edtSearch;
    private RecyclerView rvRentedCarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        edtSearch = findViewById(R.id.edt_search);
        rvRentedCarList = findViewById(R.id.rv_rented_car_list);

        List<RentedCar> rentedCarList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Car car = new Car();
            car.setType("Car " + i);
            car.setPrice(1000 * i);
            car.setYear(1000 * i);
            car.setSeats(i);
            car.setColor("Color " + i);
            car.setAvailable(false);

            Customer customer = new Customer();
            customer.setName("Customer " + i);
            customer.setDate(new Date().getTime());

            RentedCar rentedCar = new RentedCar();
            rentedCar.setCar(car);
            rentedCar.setCustomer(customer);

            rentedCarList.add(rentedCar);
        }
        dashboardCarListRvAdapter = new DashboardCarListRvAdapter(rentedCarList,
                new DashboardCarListRvAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(RentedCar rentedCar, int position) {
                        // TODO: 9/11/19 go to check out

                        Toast.makeText(DashboardActivity.this,
                                rentedCar.getCustomer().getName(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        rvRentedCarList.setAdapter(dashboardCarListRvAdapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                dashboardCarListRvAdapter.getFilter().filter(editable.toString());
            }
        });
    }
}
