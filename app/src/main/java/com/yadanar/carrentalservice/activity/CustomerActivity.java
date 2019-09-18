package com.yadanar.carrentalservice.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.CarListRvAdapter;
import com.yadanar.carrentalservice.listener.CarListItemOnClickListener;
import com.yadanar.carrentalservice.model.Car;
import com.yadanar.carrentalservice.storage.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRefCar = database.getReference(FirebaseHelper.CAR_LIST_TABLE_NAME);

    private boolean adminMode = false;

    private AppCompatEditText edtSearch;
    private RecyclerView rvCarList;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        edtSearch = findViewById(R.id.edt_search);
        rvCarList = findViewById(R.id.rv_car_list);

        dialog = new ProgressDialog(this);

        rvCarList.setHasFixedSize(true);

        final CarListRvAdapter carListRvAdapter = new CarListRvAdapter(
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

        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        dbRefCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                List<Car> carList = parseCarList((Map<String, Object>) dataSnapshot.getValue());
                carListRvAdapter.setDataSet(carList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
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

    private List<Car> parseCarList(Map<String, Object> carMap) {
        List<Car> carList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : carMap.entrySet()) {
            //Get car map
            Map singleCar = (Map) entry.getValue();
            Car car = new Car();
            car.setAvailable(Boolean.valueOf(String.valueOf(singleCar.get("available"))));
            car.setColor(String.valueOf(singleCar.get("color")));
            car.setDescription(singleCar.get("description") == null
                    ? ""
                    : String.valueOf(singleCar.get("description")));
            car.setId(String.valueOf(singleCar.get("id")));
            car.setPrice(Double.valueOf(String.valueOf(singleCar.get("price"))));
            car.setSeats(Integer.valueOf(String.valueOf(singleCar.get("seats"))));
            car.setType(String.valueOf(singleCar.get("type")));
            car.setYear(Integer.valueOf(String.valueOf(singleCar.get("year"))));
            carList.add(car);
        }

        return carList;
    }
}
