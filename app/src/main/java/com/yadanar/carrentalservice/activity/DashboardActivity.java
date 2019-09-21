package com.yadanar.carrentalservice.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.DashboardCarListRvAdapter;
import com.yadanar.carrentalservice.model.Car;
import com.yadanar.carrentalservice.model.CarType;
import com.yadanar.carrentalservice.model.Customer;
import com.yadanar.carrentalservice.model.RentedCar;
import com.yadanar.carrentalservice.storage.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    public static final String KEY_RENTED_CAR_PARAM = "key_rented_car_param";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference customerTable = db.getReference(FirebaseHelper.CUSTOMER_LIST_TABLE_NAME);
    private DatabaseReference carTable = db.getReference(FirebaseHelper.CAR_LIST_TABLE_NAME);
    private DatabaseReference carTypeTable = db.getReference(FirebaseHelper.CAR_TYPE_LIST_TABLE_NAME);

    private DashboardCarListRvAdapter dashboardCarListRvAdapter;

    private AppCompatEditText edtSearch;
    private RecyclerView rvRentedCarList;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        edtSearch = findViewById(R.id.edt_search);
        rvRentedCarList = findViewById(R.id.rv_rented_car_list);

        dashboardCarListRvAdapter = new DashboardCarListRvAdapter(
                new DashboardCarListRvAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(RentedCar rentedCar, int position) {
                        Intent i = new Intent(DashboardActivity.this,
                                CheckOutActivity.class);
                        i.putExtra(KEY_RENTED_CAR_PARAM, rentedCar);
                        i.putExtra(CarDetailActivity.KEY_CAR_TYPE_NAME_PARAM, rentedCar.getCar().getTypeName());
                        startActivity(i);
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

        dialog = new ProgressDialog(DashboardActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        customerTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }

                final List<Customer> customerList = parseCustomerList((Map<String, Object>) dataSnapshot.getValue());

                carTypeTable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot carTypeDataSnapshot) {
                        if (!carTypeDataSnapshot.exists()) {
                            return;
                        }

                        final List<CarType> carTypeList = new ArrayList<>();
                        for (DataSnapshot snapshot : carTypeDataSnapshot.getChildren()) {
                            carTypeList.add(CarType.parseCarType(snapshot));
                        }

                        Query query = carTable.orderByChild("available").equalTo(false);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dialog.dismiss();

                                if (!dataSnapshot.exists()) {
                                    return;
                                }

                                List<Car> carList = CustomerActivity.parseCarList(
                                        (Map<String, Object>) dataSnapshot.getValue(),
                                        carTypeList);
                                List<RentedCar> rentedCarList = parseRentedCar(customerList, carList);
                                dashboardCarListRvAdapter.setDataSet(rentedCarList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                dialog.dismiss();
                                Toast.makeText(DashboardActivity.this,
                                        databaseError.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(DashboardActivity.this,
                                databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(DashboardActivity.this,
                        databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Customer> parseCustomerList(Map<String, Object> customerMap) {
        List<Customer> customerList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : customerMap.entrySet()) {
            Map map = (Map) entry.getValue();
            Customer customer = new Customer();
            customer.setId(String.valueOf(map.get("id")));
            customer.setName(String.valueOf(map.get("name")));
            customer.setNrc(String.valueOf(map.get("nrc")));
            customer.setEmail(String.valueOf(map.get("email")));
            customer.setPhone(String.valueOf(map.get("phone")));
            customer.setAddress(String.valueOf(map.get("address")));
            customer.setGender(String.valueOf(map.get("gender")));
            customer.setCarId(String.valueOf(map.get("carId")));
            customer.setDate(Long.valueOf(String.valueOf(map.get("date"))));
            customerList.add(customer);
        }

        return customerList;
    }

    private List<RentedCar> parseRentedCar(List<Customer> customerList, List<Car> carList) {
        List<RentedCar> rentedCarList = new ArrayList<>();

        for (Customer customer : customerList) {
            for (Car car : carList) {
                if (customer.getCarId().equals(car.getId())) {
                    RentedCar rentedCar = new RentedCar();
                    rentedCar.setCustomer(customer);
                    rentedCar.setCar(car);
                    rentedCarList.add(rentedCar);
                }
            }
        }

        return rentedCarList;
    }
}
